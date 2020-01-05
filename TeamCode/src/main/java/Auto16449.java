import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Auto16449")
public class Auto16449 extends LinearOpMode
{
    private MecBot holo;


    public void runOpMode()
    {
        holo = new MecBot(hardwareMap, this);
        holo.setLightsColor(RevBlinkinLedDriver.BlinkinPattern.SKY_BLUE);
        waitForStart();

        holo.driveStrafe_Inches(-40, .8, this);
        float requiredDist_in = 4.5f;
        float intervalDistance = 7; //What is this supposed to do?
        holo.kissWall(requiredDist_in, intervalDistance, this);
        holo.driveStraight_Inches(3, .8, this);
        while (opModeIsActive() && !isStopRequested())
        {
            holo.stopDriveMotors();
            holo.stopAllMotors();
        }
    }
}
