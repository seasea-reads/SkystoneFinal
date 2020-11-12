package org.firstinspires.ftc.teamcode.Opmode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Hardware.HardwareList;
import org.firstinspires.ftc.teamcode.Vision.RedFrameGrabber;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "RedPark")
public class RedPark extends LinearOpMode{
    OpenCvCamera phoneCam;
    HardwareList robot = new HardwareList();

    private RedFrameGrabber redFrameGrabber;
    public ElapsedTime elapsedTime;
    //private ElapsedTime  runtime = new ElapsedTime();

    private enum POSITION {
        LEFT,
        CENTER,
        RIGHT,
    }


    @Override

    public void runOpMode() {
        robot.init(hardwareMap);
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
            robot.blueGrabber.setPosition(0.25);
        }

        waitForStart();

        resetEncoders();

        phoneCam.stopStreaming();
        delay(1);


        robot.blueClaw.setPosition(0.45);
        robot.redClaw.setPosition(0.35);

        setPosition(1800, -1800, -1800, -1800);

        while(opModeIsActive()){
            if(isStopRequested() == true)
                requestOpModeStop();
        }
    }
    public void delay(double seconds) {
        elapsedTime.reset();
        while (elapsedTime.time() < seconds && opModeIsActive()) ;
    }
    public void claw(boolean lower){
        if (lower == true){
            robot.redClaw.setPosition(0.27);
        } else {
            robot.redClaw.setPosition(0.7);
        }
    }
    public void grabber(boolean close){
        if (close == true){
            robot.redGrabber.setPosition(0.35);
        } else {
            robot.redGrabber.setPosition(0.75);
        }
    }
    public void placeStone(){
        claw(true);
        delay(0.5);
        grabber(false);
        delay(0.5);
        claw(false);
        delay(0.5);
        grabber(true);
    }
    public void grabStone(){
        claw(true);
        delay(0.5);
        grabber(true);
        delay(0.5);
        claw(false);
        delay(0.5);
    }
    public void setPosition(int bl, int br, int fl, int fr) {
        robot.backRight.setPower(0.75);
        robot.backLeft.setPower(0.75);
        robot.frontRight.setPower(0.75);
        robot.frontLeft.setPower(0.75);
        robot.backRight.setTargetPosition(br);
        robot.backLeft.setTargetPosition(bl);
        robot.frontRight.setTargetPosition(fr);
        robot.frontLeft.setTargetPosition(fl);
    }
    public void resetEncoders() {
        robot.backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setPosition(0, 0, 0, 0);
        robot.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        delay(0.2);
    }
}