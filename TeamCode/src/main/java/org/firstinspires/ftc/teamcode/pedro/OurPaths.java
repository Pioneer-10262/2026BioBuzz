package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.api.Paths;
public class OurPaths {

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(84.1608, 133.3058, 90);
    private final Pose path1 = poseFactory.of(15.7476, 106.5139, 180);
    private final Pose path1Control1 = poseFactory.of(84.5383, 104.6462, 0);

    public Path path1() {
        return Paths.curve(start, path1Control1, path1).linear(start, path1);
    }
}
