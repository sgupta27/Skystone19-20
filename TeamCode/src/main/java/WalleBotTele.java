import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name = "WalleBotTele")
public class WalleBotTele extends OpMode
{
    private WalleBot tank;
    private float rValue, lValue;
    private boolean toggleSpeedMode = false;
    private boolean hasSpeedModeBeenActivated = false;

    public void init()
    {
        tank = new WalleBot(hardwareMap);
        gamepad1.setJoystickDeadzone(.25f);
        gamepad2.setJoystickDeadzone(.25f);
    }

    public void start()
    {

    }

    public void loop()
    {

        if (toggleSpeedMode)
        {
            telemetry.addData("Full speed ahead!!!!!", null);
            rValue = gamepad1.right_stick_y;
            lValue = gamepad1.left_stick_y;
        }
        else
        {
            telemetry.addData("Demonstration/KID mode", null);
            rValue = gamepad1.right_stick_y * .5f;
            lValue = gamepad1.left_stick_y * .5f;
        }

        telemetry.addData("Radian Velocity Left", tank.getDriveLeftOne().getVelocity(AngleUnit.RADIANS));
        telemetry.addData("radian v right", tank.getDriveRightOne().getVelocity(AngleUnit.RADIANS));
        telemetry.addData("LJoyStick= ", gamepad1.left_stick_y);
        telemetry.addData("RJoyStick= ", gamepad1.right_stick_y);
        telemetry.addData("REncoders= ", tank.getRightEncoderPos());
        telemetry.addData("LEncoders= ", tank.getLeftEncoderPos());
        telemetry.update();
        tank.driveMotors(lValue, rValue);

        if (gamepad1.a)
        {
            if (!hasSpeedModeBeenActivated)
            {
                toggleSpeedMode = !toggleSpeedMode;
                hasSpeedModeBeenActivated = true;
            }
        }
        else
        {
            hasSpeedModeBeenActivated = false;
        }
    }

    public void stop()
    {
        tank.stopAllMotors();
    }
}
