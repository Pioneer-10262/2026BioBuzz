package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Enable Dashboard", group = "Dashboard")
public class EnableDashboard extends LinearOpMode {
    @Override
    public void runOpMode() {
        telemetry.addLine("Press start to enable FTC Dashboard");
        telemetry.addData("Dashboard", DashboardControl.status());
        telemetry.update();

        waitForStart();
        if (isStopRequested()) {
            return;
        }

        boolean enabled = DashboardControl.setEnabled(true);
        telemetry.addLine(enabled ? "Dashboard enabled" : "Could not enable dashboard");
        telemetry.addData("Dashboard", DashboardControl.status());
        telemetry.update();

        while (opModeIsActive()) {
            idle();
        }
    }
}
