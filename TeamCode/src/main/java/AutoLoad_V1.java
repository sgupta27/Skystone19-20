import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "AutoLoad_V1")
public class AutoLoad_V1 extends LinearOpMode
{
    private MecBot holo;

    public void runOpMode()
    {
        holo = new MecBot(hardwareMap, this);
        waitForStart();

        //blue loading side, robot facing the cubes in the middle, outside of the left wheels lined
        //up with the outside edge closer to the build zone of the second square from the audience side
        holo.driveStraight_Inches(27, .8);
        //check for color of cubes and get correct cube
        holo.driveStraight_Inches(-5, .8);
        holo.driveStrafe_Inches(-52, .8);
        //drop off cube
        holo.driveStrafe_Inches(16, .8);
    }
}
