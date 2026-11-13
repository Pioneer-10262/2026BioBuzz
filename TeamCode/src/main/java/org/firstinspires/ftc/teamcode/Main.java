package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.pedro.Constants;

@TeleOp
public class Main extends OpMode {
    // goBILDA 6000 rpm motor, 28 ticks per shaft revolution. Change both if the flywheel motor differs.
    private static final double TICKS_PER_REV = 28.0;
    private static final double FREE_RPM = 6000.0;
    private static final double TARGET_RPM = -2000;




    // Wheel is commanded this much faster so a compressed ball still leaves near 1000 rpm.
    private static final double COMPRESSION = 1.10;
    private static final double TARGET_TICKS_PER_SECOND = TARGET_RPM * COMPRESSION * TICKS_PER_REV / 60.0;
    private static final double AT_SPEED_TICKS = 100.0 * TICKS_PER_REV / 60.0;
    // F holds the commanded speed. P and a little I recover the dip when a ball compresses into the wheel.
    private static final double FLYWHEEL_P = 8.0;
    private static final double FLYWHEEL_I = 0.5;
    private static final double FLYWHEEL_D = 0.0;
    private static final double FLYWHEEL_F = 32767.0 / (FREE_RPM * TICKS_PER_REV / 60.0);

    private MecanumDrive drive;
    private DcMotorEx flywheel;
    private  DcMotorEx collector;
    private DcMotorEx gate;

    private Servo ramp;
    private Follower follower;

    boolean rampDown;

    @Override
    public void init() {
        drive = new MecanumDrive(hardwareMap);
        follower = Constants.create(hardwareMap);

        flywheel = hardwareMap.get(DcMotorEx.class, "flywheel");
        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheel.setVelocityPIDFCoefficients(FLYWHEEL_P, FLYWHEEL_I, FLYWHEEL_D, FLYWHEEL_F);

        collector = hardwareMap.get(DcMotorEx.class, "collector");
        gate = hardwareMap.get(DcMotorEx.class, "gate");
        ramp = hardwareMap.get(Servo.class, "ramp");

        rampDown = false;
    }

    @Override
    public void loop() {
        double forward = -gamepad1.left_stick_y;
        double lateral = -gamepad1.left_stick_x;
        double turn = -gamepad1.right_stick_x;
//
        follower.manual(forward, lateral, turn);
        follower.update();

        // the code will run the motor at full velocity until we realese the trigger
        if (gamepad1.y && gamepad2.a) {
            gate.setPower(1);
        } else {
            gate.setPower(0);
        }

        if (gamepad2.a){
            if (gamepad1.right_bumper) {
                flywheel.setVelocity(TARGET_TICKS_PER_SECOND*1.2);
            } else if (gamepad1.left_bumper) {
                flywheel.setVelocity(TARGET_TICKS_PER_SECOND*1.4);
            } else {
                flywheel.setVelocity(TARGET_TICKS_PER_SECOND);
            }
        } else {
            flywheel.setVelocity(0);
            gate.setPower(0);
        }

        // The code will update every iteration and this will run as the right trigger is pulling spheres in and left trigger is pushing spheres out
        collector.setPower(gamepad2.right_trigger-gamepad2.left_trigger);

        // this code makes it so that if the button a is pressed and the ramp is up, it will come down, and if the ramp is down, it will come up.
        if (gamepad1.rightTriggerWasPressed()) rampDown=!rampDown;

        if (rampDown) {
            ramp.setPosition(0.5);
        } else {
            ramp.setPosition(0.0);
        }

        telemetry.addData("Servo Position: ", ramp.getPosition());
        telemetry.addData("Flywheel rpm", flywheel.getVelocity() * 60.0 / TICKS_PER_REV);
        telemetry.addData("Flywheel target rpm", TARGET_RPM * COMPRESSION);
    }
}
