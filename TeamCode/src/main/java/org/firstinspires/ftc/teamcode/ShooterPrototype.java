package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class ShooterPrototype extends OpMode {
    DcMotorEx flywheel;
    Servo hood;

    @Override
    public void init() {
        flywheel = hardwareMap.get(DcMotorEx.class, "a");
        hood = hardwareMap.get(Servo.class, "hood");
    }

    @Override
    public void loop() {
        double position = hood.getPosition();
        double currentVelocity = flywheel.getVelocity();

        if (gamepad1.a) {
            hood.setPosition(position+0.1);
        } else if (gamepad1.b) {
            hood.setPosition(position-0.1);
        }

        if (gamepad1.x) {
            flywheel.setVelocity(currentVelocity - 10);
        } else if (gamepad2.y) {
            flywheel.setVelocity(currentVelocity + 10);
        }

        telemetry.addData("Servo Position: ", position);

        telemetry.addData("flywheel Velocity: ", currentVelocity);
    }
}
