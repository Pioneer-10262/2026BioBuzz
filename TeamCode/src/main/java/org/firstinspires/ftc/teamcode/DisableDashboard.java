package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Disable Dashboard", group = "Dashboard")
public class DisableDashboard extends LinearOpMode {
    @Override
    public void runOpMode() {
        telemetry.addLine("Press start to disable FTC Dashboard");
        telemetry.addData("Dashboard", DashboardControl.status());
        telemetry.update();

        waitForStart();
        if (isStopRequested()) {
            return;
        }

        boolean disabled = DashboardControl.setEnabled(false);
        telemetry.addLine(disabled ? "Dashboard disabled" : "Could not disable dashboard");
        telemetry.addData("Dashboard", DashboardControl.status());
        telemetry.update();

        while (opModeIsActive()) {
            idle();
        }
    }
}
