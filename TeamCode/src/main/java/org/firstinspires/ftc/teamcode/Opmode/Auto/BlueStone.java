package org.firstinspires.ftc.teamcode.Opmode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Hardware.Subsystems.WheelMovement;
import org.firstinspires.ftc.teamcode.Vision.BlueFrameGrabber;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;


@Autonomous(name = "BlueStone")
public class BlueStone extends WheelMovement {
    OpenCvCamera phoneCam;

    private BlueFrameGrabber blueFrameGrabber;

    private enum POSITION {
        LEFT,
        CENTER,
        RIGHT,
    }

    BlueStone.POSITION position;

    @Override

    public void runOpMode() throws InterruptedException {
        //initialize everything
        super.runOpMode();

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = new OpenCvInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);

        // start the vision system
        phoneCam.openCameraDevice();
        blueFrameGrabber = new BlueFrameGrabber();
        phoneCam.setPipeline(blueFrameGrabber);
        phoneCam.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);

        while (!isStarted()) {
            //set servo positions
            claw(false);
            grabber(false);
            redGrabber.setPosition(0.75);
            // Left Guide
            if (gamepad1.dpad_right == true) {
                blueFrameGrabber.leftGuide += 0.001;
            } else if (gamepad1.dpad_left == true) {
                blueFrameGrabber.leftGuide -= 0.001;
            }

            // Mask
            if (gamepad1.dpad_down == true) {
                blueFrameGrabber.mask += 0.001;
            } else if (gamepad1.dpad_up == true) {
                blueFrameGrabber.mask -= 0.001;
            }

            // Threshold
            if (gamepad2.y) {
                blueFrameGrabber.threshold += 0.001;
            } else if (gamepad2.a) {
                blueFrameGrabber.threshold -= 0.001;
            }

            //Determines Skystone Position
            if (blueFrameGrabber.position == "LEFT") {
                position = BlueStone.POSITION.LEFT;
            } else if (blueFrameGrabber.position == "MIDDLE") {
                position = BlueStone.POSITION.CENTER;
            } else {
                position = BlueStone.POSITION.RIGHT;
            }

            //Displays results
            telemetry.addData("Position", position);
            telemetry.addData("Threshold", blueFrameGrabber.threshold);
            telemetry.addData("Rect width", blueFrameGrabber.rectWidth);

            telemetry.update();
        }

        waitForStart();

        resetEncoders();

        phoneCam.stopStreaming();

        // Move to and grab the first skystone
        moveLeft(0.5, 400, 0);

        // Move over to stones
        blueGrabber.setPosition(0.45);
        blueClaw.setPosition(0.2);

        if(position == BlueStone.POSITION.CENTER){
            //determined based on starting position
            moveForward(0.5, 350, 0);
        } else if (position == BlueStone.POSITION.LEFT){
            moveBackward(0.75, 245, 0);
        }
        if(position == BlueStone.POSITION.RIGHT){
            frontRight.setPower(-1);
            backRight.setPower(1);
            frontLeft.setPower(1);
            backLeft.setPower(1);
            delay(0.05);
            stopRobot();
        }

        moveLeft(0.5, 1250, 0);
        delay(0.15);


        grabStone();
        redClaw.setPosition(0.35);
        delay(0.15);

        //reset Encoders to make further positioning easier
        //resetEncoders();

        //Move to foundation and place stone
        //move right, then back, then left
        moveRight(0.5, 382, 0);
        delay(0.05);
        if(position == BlueStone.POSITION.LEFT){
            moveBackward(1, 4000, 0);
        } else if(position == BlueStone.POSITION.CENTER){
            moveBackward(1, 5800, 0);
        }else {
            moveBackward(1, 4800, 0);
        }
        //was 4668
        delay(0.05);

        //resetEncoders();
        moveLeft(0.75, 414, 0);

        placeStone();

        //Move to get the next stone
        moveRight(0.75, 240, 0);
        claw(true);
        delay(0.1);
        if(position == BlueStone.POSITION.RIGHT){
            moveForward(1, 6650, 0);
        } else if(position == BlueStone.POSITION.CENTER){
            moveForward(1, 4380, 0);
        } else {
            moveForward(1, 5580, 0);
        }

        claw(false);
        //blueClaw.setPosition(0.77);
        delay(0.15);

        blueGrabber.setPosition(0.4);
        blueClaw.setPosition(0.2);

        moveLeft(0.75, 235, 0);
        delay(0.1);

        // Grab second skystone
        grabStone();
        delay(0.15);

        //Move to foundation and place second stone
        //move right, then back, then left
        moveRight(0.75, 387, 0);
        delay(0.05);

        //different distance depending on stone position
        if(position == BlueStone.POSITION.LEFT){
            moveBackward(1, 4642, 0); // Drop for left??
            delay(0.05);
        } else if (position == BlueStone.POSITION.RIGHT){
            moveBackward(1, 5542, 0);
            delay(0.05);
        } else{
            moveBackward(1, 3642, 0);
            delay(0.05);
        }
        moveLeft(1, 430, 0);
        placeStone();

        resetEncoders();

        //Turn Right
        frontRight.setPower(1);
        frontLeft.setPower(1);
        backLeft.setPower(1);
        backRight.setPower(-1);
        delay(0.25);
        stopRobot();
        delay(0.01);
        //Arc back right and grab the foundation
        arcLeftBack(1, 780);
        grabFoundation(true);
        delay(0.4);

        //Arc Right
        frontRight.setPower(-1);
        frontLeft.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(1);
        delay(1.8);
        stopRobot();
        delay(0.01);

        //Move back
        frontRight.setPower(1);
        frontLeft.setPower(-1);
        backLeft.setPower(-1);
        backRight.setPower(-1);
        grabFoundation(false);
        delay(1.25);
        claw(true);
        redClaw.setPosition(0.35);
        stopRobot();
        delay(0.01);

        //Move right
        frontRight.setPower(-1);
        frontLeft.setPower(-1);
        backLeft.setPower(1);
        backRight.setPower(-1);
        if(position != BlueStone.POSITION.LEFT){
            delay(0.45);
        }else {
            delay(0.6);
        }
        stopRobot();
        delay(0.01);

        //Move forward
        frontRight.setPower(-1);
        frontLeft.setPower(1);
        backLeft.setPower(1);
        backRight.setPower(1);
        delay(0.8);
        stopRobot();


        //Output encoder values
        telemetry.addData("Front Left", frontLeft.getCurrentPosition());
        telemetry.addData("Front Right", frontRight.getCurrentPosition());
        telemetry.addData("Back Left", backLeft.getCurrentPosition());
        telemetry.addData("Back Right", backRight.getCurrentPosition());
        telemetry.update();


        //Output encoder values
        telemetry.addData("Front Left", frontLeft.getCurrentPosition());
        telemetry.addData("Front Right", frontRight.getCurrentPosition());
        telemetry.addData("Back Left", backLeft.getCurrentPosition());
        telemetry.addData("Back Right", backRight.getCurrentPosition());
        telemetry.update();

        while(opModeIsActive()){
            if(isStopRequested() == true)
                requestOpModeStop();
        }
    }
    public void claw(boolean close){
        if (close == true){
            blueClaw.setPosition(0.45);
        } else {
            blueClaw.setPosition(0.27);
        }
    }
    public void grabber(boolean lower){
        if (lower == true){
            blueGrabber.setPosition(0.5);
        } else {
            blueGrabber.setPosition(0.2);
        }
    }
    public void grabFoundation(boolean lower){
        if (lower == true) {
            //Lower grabbers
            rightFrame.setPosition(1);
            leftFrame.setPosition(0);
        } else {
            //Raise grabbers
            rightFrame.setPosition(0);
            leftFrame.setPosition(0.9);
        }
    }
    public void placeStone(){
        grabber(true);
        delay(0.15);
        claw(false);
        delay(0.25);
        grabber(false);
        delay(0.15);
        claw(true);
    }
    public void grabStone(){
        grabber(true);
        delay(0.3);
        claw(true);
        delay(0.3);
        grabber(false);
        delay(0.25);
    }

}