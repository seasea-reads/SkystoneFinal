package org.firstinspires.ftc.teamcode.Opmode.Tele;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.lang.Math;

import org.firstinspires.ftc.teamcode.Hardware.HardwareList;

/*
 * Created by C.C.
 */
@Disabled
@TeleOp(name = "BadDriverControlled", group = "TeleOp")

public class DriverControlled extends OpMode {
    // Define robot
    HardwareList robot = new HardwareList();

    //Constants

    //Run once on init
    @Override
    public void init() {
        robot.init(hardwareMap);
        telemetry.addData("Status", "Initialized");
        robot.leftFrame.setPosition(0.3);
        robot.rightFrame.setPosition(0.85);
    }

    //Run once on start()
    @Override
    public void start() {
        resetEncoders();
    }

    // Loops on start
    @Override
    public void loop() {
        /////////////////////////////////////////////////////////////////////

        //************************ GAMEPAD 1 DRIVE ************************//

        /////////////////////////////////////////////////////////////////////

        //Basic Driving Controls
        double LeftY = -gamepad1.left_stick_y;
        double LeftX = gamepad1.left_stick_x;
        double RightX = gamepad1.right_stick_x;

        // Calculates direction each word should turn(range -3 to 3)
        double frontLeft = LeftY + LeftX + RightX;
        double frontRight = LeftY - LeftX - RightX;
        double backLeft = LeftY - LeftX + RightX;
        double backRight = LeftY + LeftX - RightX;

        // Calculates the motor which requires the most power
        double largestVariable = Math.abs(frontLeft);

        if (largestVariable < Math.abs(frontRight))
            largestVariable = Math.abs(frontRight);

        if (largestVariable < Math.abs(backLeft))
            largestVariable = Math.abs(backLeft);

        if (largestVariable < Math.abs(backRight))
            largestVariable = Math.abs(backRight);

        // Sets range from -1 to 1
        frontLeft /= largestVariable;
        frontRight /= largestVariable;
        backLeft /= largestVariable;
        backRight /= largestVariable;

        if(gamepad1.right_trigger > 0){
            frontLeft /= 2;
            frontRight /= 2;
            backLeft /= 2;
            backRight /= 2;
        } else if (gamepad1.left_trigger > 0){
            frontLeft /= 4;
            frontRight /= 4;
            backLeft /= 4;
            backRight /= 4;
        }


        if (gamepad1.right_bumper == true){
            robot.rightFrame.setPosition(0.25);
        } else {
            robot.rightFrame.setPosition(0.85);
        }
        if (gamepad1.left_bumper == true){
            robot.leftFrame.setPosition(0.8);
        } else {
            robot.leftFrame.setPosition(0.3);
        }

        // Sets motor powers
        robot.frontLeft.setPower(frontLeft);
        robot.frontRight.setPower(-frontRight);
        robot.backLeft.setPower(backLeft);
        robot.backRight.setPower(backRight);


        /////////////////////////////////////////////////////////////////////

        //************************ GAMEPAD 2 DRIVE ************************//

        /////////////////////////////////////////////////////////////////////

        //Sets Claw positions
        if(gamepad2.right_bumper == true){
            robot.redGrabber.setPosition(0.75);
        } else{
            robot.redGrabber.setPosition(0.35);
        }

        if(gamepad2.right_trigger > 0){
            robot.redClaw.setPosition(0.27);
        } else if(gamepad2.a){
            robot.redClaw.setPosition(0.47);
        } else if(gamepad2.y){
            robot.redClaw.setPosition(0.6);
        } else {
            robot.redClaw.setPosition(0.7);
        }

        if(gamepad2.left_bumper == true){
            robot.blueGrabber.setPosition(0.25);
        } else{
            robot.blueGrabber.setPosition(0.5);
        }

        if(gamepad2.left_trigger > 0){
            robot.blueClaw.setPosition(0.77);
        } else if(gamepad2.dpad_down){
            robot.blueClaw.setPosition(0.7);
        } else if(gamepad2.dpad_up){
            robot.blueClaw.setPosition(0.57);
        } else {
            robot.blueClaw.setPosition(0.47);
        }

        //Output encoder values
        telemetry.addData("Front Left", robot.frontLeft.getCurrentPosition());
        telemetry.addData("Front Right", robot.frontRight.getCurrentPosition());
        telemetry.addData("Back Left", robot.backLeft.getCurrentPosition());
        telemetry.addData("Back Right", robot.backRight.getCurrentPosition());
    }

    // Runs once - on stop
    @Override
    public void stop(){
    }
    public void resetEncoders() {
        robot.backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

}
