import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "AutoRedBuild_V1")
public class AutoRedBuild_V1 extends LinearOpMode
{
    private MecBot holo;

    public void runOpMode()
    {
        holo = new MecBot(hardwareMap, this);
        waitForStart();
        //on red build side, robot facing the platforms with the red plate/black lines lined up to the
        // ref side edge of the second square from the ref side
        holo.driveStraight_Inches(30, .8);
        //drop down platform grabber
        holo.driveStraight_Inches(-30, .8);
        holo.driveStrafe_Inches(-10, .8);
        holo.driveStraight_Inches(3, .8);
        holo.driveStrafe_Inches(-35,.8);
    }
}
