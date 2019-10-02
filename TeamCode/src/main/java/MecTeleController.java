import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "MecTeleController")
public class MecTeleController extends OpMode
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

    /*public void test()
    {
        double Speed = -gamepad1.left_stick_y;
        double Turn = gamepad1.left_stick_x;
        double Strafe = gamepad1.right_stick_x;
        double MAX_SPEED = 1.0;
        holo.holonomic(Speed, Turn, Strafe, MAX_SPEED);
    }*/

    /*public void holonomic(double Speed, double Turn, double Strafe, double MAX_SPEED)
    {
        //left front= +speed +Turn +strafe
        //right front= +speed -Turn -strafe
        //left rear= +speed +Turn -strafe
        //right rear= +speed -Turn +strafe

        double Magnitude = Math.abs(Speed) + Math.abs(Turn) + Math.abs(Strafe);
        Magnitude = (Magnitude > 1) ? Magnitude: 1; //Set Scaling to keep -1 to +1 range
        if (Magnitude > 1)
        {
            Speed = Speed/Magnitude;
            Turn = Turn/Magnitude;
            Strafe = Strafe/Magnitude;
        }

        driveLeftFront.setPower(Speed + Turn + Strafe);
        if (driveLeftBack != null)
        {
            driveLeftBack.setPower(Speed - Turn - Strafe);
        }

        driveRightFront.setPower(Speed + Turn - Strafe);
        if (driveRightBack !=null)
        {
            driveRightBack.setPower(Speed - Turn + Strafe);
        }
    }*/

    public void loop()
    {
        double Speed = -gamepad1.left_stick_y;
        double Strafe = gamepad1.left_stick_x;
        double Turn = gamepad1.right_stick_x;
        double RightY = gamepad1.right_stick_y;
        double MAX_SPEED = .5;
        telemetry.addData("Speed: ", Speed);
        telemetry.addData("Strafe: ", Strafe);
        telemetry.addData("Turn: ", Turn);
        telemetry.addData("Right Y: ", RightY);
        telemetry.addData("MAX_SPEED: ", MAX_SPEED);
        telemetry.update();
        holo.holonomic(Turn, Strafe, Speed, MAX_SPEED);

    }
}
