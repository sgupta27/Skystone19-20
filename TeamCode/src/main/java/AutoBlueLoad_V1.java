import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;

@Autonomous(name = "AutoBlueLoad_V1")
public class AutoBlueLoad_V1 extends LinearOpMode
{
    private MecBot holo;

    public void runOpMode()
    {
        holo = new MecBot(hardwareMap,this);
        waitForStart();

        //blue loading side, robot facing the cubes in the middle, outside of the left wheels lined
        //up with the outside edge closer to the build zone of the second square from the audience side
        holo.driveStraight_Inches(25, .8);
        char colorSkystone = holo.getColor();
        float totalStrafeDist_In = -52;
        telemetry.addData("stone color = ", colorSkystone);
        telemetry.update();
        while (colorSkystone == 'y')
        {
            holo.driveStrafe_Inches(-8, .8);
            totalStrafeDist_In = totalStrafeDist_In + 8;
            colorSkystone = holo.getColor();
            telemetry.addData("stone color 2 = ", colorSkystone);
            telemetry.update();
        }
        //grab skystone
        holo.driveStraight_Inches(-5,.8);
        holo.driveStrafe_Inches(totalStrafeDist_In, .8);
        //drop skystone
        holo.driveStrafe_Inches(13, .8);
        holo.stopAllMotors();
    }
}
