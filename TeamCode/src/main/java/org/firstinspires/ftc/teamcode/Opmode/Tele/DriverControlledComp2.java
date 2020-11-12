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
@TeleOp(name = "DriverControlled", group = "TeleOp")

public class DriverControlledComp2 extends OpMode {
    // Define robot
    HardwareList robot = new HardwareList();

    //Constants
    final int EXTRA_LIFT = 100;
    final int BLOCK_HEIGHT = 200;
    // blockLevel will be increased in encoder values
    int blockLevel = 0;
    boolean increment = false;
    boolean decrement = false;

    //Run once on init
    @Override
    public void init() {
        robot.init(hardwareMap);
        telemetry.addData("Status", "Initialized");
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

        if(largestVariable > 1) {
            // Sets range from -1 to 1
            frontLeft /= largestVariable;
            frontRight /= largestVariable;
            backLeft /= largestVariable;
            backRight /= largestVariable;
        }

        if(gamepad1.left_trigger <= 0.8){
            frontLeft *= 1 - gamepad1.left_trigger;
            frontRight *= 1 - gamepad1.left_trigger;
            backLeft *= 1 - gamepad1.left_trigger;
            backRight *= 1 - gamepad1.left_trigger;
        } else if(gamepad1.left_trigger > 0.8){
            frontLeft *= 0.2;
            frontRight *= 0.2;
            backLeft *= 0.2;
            backRight *= 0.2;
        }


        if (gamepad1.y == true){
            //Lower foundation grabbers
            robot.rightFrame.setPosition(0.3);
            robot.leftFrame.setPosition(0.9);
        } else {
            //Raise foundation grabbers
            robot.rightFrame.setPosition(0.85);
            robot.leftFrame.setPosition(0.3);
        }

        // Sets motor powers
        robot.frontLeft.setPower(frontLeft);
        robot.frontRight.setPower(-frontRight);
        robot.backLeft.setPower(backLeft);
        robot.backRight.setPower(backRight);

        //Control claws in case of lift failure
        if (gamepad1.dpad_left == true){
            robot.blueGrabber.setPosition(0.8);
        } else{
            robot.blueGrabber.setPosition(0.5);
        }
        if (gamepad1.dpad_right == true){
            robot.redGrabber.setPosition(0.3);
        } else {
            robot.redGrabber.setPosition(0.7);
        }

        if (gamepad1.dpad_up){
            robot.blueClaw.setPosition(0.43);
            robot.redClaw.setPosition(0.35);
        }
            //Grabber controls
        if (gamepad1.left_bumper == true){
            robot.redClaw.setPosition(0.57);
            robot.blueClaw.setPosition(0.27);
        } else{
            robot.blueClaw.setPosition(0.75);
            robot.redClaw.setPosition(0);
        }

        //Intake
        if (gamepad1.right_trigger >= 0.1){
            robot.intake.setPower(-gamepad1.right_trigger);
        } else if(gamepad1.right_bumper == true){
            robot.intake.setPower(1);
        } else {
            robot.intake.setPower(0);
        }

        /////////////////////////////////////////////////////////////////////

        //************************ GAMEPAD 2 DRIVE ************************//

        /////////////////////////////////////////////////////////////////////
        //Manual Slide Control
        if(gamepad2.left_stick_y > 0 && robot.limitSwitch.getState() == true){
            if(robot.verticalSlides.getCurrentPosition() > 1000){
                robot.verticalSlides.setPower(gamepad2.left_stick_y);
            } else {
                robot.verticalSlides.setPower(gamepad2.left_stick_y / 4);
            }
        } else if(gamepad2.left_stick_y < 0){
            robot.verticalSlides.setPower(gamepad2.left_stick_y);
        } else {
            robot.verticalSlides.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.verticalSlides.setPower(0);
        }

        /* Set up stacking automation*/
        //Increments for increasing block levels
        if(gamepad2.b == true && increment == false){
            blockLevel += 1;// Determine encoder value
            increment = true;
        } else if (gamepad2.b == false){
            increment = false;
        }
        if(gamepad2.x == true && decrement == false){
            blockLevel -= 1;// Determine encoder value
        } else if (gamepad2.x == false){
            decrement = false;
        }
        if(gamepad2.y == true){
            blockLevel = 0;
        }
        if(gamepad2.a == true){
            resetVerticalSlides();
        }

        if(gamepad2.dpad_down){
            robot.backGrabber.setPosition(1);
        }
/*
        if(gamepad2.right_stick_y > 0){
            if (robot.verticalSlides.getCurrentPosition() < blockLevel){
                robot.verticalSlides.setPower(gamepad2.right_stick_y);
            } else {
                robot.verticalSlides.setPower(0);// Enough to hold the verticalSlides without moving them
            }
        }

*/
        // Grab the stone
        if(gamepad2.right_bumper == true){
            robot.backGrabber.setPosition(0.43);
        } else if(gamepad2.right_trigger > 0){
            robot.backGrabber.setPosition(0.7);
       }

        // Horizontal Slides control
        int liftedPosition = robot.verticalSlides.getCurrentPosition() + 0; // Determine value
        if (gamepad2.left_bumper == true){
            robot.horizontalSlides.setPosition(0.15); // Determine value
        } else if (gamepad2.left_trigger > 0){
            //TEMPORARY VALUE FOR TESTING THE INTAKE?
            robot.horizontalSlides.setPosition(0.8);
            /*
            //Set up to lift the verticalSlides and move back
            if(robot.verticalSlides.getCurrentPosition() < liftedPosition){
                robot.verticalSlides.setPower(0.5);
            } else {
                robot.verticalSlides.setPower(0.2); // Just enough to hold the verticalSlides
                robot.horizontalSlides.setPosition(0.25); // Determine value
            }
            */
        }

        //Output encoder values
        telemetry.addData("Limit", robot.limitSwitch.getState());
        telemetry.addData("Block Level", blockLevel);
        telemetry.addData("Slide Height", robot.verticalSlides.getCurrentPosition());
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
    public void resetVerticalSlides(){
        robot.verticalSlides.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.verticalSlides.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

}
