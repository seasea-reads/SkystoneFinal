package org.firstinspires.ftc.teamcode.Hardware;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.util.ElapsedTime;

/* The purpose of this class is to declare all motors, servos, and sensors
   in a single location to make identifying them easier in the future */
public class HardwareList {
    /* Declares the four wheels */
    public DcMotor frontLeft = null;
    public DcMotor backLeft = null;
    public DcMotor frontRight = null;
    public DcMotor backRight = null;
    public DcMotor intake = null;
    public DcMotor verticalSlides = null;

    /* Declares the VS limit switch*/
    public DigitalChannel limitSwitch;

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
    HardwareMap hardwareMap =  null;

    private ElapsedTime period  = new ElapsedTime();

    /* Initialize the standard Hardware interfaces */
    public void init(HardwareMap rhwMap) {
        /* Save the reference as Hardware map */
        hardwareMap = rhwMap;

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

        /*Define and initialize limit switch*/
        limitSwitch = hardwareMap.get(DigitalChannel.class, "VT");
        limitSwitch.setMode(DigitalChannel.Mode.INPUT);

        /* Define and initialize autonomous servos*/
        blueClaw = hardwareMap.get(Servo.class, "BC");
        blueClaw.setPosition(0.8);

        blueGrabber = hardwareMap.get(Servo.class, "BG");
        blueGrabber.setPosition(0.2);

        redClaw = hardwareMap.get(Servo.class, "RC");
        redClaw.setPosition(0);

        redGrabber = hardwareMap.get(Servo.class, "RG");
        redGrabber.setPosition(0.7);

/* ^^If you look above me, there is a tab called HardwareList.  I'm using this for teleop initialization.
        I set up two initializations, one for auto with the gyro functions included, and one for teleop.
        Do not worry about editing the auto initializations, I can do this once I start programming auto.
        Also, you should see a tab called DriverControlledComp3.  This is what you will edit for teleop controls.
        On the left, you can see where they are found in the folders.  Don't worry abbout changing anything else,
        it won't help you with teleop. */

        leftFrame = hardwareMap.get(Servo.class, "LFG");
        leftFrame.setPosition(0.9); // This is the first thing you should have to change, as everything else is the same as before

        rightFrame = hardwareMap.get(Servo.class, "RFG");
        rightFrame.setPosition(0); // You will likely have to change this as well.

        /* Define and intialize other servos*/

        horizontalSlides = hardwareMap.get(Servo.class, "HS");  // This shows the servo's configuration on the phones
        horizontalSlides.setPosition(0.58); // This is the initialized position of the servo, so this is what you change

        frontGrabber = hardwareMap.get(Servo.class, "FMG"); //Example: you rotate the FMG, but it isn't opening enough
        frontGrabber.setPosition(0.7);  //In that case you need to initialize 0.75 to 0.8 or even higher.

        backGrabber = hardwareMap.get(Servo.class, "BMG");
        backGrabber.setPosition(0.35);// Keep the backGrabber closed, frontGrabber open, and horizontal slides closed in init.
    }
} // This is the end of the program, so now you know where to find all of the stuff you need to change