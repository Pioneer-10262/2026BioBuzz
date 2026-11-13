package org.firstinspires.ftc.teamcode;

import android.content.Context;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.ftccommon.FtcEventLoop;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.ftccommon.external.OnCreateEventLoop;

import java.lang.reflect.Method;

/**
 * FTC Dashboard 0.6 registers its own enable op mode before the dashboard
 * instance exists, so that op mode never appears on the Driver Station.
 * Shutting down the Robot Controller also saves the dashboard as disabled.
 */
public final class DashboardControl {
    private static final String TAG = "DashboardControl";

    private DashboardControl() {
    }

    @OnCreateEventLoop
    public static void onCreateEventLoop(Context context, FtcEventLoop eventLoop) {
        if (!setEnabled(true)) {
            RobotLog.ee(TAG, "FTC Dashboard was disabled and could not be enabled");
        }
    }

    public static String status() {
        FtcDashboard dashboard = FtcDashboard.getInstance();
        if (dashboard == null) {
            return "not running";
        }
        return dashboard.isEnabled() ? "enabled" : "disabled";
    }

    public static boolean setEnabled(boolean enabled) {
        FtcDashboard dashboard = FtcDashboard.getInstance();
        if (dashboard == null) {
            RobotLog.ee(TAG, "FTC Dashboard is not running");
            return false;
        }
        if (dashboard.isEnabled() == enabled) {
            return true;
        }

        try {
            Method method = FtcDashboard.class.getDeclaredMethod(enabled ? "enable" : "disable");
            method.setAccessible(true);
            method.invoke(dashboard);
        } catch (ReflectiveOperationException e) {
            RobotLog.ee(TAG, e, "Unable to change FTC Dashboard enabled state");
            return false;
        }

        return dashboard.isEnabled() == enabled;
    }
}
