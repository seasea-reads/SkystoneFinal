package org.firstinspires.ftc.teamcode.Hardware.Subsystems;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Hardware.HardwareList;

import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Mechanical Paradox on 12/19/2019.
 */

@Autonomous(name = "WheelMovement")
//@Disabled
public class WheelMovement extends LinearOpMode {
    HardwareList robot = new HardwareList();
    BNO055IMU imu;
    Orientation angles;
    double errorFactor = 45;

    public ElapsedTime elapsedTime;

    public void moveForward(double speed, int distance, double targetAngle){
        //Declare necessary values
        double angleError;

        double FLSpeed;
        double FRSpeed;
        double BLSpeed;
        double BRSpeed;

        //Rather than constantly resetting the encoders, we can add the new distance
        double newDistance = distance + encoderAverage();

        //resetEncoders();

        while(encoderAverage() < newDistance && opModeIsActive()){
            // Determine angle and error
            telemetry.addData("Current Angle", angles.firstAngle);
            telemetry.addData("Average Encoder", encoderAverage());
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            angleError = Math.abs(Math.abs(targetAngle) - Math.abs(angles.firstAngle));

            FLSpeed = speed;
            FRSpeed = speed;
            BLSpeed = speed;
            BRSpeed = speed;

            if(targetAngle == 180 && angles.firstAngle < 0){
                FRSpeed -= angleError/errorFactor;
                BRSpeed -= angleError/errorFactor;
            } else if(targetAngle < angles.firstAngle){
                FRSpeed -= angleError/errorFactor;
                BRSpeed -= angleError/errorFactor;
                telemetry.addData("greater", "greater");
            } else{
                FLSpeed -= angleError/errorFactor;
                BLSpeed -= angleError/errorFactor;
                telemetry.addData("less", "less");
            }

            //Set robot speed
            frontRight.setPower(-FRSpeed);
            backRight.setPower(BRSpeed);
            frontLeft.setPower(FLSpeed);
            backLeft.setPower(BLSpeed);

            telemetry.update();
        }
        if(distance > 100) {
            frontRight.setPower(0.5);
            backRight.setPower(-0.5);
            frontLeft.setPower(-0.5);
            backLeft.setPower(-0.5);
        }
        delay(0.1);
        //Reset encoders to prepare for the next command
        //resetEncoders();
        stopRobot();
    }

    public void arcRightBack(double speed, int distance){
        //Declare necessary values
        double angleError;

        //Rather than constantly resetting the encoders, we can add the new distance
        double newDistance = distance + encoderAverage();

        //resetEncoders();

        while(encoderAverage() < newDistance && opModeIsActive()){

            //Set robot speed
            frontRight.setPower(0);
            backRight.setPower(0);
            frontLeft.setPower(-speed);
            backLeft.setPower(-speed);


            telemetry.update();
        }
        //Reset encoders to prepare for the next command
        //resetEncoders();
        stopRobot();
    }

    public void arcLeftBack(double speed, int distance){
        //Declare necessary values
        double angleError;

        //Rather than constantly resetting the encoders, we can add the new distance
        double newDistance = distance + encoderAverage();

        //resetEncoders();

        while(encoderAverage() < newDistance && opModeIsActive()){

            //Set robot speed
            frontRight.setPower(speed);
            backRight.setPower(-speed);
            frontLeft.setPower(0);
            backLeft.setPower(0);


            telemetry.update();
        }
        //Reset encoders to prepare for the next command
        //resetEncoders();
        stopRobot();
    }

    public void arcRight(double speed, int distance){
        //Declare necessary values
        double angleError;

        //Rather than constantly resetting the encoders, we can add the new distance
        double newDistance = distance + frontLeft.getCurrentPosition(), FLpos = frontLeft.getCurrentPosition();

        //resetEncoders();

        while(FLpos < newDistance && opModeIsActive()){

            //Set robot speed
            frontRight.setPower(0);
            backRight.setPower(0);
            frontLeft.setPower(speed);
            backLeft.setPower(speed);


            telemetry.update();
            FLpos = frontLeft.getCurrentPosition();
        }
        //Reset encoders to prepare for the next command
        //resetEncoders();
        stopRobot();
    }
    public void arcLeft(double speed, int distance){
        //Declare necessary values
        double angleError;

        double FRSpeed;
        double BRSpeed;

        //Rather than constantly resetting the encoders, we can add the new distance
        double newDistance = distance + frontRight.getCurrentPosition(), FRpos = frontRight.getCurrentPosition();

        //resetEncoders();

        while(FRpos < newDistance && opModeIsActive()){
            FRSpeed = speed;
            BRSpeed = speed;

            //Set robot speed
            frontRight.setPower(-FRSpeed);
            backRight.setPower(BRSpeed);
            frontLeft.setPower(0);
            backLeft.setPower(0);

            telemetry.update();
            FRpos = frontRight.getCurrentPosition();
        }
        //Reset encoders to prepare for the next command
        //resetEncoders();
        stopRobot();
    }


    public void moveBackward(double speed, int distance, double targetAngle){
        //Declare necessary values
        double angleError;

        double FLSpeed;
        double FRSpeed;
        double BLSpeed;
        double BRSpeed;

        //Rather than constantly resetting the encoders, we can add the new distance
        int newDistance = distance + encoderAverage();

        //resetEncoders();
    //int reduceSpeed = 50, difference = distance;

        while(encoderAverage() < newDistance && opModeIsActive()){
            // Determine angle and error
            telemetry.addData("Current Angle", angles.firstAngle);
            telemetry.addData("Average Encoder", encoderAverage());
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            angleError = Math.abs(Math.abs(targetAngle) - Math.abs(angles.firstAngle));

            FLSpeed = speed;
            FRSpeed = speed;
            BLSpeed = speed;
            BRSpeed = speed;
            /*if (difference < reduceSpeed){
                speed /= 2;
                reduceSpeed /= 2;
            }
            difference = newDistance - encoderAverage();
            */

            //Correct the Position
            if(targetAngle == 180 && angles.firstAngle < 0){
                FRSpeed += angleError/errorFactor;
                BRSpeed += angleError/errorFactor;
                FLSpeed -= angleError/errorFactor;
                BLSpeed -= angleError/errorFactor;
            } else if(targetAngle < angles.firstAngle){
                FRSpeed += angleError/errorFactor;
                BRSpeed += angleError/errorFactor;
                FLSpeed -= angleError/errorFactor;
                BLSpeed -= angleError/errorFactor;
                telemetry.addData("greater", "greater");
            } else{
                FLSpeed += angleError/errorFactor;
                BLSpeed += angleError/errorFactor;
                FRSpeed -= angleError/errorFactor;
                BRSpeed -= angleError/errorFactor;
                telemetry.addData("less", "less");
            }

            //Set robot speed
            frontRight.setPower(FRSpeed);
            backRight.setPower(-BRSpeed);
            frontLeft.setPower(-FLSpeed);
            backLeft.setPower(-BLSpeed);

            telemetry.update();
        }

        frontRight.setPower(-1);
        backRight.setPower(1);
        frontLeft.setPower(1);
        backLeft.setPower(1);
        delay(0.1);
        //Reset encoders to prepare for the next command
        //resetEncoders();
        stopRobot();
    }

    public void moveRight(double speed, int distance, double targetAngle){
        //Declare necessary values
        double angleError;

        double FLSpeed;
        double FRSpeed;
        double BLSpeed;
        double BRSpeed;

        //Rather than constantly resetting the encoders, we can add the new distance
        //double newDistance = distance + encoderAverage();

        resetEncoders();

        while(encoderAverage() < distance && opModeIsActive()){
            // Determine angle and error
            telemetry.addData("Current Angle", angles.firstAngle);
            telemetry.addData("Average Encoder", encoderAverage());
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            angleError = Math.abs(Math.abs(targetAngle) - Math.abs(angles.firstAngle));

            FLSpeed = speed;
            FRSpeed = speed;
            BLSpeed = speed;
            BRSpeed = speed;

            //Correct the Position
            if(targetAngle == 180 && angles.firstAngle < 0){
                BLSpeed -= angleError/errorFactor;
                BRSpeed -= angleError/errorFactor;
            } else if(targetAngle < angles.firstAngle){
                BLSpeed -= angleError/errorFactor;
                BRSpeed -= angleError/errorFactor;
                telemetry.addData("greater", "greater");
            } else{
                FLSpeed -= angleError/errorFactor;
                FRSpeed -= angleError/errorFactor;
                telemetry.addData("less", "less");
            }

            //Set robot speed
            frontRight.setPower(FRSpeed);
            backRight.setPower(BRSpeed);
            frontLeft.setPower(FLSpeed);
            backLeft.setPower(-BLSpeed);

            telemetry.update();
        }
        //Reset encoders to prepare for the next command
        //resetEncoders();
        stopRobot();
    }

    public void moveLeft(double speed, int distance, double targetAngle){
        //Declare necessary values
        double angleError;

        double FLSpeed;
        double FRSpeed;
        double BLSpeed;
        double BRSpeed;

        //Rather than constantly resetting the encoders, we can add the new distance
        //double newDistance = distance + encoderAverage();

        resetEncoders();

        while(encoderAverage() < distance && opModeIsActive()){
            // Determine angle and error
            telemetry.addData("Current Angle", angles.firstAngle);
            telemetry.addData("Average Encoder", encoderAverage());
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            angleError = Math.abs(Math.abs(targetAngle) - Math.abs(angles.firstAngle));

            FLSpeed = speed;
            FRSpeed = speed;
            BLSpeed = speed;
            BRSpeed = speed;

            //Correct the Position
            if(targetAngle == 180 && angles.firstAngle < 0){
                BLSpeed += angleError/errorFactor;
                BRSpeed += angleError/errorFactor;
                FLSpeed -= angleError/errorFactor;
                FRSpeed -= angleError/errorFactor;
            } else if(targetAngle < angles.firstAngle){
                BLSpeed += angleError/errorFactor;
                BRSpeed += angleError/errorFactor;
                FLSpeed -= angleError/errorFactor;
                FRSpeed -= angleError/errorFactor;
                telemetry.addData("greater", "greater");
            } else{
                FLSpeed += angleError/errorFactor;
                FRSpeed += angleError/errorFactor;
                BLSpeed -= angleError/errorFactor;
                BRSpeed -= angleError/errorFactor;
                telemetry.addData("less", "less");
            }

            //Set robot speed
            frontRight.setPower(-FRSpeed);
            backRight.setPower(-BRSpeed);
            frontLeft.setPower(-FLSpeed);
            backLeft.setPower(BLSpeed);

            telemetry.update();
        }
        //Reset encoders to prepare for the next command
        //resetEncoders();
        stopRobot();
    }

    public void delay(double seconds) {
        elapsedTime.reset();
        while (elapsedTime.time() < seconds && opModeIsActive()) ;

    }

    public void stopRobot() {
        frontRight.setPower(0);
        backRight.setPower(0);
        frontLeft.setPower(0);
        backLeft.setPower(0);
    }
    public void resetEncoders() {
        stopRobot();

        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        delay(0.1);

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public int encoderAverage(){
        int average, sum = 0;
        sum += Math.abs(backLeft.getCurrentPosition());
        sum += Math.abs(frontLeft.getCurrentPosition());
        sum += Math.abs(backRight.getCurrentPosition());
        sum += Math.abs(frontRight.getCurrentPosition());
        average = sum/4;

        return average;
    }
    /* Declares the four wheels */
    public DcMotor frontLeft = null;
    public DcMotor backLeft = null;
    public DcMotor frontRight = null;
    public DcMotor backRight = null;
    public DcMotor intake = null;
    public DcMotor verticalSlides = null;

    /* Declares the auto claw servos */
    public Servo blueClaw = null;
    public Servo blueGrabber = null;
    public Servo redClaw = null;
    public Servo redGrabber = null;

    /*Declares the main lift servos*/
    public Servo horizontalSlides = null;
    public Servo backGrabber = null;
    public Servo frontGrabber = null;

    /* Declares the frame grabber servos*/
    public Servo leftFrame = null;
    public Servo rightFrame = null;

    /* Sets servo position */
    public static final double MID_SERVO = 0.5 ;

    /* Declares the local OpMode members */
    //HardwareMap hwMap           =  null;

    private ElapsedTime period  = new ElapsedTime();
    @Override
    public void runOpMode() throws InterruptedException {

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = false;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);


        /* Define and Initialize Drive Motors */
        frontLeft = hardwareMap.get(DcMotor.class, "FL");
        backLeft = hardwareMap.get(DcMotor.class, "BL");
        frontRight = hardwareMap.get(DcMotor.class, "FR");
        backRight = hardwareMap.get(DcMotor.class, "BR");
        intake = hardwareMap.get(DcMotor.class, "I");
        verticalSlides = hardwareMap.get(DcMotor.class, "VS"); //Because we have two sets of slides


        frontLeft.setDirection(DcMotor.Direction.REVERSE); //Need to confirm these
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        /* Set all motors to zero power */
        frontLeft.setPower(0);
        backLeft.setPower(0);
        frontRight.setPower(0);
        backRight.setPower(0);
        intake.setPower(0);

        /* Set motors to run with encoders*/
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        verticalSlides.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        /* Define and initialize autonomous servos*/
        blueClaw = hardwareMap.get(Servo.class, "BC");
        blueClaw.setPosition(0.2);

        blueGrabber = hardwareMap.get(Servo.class, "BG");
        blueGrabber.setPosition(0.2);

        redClaw = hardwareMap.get(Servo.class, "RC");
        redClaw.setPosition(0.7);

        redGrabber = hardwareMap.get(Servo.class, "RG");
        redGrabber.setPosition(0.27);

        leftFrame = hardwareMap.get(Servo.class, "LFG");
        leftFrame.setPosition(0.9); // This is the first thing you should have to change, as everything else is the same as before

        rightFrame = hardwareMap.get(Servo.class, "RFG");
        rightFrame.setPosition(0); // You will likely have to change this as well.

        /* Define and intialize other servos*/

        horizontalSlides = hardwareMap.get(Servo.class, "HS");  // This shows the servo's configuration on the phones
        horizontalSlides.setPosition(0.67); // This is the initialized position of the servo, so this is what you change

        frontGrabber = hardwareMap.get(Servo.class, "FMG"); //Example: you rotate the FMG, but it isn't opening enough
        frontGrabber.setPosition(0.7);  //In that case you need to initialize 0.75 to 0.8 or even higher.

        backGrabber = hardwareMap.get(Servo.class, "BMG");
        backGrabber.setPosition(0.35);

        elapsedTime = new ElapsedTime();

        //waitForStart();
    }
}
