import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "AutoRedBuild_V2")
public class AutoRedBuild_V2 extends LinearOpMode
{
    private MecBot holo;

    public void runOpMode()
    {
        holo = new MecBot(hardwareMap, this);
        holo.setLightsColor(RevBlinkinLedDriver.BlinkinPattern.RED);
        waitForStart();

        //on red build side, robot facing the platforms with the red plate/black lines lined up to the
        // ref side edge of the second square from the ref side
        //THIS MISSION IS WORKING, TWEAKS PROBABLY DO NOT NEED TO BE MADE!!!!!!!!!!!!!!!!!!!!
        holo.driveStrafe_Inches(12,.8, this);
        holo.driveStraight_Inches(26, .8, this); // Changed this line and the line below. We split the drive straight into 2 parts. The second part is slower to make sure we don't bump the platform
        holo.driveStraight_Inches(7,0.5,this);
        holo.platformGrab();
        sleep(700);
        holo.driveStraight_Inches(-33, .4, this);
        holo.platformRelease();
        holo.driveStrafe_Inches(-30, .8, this);
        holo.driveStraight_Inches(21, .8, this);
        holo.driveStrafe_Inches(7,.8, this);
        holo.driveStrafe_Inches(-7,.8, this);
        holo.driveStraight_Inches(-18,.8, this);
        holo.driveStrafe_Inches(-20,.8, this);
        holo.driveStraight_Inches(-4,.8,this);
        while (opModeIsActive() && !isStopRequested())
        {
            holo.stopDriveMotors();
            holo.stopAllMotors();
        }
    }
}
