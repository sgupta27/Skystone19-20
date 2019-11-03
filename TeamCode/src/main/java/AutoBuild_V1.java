import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "AutoBuild_V1")
public class AutoBuild_V1 extends LinearOpMode
{
    private MecBot holo;

    public void runOpMode()
    {
        holo = new MecBot(hardwareMap, this);
        waitForStart();
        //on blue build side, robot facing the build triangle with the middle of the robot (black lines)
        //lined up with the outside edge of the side closer to the triangle of the second square from
        //the ref side of the field
        holo.driveStrafe_Inches(27, .8);
        //drop down platform grabber
        holo.drivePivot_Degrees(90, .8);
        holo.driveStraight_Inches(10, .8);
        //release platform grabber
        holo.driveStrafe_Inches(-4, .8);
        holo.driveStraight_Inches(-8, .8);
        holo.driveStrafe_Inches(-44, .8);
        holo.stopDriveMotors();
    }
}
