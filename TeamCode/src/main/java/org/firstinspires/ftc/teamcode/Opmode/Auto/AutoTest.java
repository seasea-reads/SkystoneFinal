package org.firstinspires.ftc.teamcode.Opmode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Hardware.HardwareList;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.WheelMovement;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "TestGyro", group = "Tests")
public class AutoTest extends WheelMovement {

    @Override
    public void runOpMode() throws InterruptedException {

        super.runOpMode();
        waitForStart();
        resetEncoders();
        elapsedTime.reset();



        //moveRight(0.5, 4000, 0);

        //moveLeft(0.5, 2000, 0);

        //moveForward(0.5, 2000, 0);

        moveBackward(0.5, 3000, 0);

        while(opModeIsActive()){
            if(isStopRequested() == true)
                requestOpModeStop();
        }
    }
}
