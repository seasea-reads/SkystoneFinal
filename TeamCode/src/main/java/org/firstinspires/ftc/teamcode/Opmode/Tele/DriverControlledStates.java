package org.firstinspires.ftc.teamcode.Opmode.Tele;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Hardware.HardwareList;

/*
 * Created by C.C.
 */
@TeleOp(name = "DriverControlled", group = "TeleOp")

public class DriverControlledStates extends OpMode {
    // Define robot
    HardwareList robot = new HardwareList();
    ElapsedTime elapsedTime;

    //Constants
    final int EXTRA_LIFT = 100;
    final int BLOCK_HEIGHT = 200;
    // blockLevel will be increased in encoder values
    int blockLevel = 0;
    boolean increment = false;
    boolean isPressed = false;
    boolean isPressed2 = false;
    boolean decrement = false;
    boolean frontGrabber = false;
    boolean autoRetract = false;
    int slideCurrent;
    int slidePos;

    //Run once on init
    @Override
    public void init() {
        robot.init(hardwareMap);
        telemetry.addData("Status", "Initialized");
        resetVerticalSlides();
    }

    //Run once on start()
    @Override
    public void start() {
        resetEncoders();
        elapsedTime = new ElapsedTime();
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
            frontLeft *= 0.4;
            frontRight *= 0.4;
            backLeft *= 0.4;
            backRight *= 0.4;
        }


        if (gamepad1.y == true){
            //Lower foundation grabbers
            robot.rightFrame.setPosition(1); //was 0.3
            robot.leftFrame.setPosition(0);
        } else {
            //Raise foundation grabbers
            robot.rightFrame.setPosition(0); // was 0.85
            robot.leftFrame.setPosition(0.9);
        }

        // Sets motor powers
        robot.frontLeft.setPower(frontLeft);
        robot.frontRight.setPower(-frontRight);
        robot.backLeft.setPower(backLeft);
        robot.backRight.setPower(backRight);

        //Control claws in case of lift failure
        if (gamepad1.dpad_left){
            robot.blueGrabber.setPosition(0.5);
        } else{
            robot.blueGrabber.setPosition(0.2);
        }
        if (gamepad1.dpad_right){
            robot.redGrabber.setPosition(0.3);
        } else {
            robot.redGrabber.setPosition(0.7);
        }

        if (gamepad1.dpad_up){
            robot.blueClaw.setPosition(0.43);
            robot.redClaw.setPosition(0.35);
        }
            //Grabber controls
        if (gamepad1.left_bumper){
            robot.redClaw.setPosition(0.57);
            robot.blueClaw.setPosition(0.27);
        } else{
            robot.blueClaw.setPosition(0.75);
            robot.redClaw.setPosition(0);
        }

        //Intake
        if (gamepad1.right_trigger >= 0.05){
            if(gamepad1.right_trigger >= 0.85){
                robot.intake.setPower(-0.85);
            } else {
                robot.intake.setPower(-gamepad1.right_trigger);
            }
        } else if(gamepad1.right_bumper == true){
            robot.intake.setPower(0.5);
        } else {
            robot.intake.setPower(0);
        }

        /////////////////////////////////////////////////////////////////////

        //************************ GAMEPAD 2 DRIVE ************************//

        /////////////////////////////////////////////////////////////////////
        //Manual Slide Control
        if(!robot.limitSwitch.getState()){
            resetVerticalSlides();
        }
        if(gamepad2.left_stick_y > 0 && robot.limitSwitch.getState()){
            if(robot.verticalSlides.getCurrentPosition() > 100){
                if(gamepad2.right_trigger >= 0.1){
                    robot.verticalSlides.setPower(gamepad2.left_stick_y / 2);
                } else {
                    robot.verticalSlides.setPower(gamepad2.left_stick_y);
                }
            } else {
                if(gamepad2.left_stick_button){
                    robot.verticalSlides.setPower(gamepad2.left_stick_y / 4);
                } else {
                    robot.verticalSlides.setPower(gamepad2.left_stick_y / 2);
                }
            }
        } else if(gamepad2.left_stick_y < 0){
            robot.verticalSlides.setPower(gamepad2.left_stick_y);
        } else if(!robot.limitSwitch.getState()){
            robot.verticalSlides.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.verticalSlides.setPower(0);
        } else if(!autoRetract){
            robot.verticalSlides.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.verticalSlides.setPower(-0.01);
        }
/*
        /* Set up stacking automation/
        //Increments for increasing block levels
        if(gamepad2.b == true && increment == false){
            blockLevel += 1;// Determine encoder value -> approximately 200 encoder ticks
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
        */

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

        if(gamepad2.a){
            robot.backGrabber.setPosition(0.35);
            robot.frontGrabber.setPosition(0.25);
        } else if(gamepad2.x){
            robot.backGrabber.setPosition(0.7);
            robot.frontGrabber.setPosition(0.6);
        } else if (gamepad2.b){
            robot.backGrabber.setPosition(0.4);
            robot.frontGrabber.setPosition(0.75);
        } else if(gamepad2.y) {
            robot.frontGrabber.setPosition(0.75);
            robot.horizontalSlides.setPosition(0);
            robot.backGrabber.setPosition(1);
        }

        if(gamepad2.right_bumper == true){
            autoRetract = true;
            slidePos = robot.verticalSlides.getCurrentPosition();
            slidePos += 100;
            elapsedTime.reset();
        }

        //Automatic Retract
        /*
        if(autoRetract){
            slideCurrent = robot.verticalSlides.getCurrentPosition();
            if(slideCurrent < slidePos && elapsedTime.time() > 0.2){
                robot.verticalSlides.setPower(-1); //Lift Vertical Slides
                frontGrabber = true;
            } else{
                frontGrabber = false;
                robot.verticalSlides.setPower(-0.01);
                robot.horizontalSlides.setPosition(0.75); //Retract Horizontal Slides slightly more than normal
                robot.backGrabber.setPosition(0.35); //Set up for grabbing stones
                robot.frontGrabber.setPosition(0.75);
                if(elapsedTime.time() > 1){
                    frontGrabber = true;
                    robot.verticalSlides.setPower(1); // Once all else is done, drop slides
                }
            }
            telemetry.addData("Lifted", frontGrabber);
            telemetry.addData("Speed", robot.verticalSlides.getPower());
            if(!robot.limitSwitch.getState()){ // Stop retracting once closed
                autoRetract = false;
                robot.horizontalSlides.setPosition(0.68); // Go to normal slide extension
            }
        }*/

        if(autoRetract){
            if(robot.verticalSlides.getCurrentPosition() > 500){
                robot.verticalSlides.setPower(1); //Lift Vertical Slides
                frontGrabber = true;
            } else{
                robot.verticalSlides.setPower(0.5); // Once all else is done, drop slides
            }
            telemetry.addData("Lifted", frontGrabber);
            telemetry.addData("Speed", robot.verticalSlides.getPower());
            if(!robot.limitSwitch.getState()){ // Stop retracting once closed
                autoRetract = false;
                robot.verticalSlides.setPower(0);
                //robot.horizontalSlides.setPosition(0.68); // Go to normal slide extension
            }
        }

        // Horizontal Slides control
        int liftedPosition = robot.verticalSlides.getCurrentPosition() + 0;
        if (gamepad2.left_bumper == true){
            robot.horizontalSlides.setPosition(0.11); // Open
        } else if (gamepad2.left_trigger > 0){
            robot.horizontalSlides.setPosition(0.68); // Close
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
