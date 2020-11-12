package org.firstinspires.ftc.teamcode.Opmode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.WheelMovement;
import org.firstinspires.ftc.teamcode.Vision.RedFrameGrabber;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "RedStone")
public class RedStone extends WheelMovement {
    OpenCvCamera phoneCam;

    private RedFrameGrabber redFrameGrabber;
    //private ElapsedTime  runtime = new ElapsedTime();

    private enum POSITION {
        LEFT,
        CENTER,
        RIGHT,
    }

    RedStone.POSITION position;

    @Override

    public void runOpMode()  throws InterruptedException {
        //initialize everything
        super.runOpMode();

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = new OpenCvInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);

        elapsedTime = new ElapsedTime();
        // start the vision system
        phoneCam.openCameraDevice();
        redFrameGrabber = new RedFrameGrabber();
        phoneCam.setPipeline(redFrameGrabber);
        phoneCam.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);

        while (!isStarted()) {
            //set servo positions
            claw(false);
            grabber(false);
            blueGrabber.setPosition(0.2);
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

            //Determines Skystone Position
            if (redFrameGrabber.position == "LEFT") {
                position = RedStone.POSITION.LEFT;
            } else if (redFrameGrabber.position == "MIDDLE") {
                position = RedStone.POSITION.CENTER;
            } else {
                position = RedStone.POSITION.RIGHT;
            }

            //Displays results
            telemetry.addData("Position", position);
            telemetry.addData("Threshold", redFrameGrabber.threshold);
            telemetry.addData("Rect width", redFrameGrabber.rectWidth);

            telemetry.update();
        }

        waitForStart();

        resetEncoders();

        phoneCam.stopStreaming();

        // Move to and grab the first skystone
        moveRight(0.5, 400, 0);

        // Move over to stones
        redGrabber.setPosition(0.4);
        redClaw.setPosition(0.57);

        if(position == RedStone.POSITION.CENTER){
            //determined based on starting position
            moveForward(0.5, 350, 0);
        } else if (position == RedStone.POSITION.RIGHT){
            moveBackward(0.75, 275, 0);
        }

        moveRight(0.5, 1200, 0);
        delay(0.15);

        grabStone();
        blueClaw.setPosition(0.45);
        delay(0.15);


        //reset Encoders to make further positioning easier
        //resetEncoders();

        //Move to foundation and place stone
        //move right, then back, then left
        moveLeft(0.5, 242, 0);
        delay(0.05);
        if(position == RedStone.POSITION.LEFT){
            moveBackward(1, 5000, 0);
        } else if(position == RedStone.POSITION.CENTER){
            moveBackward(1, 6500, 0);
        } else {
            moveBackward(1, 4300, 0);
        }
        //was 4668
        delay(0.05);

        //resetEncoders();
        moveRight(0.75, 484, 0);

        placeStone();

        //Move to get the next stone
        moveLeft(0.75, 400, 0);
        claw(true);
        delay(0.1);
        if(position == RedStone.POSITION.RIGHT){
            moveForward(1, 5500, 0);
        } else if(position == RedStone.POSITION.CENTER){
            moveForward(1, 4575, 0);
        } else {
            moveForward(1, 6150, 0);
        }

        claw(false);
        delay(0.15);

        redGrabber.setPosition(0.38);
        redClaw.setPosition(0.57);

        moveRight(0.75, 425, 0);
        delay(0.1);

        // Grab second skystone
        grabStone();
        delay(0.15);

        //Move to foundation and place second stone
        //move right, then back, then left
        if(position == RedStone.POSITION.CENTER){
            moveLeft(0.75, 377, 0);
        } else {
            moveLeft(0.75, 357, 0);
        }
        delay(0.05);

        //Move to foundation to place
        //different distance depending on stone position
        if(position == RedStone.POSITION.RIGHT){
            moveBackward(1, 4642, 0);
            delay(0.05);
        } else if(position == RedStone.POSITION.CENTER){
            moveBackward(1, 3700, 0);
            delay(0.05);
        } else {
            moveBackward(1, 5642, 0);
            delay(0.05);
        }

        moveRight(1, 500, 0);
        placeStone();

        resetEncoders();

        //Turn Left
        frontRight.setPower(-1);
        frontLeft.setPower(-1);
        backLeft.setPower(-1);
        backRight.setPower(1);
        delay(0.25);
        stopRobot();
        delay(0.01);
        //Arc back right and grab the foundation
        arcRightBack(1, 750);
        grabFoundation(true);
        delay(0.4);

        //Arc Right
        frontRight.setPower(0);
        frontLeft.setPower(1);
        backLeft.setPower(1);
        backRight.setPower(0);
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
        blueClaw.setPosition(0.45);
        stopRobot();
        delay(0.01);

        //Move right
        frontRight.setPower(1);
        frontLeft.setPower(1);
        backLeft.setPower(-1);
        backRight.setPower(1);
        if(position == RedStone.POSITION.CENTER){
            delay(0.45);
        } else if(position == RedStone.POSITION.RIGHT){
            delay(0.5);
        } else{
            delay(0.65);
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

        while(opModeIsActive()){
            if(isStopRequested() == true)
                requestOpModeStop();
        }
    }
    public void claw(boolean close){
        if (close == true){
            redClaw.setPosition(0.35);
        } else {
            redClaw.setPosition(0.7);
        }
    }
    public void grabber(boolean lower){
        if (lower == true){
            redGrabber.setPosition(0.3);
        } else {
            redGrabber.setPosition(0.7);
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
        claw(false);
        delay(0.05);
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