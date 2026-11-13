
package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@Config
@TeleOp(name = "DcMotorEx PID Tuner", group = "Tuning")
public class DcMotorExPidTuner extends LinearOpMode {

    // Dashboard-adjustable PIDF constants
    public static volatile double P = 10.0;
    public static volatile double I = 3.0;
    public static volatile double D = 0.0;
    public static volatile double F = 0.0;

    // Dashboard-adjustable targets
    public static volatile int TARGET_POSITION = 1000;
    public static volatile double TARGET_VELOCITY = 500.0;

    public static volatile double MAX_POWER = 1.0;

    // POSITION or VELOCITY
    public static volatile String TUNING_MODE = "VELOCITY";

    private DcMotorEx motor;

    private double lastP = Double.NaN;
    private double lastI = Double.NaN;
    private double lastD = Double.NaN;
    private double lastF = Double.NaN;

    private DcMotor.RunMode currentMode = null;

    private int lastTargetPosition = Integer.MIN_VALUE;

    @Override
    public void runOpMode() throws InterruptedException {

        DashboardControl.setEnabled(true);

        telemetry = new MultipleTelemetry(
                telemetry,
                FtcDashboard.getInstance().getTelemetry()
        );

        motor = hardwareMap.get(DcMotorEx.class, "flywheel");

        motor.setZeroPowerBehavior(
                DcMotor.ZeroPowerBehavior.BRAKE
        );

        motor.setPower(0);

        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addLine("PID Tuner Initialized");
        telemetry.addLine("Waiting for start...");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            // Read dashboard values every iteration
            double p = P;
            double i = I;
            double d = D;
            double f = F;

            String tuningMode = TUNING_MODE;

            int targetPosition = TARGET_POSITION;
            double targetVelocity = TARGET_VELOCITY;

            double maxPower = Math.max(
                    0.0,
                    Math.min(1.0, MAX_POWER)
            );

            // Validate tuning mode
            boolean positionMode =
                    "POSITION".equalsIgnoreCase(tuningMode);

            boolean velocityMode =
                    "VELOCITY".equalsIgnoreCase(tuningMode);

            if (!positionMode && !velocityMode) {

                motor.setPower(0);

                telemetry.addData("ERROR", "Invalid tuning mode");
                telemetry.addData("Requested Mode", tuningMode);

                telemetry.update();

                idle();
                continue;
            }

            DcMotor.RunMode requestedMode = positionMode
                    ? DcMotor.RunMode.RUN_TO_POSITION
                    : DcMotor.RunMode.RUN_USING_ENCODER;

            // Handle mode transitions
            if (currentMode != requestedMode) {

                motor.setPower(0);

                motor.setMode(requestedMode);

                currentMode = requestedMode;

                // Force all PIDF values to be reapplied
                lastP = Double.NaN;
                lastI = Double.NaN;
                lastD = Double.NaN;
                lastF = Double.NaN;

                // Force position target to be reapplied
                lastTargetPosition = Integer.MIN_VALUE;
            }

            // Apply updated PIDF coefficients
            boolean pidChanged =
                    p != lastP ||
                            i != lastI ||
                            d != lastD ||
                            f != lastF;

            if (pidChanged) {

                if (positionMode) {

                    // RUN_TO_POSITION uses position P control
                    motor.setPositionPIDFCoefficients(p);

                } else {

                    // RUN_USING_ENCODER uses velocity PIDF
                    motor.setVelocityPIDFCoefficients(
                            p, i, d, f
                    );
                }

                // Cache applied values
                lastP = p;
                lastI = i;
                lastD = d;
                lastF = f;
            }

            // POSITION CONTROL
            if (positionMode) {

                if (targetPosition != lastTargetPosition) {

                    motor.setTargetPosition(targetPosition);

                    lastTargetPosition = targetPosition;
                }

                motor.setPower(maxPower);

                int actualPosition = motor.getCurrentPosition();

                int error = targetPosition - actualPosition;

                telemetry.addData("Mode", "POSITION");
                telemetry.addData("Target Position", targetPosition);
                telemetry.addData("Actual Position", actualPosition);
                telemetry.addData("Position Error", error);

                telemetry.addData("Busy", motor.isBusy());

                telemetry.addData("Position P", p);

            }

            // VELOCITY CONTROL
            else {

                // FTC SDK velocity units are encoder ticks per second
                if (Double.isFinite(targetVelocity)) {

                    motor.setVelocity(targetVelocity);

                } else {

                    motor.setVelocity(0);

                    telemetry.addData(
                            "ERROR",
                            "Invalid target velocity"
                    );
                }

                double actualVelocity = motor.getVelocity();

                double velocityError =
                        targetVelocity - actualVelocity;

                telemetry.addData("Mode", "VELOCITY");
                telemetry.addData("Target Velocity", targetVelocity);
                telemetry.addData("Actual Velocity", actualVelocity);
                telemetry.addData("Velocity Error", velocityError);

            }

            // Controller-reported coefficients
            PIDFCoefficients applied =
                    motor.getPIDFCoefficients(requestedMode);

            telemetry.addData("Requested P", p);
            telemetry.addData("Requested I", i);
            telemetry.addData("Requested D", d);
            telemetry.addData("Requested F", f);

            telemetry.addData("Hub P", applied.p);
            telemetry.addData("Hub I", applied.i);
            telemetry.addData("Hub D", applied.d);
            telemetry.addData("Hub F", applied.f);

            telemetry.addData("MAX POWER", maxPower);

            telemetry.update();

            idle();
        }

        motor.setPower(0);
    }
}
