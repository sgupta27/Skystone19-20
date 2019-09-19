import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name = "MecTestTele")
public class MecTestTele extends OpMode
{
    private MecBot holo;
    private float rValue, lValue;
    private boolean toggleSpeedMode = false;
    private boolean hasSpeedModeBeenActivated = false;

    public void init()
    {
        holo = new MecBot(hardwareMap);
        gamepad1.setJoystickDeadzone(.25f);
        gamepad2.setJoystickDeadzone(.25f);
    }

    public void start()
    {

    }

    public void loop()
    {
        if (gamepad1.b)
        {
            holo.driveStraight_Enc(100f, .5);
        }
        else
        {
            holo.driveRightFront(0f);
        }
        if (gamepad1.a)
        {
            holo.pivot_enc(1500);
        }
        else
        {
            holo.driveRightBack(0f);
        }
        if (gamepad1.y)
        {
            holo.strafe_enc(-150);
        }
        else
        {
            holo.driveLeftFront(0f);
        }
        if (gamepad1.x)
        {
            holo.strafe_enc(150);
        }
        else
        {
            holo.driveLeftBack(0f);
        }
        /*if (toggleSpeedMode)
        {
            telemetry.addData("Speed mode = ON", null);
            rValue = gamepad1.right_stick_y;
            lValue = gamepad1.left_stick_y;
        }
        else
        {
            telemetry.addData("speed mode = OFF", null);
            rValue = gamepad1.right_stick_y * .5f;
            lValue = gamepad1.left_stick_y * .5f;
        }

        telemetry.addData("Radian Velocity Left Front", holo.getDriveLeftFront().getVelocity(AngleUnit.RADIANS));
        telemetry.addData("Radian Velocity Left Back", holo.getDriveLeftBack().getVelocity(AngleUnit.RADIANS));
        telemetry.addData("radian v right front", holo.getDriveRightFront().getVelocity(AngleUnit.RADIANS));
        telemetry.addData("radian v right back", holo.getDriveRightBack().getVelocity(AngleUnit.RADIANS));
        telemetry.addData("LJoyStick= ", gamepad1.left_stick_y);
        telemetry.addData("RJoyStick= ", gamepad1.right_stick_y);
        telemetry.addData("RFrontEncoders= ", holo.getRightFrontEncoderPos());
        telemetry.addData("RBackEncoders= ", holo.getRightBackEncoderPos());
        telemetry.addData("LFrontEncoders= ", holo.getLeftFrontEncoderPos());
        telemetry.addData("LBackEncoders= ", holo.getLeftBackEncoderPos());
        telemetry.update();
        holo.driveMotors(lValue, rValue);*/
    }

    public void stop()
    {
        holo.stopAllMotors();
    }
}
