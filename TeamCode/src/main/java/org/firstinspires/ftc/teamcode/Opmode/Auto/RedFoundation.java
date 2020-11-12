package org.firstinspires.ftc.teamcode.Opmode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Hardware.Subsystems.WheelMovement;

@Autonomous(name = "RedFoundation")
public class RedFoundation extends WheelMovement {
    @Override
    public void runOpMode() throws InterruptedException {
        //initialize everything
        super.runOpMode();


        while (!isStarted()) {
            //set servo positions
            claw(false);
            grabber(false);
            redGrabber.setPosition(0.75);
        }

        waitForStart();

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
        arcRight(1, 4);

        //Move back
        frontRight.setPower(1);
        frontLeft.setPower(-1);
        backLeft.setPower(-1);
        backRight.setPower(-1);
        delay(0.75);
        grabFoundation(false);
        claw(true);
        blueClaw.setPosition(0.45);
        stopRobot();
        delay(0.01);

        //Move right
        frontRight.setPower(1);
        frontLeft.setPower(1);
        backLeft.setPower(-1);
        backRight.setPower(1);
        delay(0.2);
        stopRobot();
        delay(0.01);

        //Move forward
        frontRight.setPower(-1);
        frontLeft.setPower(1);
        backLeft.setPower(1);
        backRight.setPower(1);
        delay(0.8);
        stopRobot();
        //moveBackward(1, 200, );
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