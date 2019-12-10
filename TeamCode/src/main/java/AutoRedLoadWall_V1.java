import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "AutoRedLoadWall_V1")
public class AutoRedLoadWall_V1 extends LinearOpMode
{
    private MecBot holo;

    public void runOpMode()
    {
        holo = new MecBot(hardwareMap, this);
        holo.setLightsColor(RevBlinkinLedDriver.BlinkinPattern.RED);
        waitForStart();

        //red loading side, robot facing the cubes in the middle, outside of the left wheels lined
        //up with the outside edge closer to the build zone of the second square from the audience side
        holo.driveStraight_Inches(25, .8, this);
        holo.driveStrafe_Inches(-24,.8, this); //was 16 but added 8 for the sensor distance adjustment
        char colorSkystone = holo.getColor();
        int testNumber = 1;
        float totalStrafeDist_In = 76; //was 68 but added 8 for the sensor distance adjustment
        telemetry.addData("stone color = ", colorSkystone);
        telemetry.update();
        while (colorSkystone == 'y' & testNumber < 3)
        {
            holo.driveStrafe_Inches(8, .8, this);
            totalStrafeDist_In = totalStrafeDist_In - 8;
            colorSkystone = holo.getColor();
            testNumber = testNumber + 1;
            telemetry.addData("number of readings = ", testNumber);
            telemetry.addData("stone color 2 = ", colorSkystone);
            telemetry.update();

        }
        //grab skystone
        holo.driveStraight_Inches(-5,.8, this);
        holo.driveStrafe_Inches(totalStrafeDist_In, .8, this);
        //drop skystone
        //holo.driveStrafe_Inches(-(totalStrafeDist_In + 28), .8);
        //grab second skystone
        //holo.driveStrafe_Inches(totalStrafeDist_In + 20, .8);
        //drop skystone
        holo.driveStrafe_Inches(-48,.8, this);
        holo.drivePivot_Degrees(-175, .8, this);
        sleep(100);
        holo.kissWall(4,7,this);
        holo.driveStrafe_Inches(-36,.8, this);
        holo.stopAllMotors();
    }
}
