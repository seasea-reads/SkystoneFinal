package org.firstinspires.ftc.teamcode.Opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Vision.RedFrameGrabber;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@TeleOp(name="VisionTest")
public class VisionTest extends LinearOpMode{
    OpenCvCamera phoneCam;

    private RedFrameGrabber redFrameGrabber;

    private enum POSITION {
        LEFT,
        CENTER,
        RIGHT,
    }

    POSITION position;



    @Override
    public void runOpMode() {

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = new OpenCvInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);

        // start the vision system
        phoneCam.openCameraDevice();
        redFrameGrabber = new RedFrameGrabber();
        phoneCam.setPipeline(redFrameGrabber);
        phoneCam.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);

        while (!isStarted()) {

            // Left Guide
            if (gamepad1.dpad_right == true) {
                redFrameGrabber.leftGuide += 0.001;
            } else if (gamepad1.dpad_left == true) {
                redFrameGrabber.leftGuide -= 0.001;
            }

            // Mask
            if (gamepad1.dpad_down == true) {
                redFrameGrabber.mask += 0.001;
            } else if (gamepad1.dpad_up == true) {
                redFrameGrabber.mask -= 0.001;
            }

            // Threshold
            if (gamepad2.y) {
                redFrameGrabber.threshold += 0.001;
            } else if (gamepad2.a) {
                redFrameGrabber.threshold -= 0.001;
            }

            if (redFrameGrabber.position == "LEFT") {
                position = POSITION.LEFT;
            } else if (redFrameGrabber.position == "MIDDLE") {
                position = POSITION.CENTER;
            } else {
                position = POSITION.RIGHT;
            }
            telemetry.addData("Position", position);
            telemetry.addData("Threshold", redFrameGrabber.threshold);
            telemetry.addData("Rect width", redFrameGrabber.rectWidth);

            telemetry.update();
        }

        waitForStart();

        while(opModeIsActive()){
            telemetry.addData("Position", position);
            telemetry.addData("Threshold", redFrameGrabber.threshold);
        }
    }
}
