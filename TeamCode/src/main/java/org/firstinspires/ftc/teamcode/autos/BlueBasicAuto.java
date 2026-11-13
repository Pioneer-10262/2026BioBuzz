package org.firstinspires.ftc.teamcode.autos;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedro.Constants;

public class BlueBasicAuto extends OpMode {
    private Follower follower;
    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(81.6952, 8.3885, 90);
    private final Pose path1 = poseFactory.of(128.9993, 33.869, 180);
    private final Pose path1Control1 = poseFactory.of(83.1654, 35.0422, 0);

    public Path path1() {
        return Paths.curve(start, path1Control1, path1).linear(start, path1);
    }

    @Override
    public void init() {
        follower = Constants.create(hardwareMap);
        follower.setPose(start);
    }

    @Override
    public void start() {
        follower.follow(path1());
    }


    @Override
    public void loop() {
        follower.update();
    }
}
