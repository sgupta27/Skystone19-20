import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "AutoBlueBuild_V1")
public class AutoBlueBuild_V1 extends LinearOpMode
{
    private MecBot holo;

    public void runOpMode()
    {
        holo = new MecBot(hardwareMap, this);
        holo.setLightsColor(RevBlinkinLedDriver.BlinkinPattern.SKY_BLUE);
        waitForStart();

        //on blue build side, robot facing the platforms with the black lines lined up to the ref side
        //edge of the second square from the ref side
        holo.driveStrafe_Inches(-12,.8, this);
        holo.driveStraight_Inches(32, .8, this);
        holo.platformGrab();
        sleep(700);
        holo.driveStraight_Inches(-31, .8, this);
        holo.platformRelease();
        holo.driveStrafe_Inches(28, .8, this);
        holo.driveStraight_Inches(21, .8, this);
        holo.driveStrafe_Inches(-6,.8, this);
        holo.driveStrafe_Inches(6,.8, this);
        holo.driveStraight_Inches(-18,.8, this);
        holo.driveStrafe_Inches(20,.8, this);
        while (opModeIsActive() && !isStopRequested())
        {
            holo.stopDriveMotors();
            holo.stopAllMotors();
        }
    }
}
