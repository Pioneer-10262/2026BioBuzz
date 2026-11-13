package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.drivetrain.Drivetrain;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Localizer;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("front left");
        c.frontRightName.set("front right");
        c.backLeftName.set("back left");
        c.backRightName.set("back right");
        c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.frontRightDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
    });

    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("pinpoint");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodOffset.set(3.322830500565176);
        c.yPodOffset.set(-6.940240334338091);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });

    // Sample from Pedro's example robot, not a measurement of this drivetrain.
    // Replace these with the Foresight tuner output.
    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.24356981204823544);
                Controller secondaryTranslationalForward = Controller.proportional(0.0899925372465799);
                Controller primaryTranslationalLateral = Controller.proportional(0.48010872899153956);
                Controller secondaryTranslationalLateral = Controller.proportional(0.1773873466208649);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.02661501897328306));
                c.brake.set(Controller.proportionalFeedforward(0.0226227661272906));

                c.headingFeedback.set(Controller.proportional(4.962674868746434));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.03227173385208946, 0.01482427112355658));

                c.linearBrakeCoefficients.set(Matrix.diag(0.07788682181536993, 0.0599717067381023));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.0016029960723033718, 0.001678137174086362));

                c.maxAchievableForwardVelocity.set(39.816082652398954);
                c.maxAchievableStrafeVelocity.set(31.652173320046508);
                c.naturalForwardDeceleration.set(37.74414809295111);
                c.naturalStrafeDeceleration.set(63.831201017080666);
            }
    );
    public static Follower create(HardwareMap h) {
        Localizer pinpoint = new PinpointLocalizer(h, localizerConfig);
        Drivetrain drivetrain = new Mecanum(h, drivetrainConfig);
        Foresight foresight = new Foresight(foresightConfig);

        return new Follower(pinpoint, drivetrain, foresight);
    }
}