package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "CompTeleDemoV1")
public class CompTeleDemoV1 extends OpMode
{
    private CompRobot compRobot;
    private double wristPosition = .4;
    private boolean isInDemoMode = true;
    private boolean hasXBeenPressed = false;
    private double speedFactor = .5;

    @Override
    public void init()
    {
        compRobot = new CompRobot(hardwareMap);
        gamepad2.setJoystickDeadzone(.3f);
        gamepad1.setJoystickDeadzone(.3f);
    }

    @Override
    public void loop()
    {
        telemetry.addData("Demo Mode: ", isInDemoMode);
        //Gramepad 1 -- DRIVER
        //Driving robot
        if (isInDemoMode)
            speedFactor = .5;
        else
            speedFactor = 1;

        compRobot.driveMotors(-gamepad1.left_stick_y * speedFactor, -gamepad1.right_stick_y * speedFactor);

        // control the motor for climbing
        if(!isInDemoMode)
        {
            compRobot.getClimberMotor().setPower(gamepad1.right_trigger - gamepad1.left_trigger);

            if (gamepad1.y)
                compRobot.samplerUp();
        }

        //Gamepad 2 -- Attachments

        if(!isInDemoMode)
        {
            //The grabber wheel stuff, AKA the mineral Ejector

            if (gamepad2.left_trigger > .2f)
                compRobot.setGrabberWheelPower(-gamepad2.left_trigger);
            else if (gamepad2.left_bumper)
                compRobot.setGrabberWheelPower(1); //Positive power means you're spitting it out
            else
                compRobot.setGrabberWheelPower(0);

            //This controls the wrist of the grabbers

            if (gamepad2.right_bumper)
            {
                wristPosition += .0038;
                if (wristPosition > 1)
                    wristPosition = 1;
            }
            else if (gamepad2.right_trigger > .2f)
            {
                wristPosition -= .0038;
                if (wristPosition < .4)
                    wristPosition = .4;
            }

            compRobot.getWristCollectorServo().setPosition(wristPosition);

            telemetry.addData("WristPosition: ", wristPosition);

            //Extender Controls (not climbing)
            compRobot.getCollectorLifterMotor().setPower(-gamepad2.left_stick_y);

            //The entire arm pivot controls or shoulder controls
            compRobot.getCollectorPivoterMotor().setPower(-gamepad2.right_stick_y / 4);
        }

        //The "Demo mode" toggler
        if(gamepad2.x)
        {
            if(!hasXBeenPressed)
            {
                isInDemoMode = !isInDemoMode;
                hasXBeenPressed = true;
            }
        }
        else
            hasXBeenPressed = false;

        telemetry.update();
    }

    public void stop()
    {
        compRobot.stopDriveMotors();
        super.stop();
    }
}
