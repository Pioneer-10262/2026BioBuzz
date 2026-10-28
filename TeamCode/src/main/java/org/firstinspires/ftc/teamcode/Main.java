package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.pedro.Constants;

@TeleOp
public class Main extends OpMode {
    private Follower follower;
    private DcMotorEx flywheel;
    private  DcMotorEx collector;
    private Servo ramp;

    boolean rampDown;

    @Override
    public void init() {
        follower = Constants.create(hardwareMap);

        flywheel = hardwareMap.get(DcMotorEx.class, "flywheel");
        collector = hardwareMap.get(DcMotorEx.class, "collector");
        ramp = hardwareMap.get(Servo.class, "ramp");
    }

    @Override
    public void loop() {
        double forward = -gamepad1.left_stick_y;
        double lateral = gamepad1.left_stick_x;
        double turn = gamepad1.right_stick_x;

        follower.manual(forward, lateral, turn);
        follower.update();

        // the code will run the motor at full velocity until we realese the trigger
        if (gamepad1.y) flywheel.setVelocity(1000); else flywheel.setVelocity(0);

        // The code will update every iteration and this will run as the right trigger is pulling spheres in and left trigger is pushing spheres out
        collector.setPower(gamepad1.right_trigger-gamepad1.left_trigger);

        // this code makes it so that if the button a is pressed and the ramp is up, it will come down, and if the ramp is down, it will come up.
        if (gamepad1.aWasPressed()) rampDown=!rampDown;
        if (rampDown) {
            ramp.setPosition(1);
        } else {
            ramp.setPosition(0);
        }


    }
}
