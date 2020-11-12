package org.firstinspires.ftc.teamcode.Opmode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Hardware.HardwareList;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.WheelMovement;
import org.firstinspires.ftc.teamcode.Vision.BlueFrameGrabber;
import org.firstinspires.ftc.teamcode.Vision.RedFrameGrabber;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@Autonomous(name = "BlueFoundation")
public class BlueFoundation extends WheelMovement {
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
        grabFoundation(true);
        delay(1);

        arcLeft(1, 1500);
        //moveBackward(1, 200, );
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