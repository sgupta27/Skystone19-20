import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;

@TeleOp(name = "MecTeleController")
public class MecTeleController extends OpMode
{
    private MecBot holo;
    private float rValue, lValue;
    private boolean toggleSpeedMode = false;
    private boolean hasSpeedModeBeenActivated = false;
    private double wristPosition = .4;


    public void init() {
        holo = new MecBot(hardwareMap, this);
        gamepad1.setJoystickDeadzone(.25f);
        gamepad2.setJoystickDeadzone(.25f); 500 650 850 1000
        holo.resetRunTime();
        holo.setLightsColor(RevBlinkinLedDriver.BlinkinPattern.BLACK);
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
        //Drive Controls (controller 1)
        double Forward = -gamepad1.left_stick_y; // this was -gamepad1.leftstick_y; AND also changed Speed to Forward
        double Strafe = -gamepad1.left_stick_x;
        double Turn = -gamepad1.right_stick_x;
        //double RightY = gamepad1.right_stick_y;
        double MAX_SPEED = .8;
        double runTime = holo.getRunTime();
        telemetry.addData("Forward: ", Forward); //Removed for testing
        telemetry.addData("Strafe: ", Strafe);
        telemetry.addData("Turn: ", Turn);
        //telemetry.addData("Right Y: ", RightY);
        telemetry.addData("MAX_SPEED: ", MAX_SPEED);
        telemetry.update();
        holo.holonomic(Turn, Strafe, Forward, MAX_SPEED);
        if (runTime > 90) //Shows us when 30 seconds are left
        {
            holo.setLightsColor(RevBlinkinLedDriver.BlinkinPattern.GREEN);
        }
        //Attachment controls (controller 2)
        //arm pivot/shoulder action, in and out action, wrist action, close/open action
        /*if (gamepad2.right_bumper) //wrist code from last year
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

        if (gamepad2.left_bumper) //wrist code from last year
        {
            openGrabber()//Doesn't exist
        }
        else if (gamepad2.left_trigger > .2f)
        {
            closeGrabber()//Doesn't exist
        }

        compRobot.getWristCollectorServo().setPosition(wristPosition);

        compRobot.getCollectorLifterMotor().setPower(-gamepad2.left_stick_y);//in and out from last year

        compRobot.getCollectorPivoterMotor().setPower(-gamepad2.right_stick_y / 4);//arm pivot/shoulder from last year
        */
    }
}
