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
    private double wristPosition = 0.0;
    private boolean grabberDeployed = false;
    private boolean grabberBTNReleased = true;
    private double lastShoulderPosition = 0;

    public void init()
    {
        holo = new MecBot(hardwareMap, this);
        gamepad1.setJoystickDeadzone(.25f);
        gamepad2.setJoystickDeadzone(.25f);
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
        double MAX_SPEED = 1;
        double runTime = holo.getRunTime();
        boolean stealthMode = false;
        boolean triggerHeld = false;
        double speedFactor = .5;
        if(gamepad1.right_trigger > 0.2f)
        {
            Forward *= 0.5f;
            Strafe *= 0.5f;
            Turn *= 0.5f;
        }
        holo.holonomic(Turn, Strafe, Forward, MAX_SPEED);

        if (runTime > 90) //Shows us when 30 seconds are left
        {
            holo.setLightsColor(RevBlinkinLedDriver.BlinkinPattern.GREEN);
        }
        else if (runTime > 75) //Shows us when 45 seconds are left
        {
            holo.setLightsColor(RevBlinkinLedDriver.BlinkinPattern.WHITE);
        }
        //Attachment controls (controller 2)
        //arm pivot/shoulder action, in and out action, wrist action, close/open action
        double armPower = gamepad2.left_stick_y;
        holo.setArmPower(armPower);

        //The entire arm pivot controls or shoulder controls
        double shoulderPower_PCT = gamepad2.right_stick_y;
        double shoulderPosition_ENC = holo.getShoulderPosition();
        telemetry.addData("Enc Shoulder Pos: ", shoulderPosition_ENC);
        telemetry.addData("Enc extender Pos: ", holo.getArmPosition());
        if (Math.abs(shoulderPower_PCT) > 0.05)
        {
            if (shoulderPower_PCT < 0.0f)
            {
                shoulderPower_PCT *= 0.5f; //negative is up
            }
            else
            {
                shoulderPower_PCT *= 0.2; //gravity is helping us
            }
            shoulderPower_PCT += -0.1;//small up to account for gravity
            holo.setShoulderPower(shoulderPower_PCT); //play with the denominator if needed to slow down
            if (shoulderPower_PCT > 0)
            {
                wristPosition = Math.abs(shoulderPosition_ENC) * 0.0005887 + 0.4294;
            }
            else
            {
                wristPosition = Math.abs(shoulderPosition_ENC) * 0.0005158 + 0.3875;
            }
            lastShoulderPosition = shoulderPosition_ENC;
        }
        else if (shoulderPosition_ENC > lastShoulderPosition)
        {
            holo.setShoulderPower(-0.24);
        }
        else
        {
            holo.setShoulderPower(0.0);
        }

        if (gamepad2.y)
        {
            wristPosition = 0;
        }
        else if (gamepad2.right_bumper)
        {
            wristPosition -= .0038;
        }
        else if (gamepad2.right_trigger > .2f)
        {
            wristPosition += .0038;
        }
        telemetry.addData("wrist before:", wristPosition);
        wristPosition = holo.setWristPosition(wristPosition);
        telemetry.addData("wrist after:", wristPosition);
        telemetry.update();
        //gamepad 1 button a will toggle platform grabbers
        if (grabberBTNReleased)
        {
            if (gamepad1.a)
            {
                grabberBTNReleased = false;
                grabberDeployed = !grabberDeployed;
                if (grabberDeployed)
                {
                    holo.platformGrab();
                }
                else
                {
                    holo.platformRelease();
                }
            }
        }
        else if (!gamepad1.a)
        {
            grabberBTNReleased = true;
        }
        // Arm clamp Controls

       if (gamepad2.left_bumper)
        {
            holo.clamp(false);
        }
        else if (gamepad2.left_trigger > .2f)
        {
            holo.clamp(true);
        }
        telemetry.update();
    }

    public void stop()
    {
        holo.setWristPosition(0);
        holo.stopAllMotors();
    }
}
