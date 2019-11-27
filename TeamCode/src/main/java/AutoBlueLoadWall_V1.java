import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;

@Autonomous(name = "AutoBlueLoadWall_V1")
public class AutoBlueLoadWall_V1 extends LinearOpMode
{
    private MecBot holo;

    public void runOpMode()
    {
        holo = new MecBot(hardwareMap,this);
        holo.setLightsColor(RevBlinkinLedDriver.BlinkinPattern.SKY_BLUE);
        waitForStart();

        //blue loading side, robot facing the cubes in the middle, outside of the right wheels lined
        //up with the outside edge closer to the build zone of the second square from the audience side
        holo.driveStraight_Inches(25, .8);
        holo.driveStrafe_Inches(16,.8);
        char colorSkystone = holo.getColor();
        int testNumber = 1;
        float totalStrafeDist_In = -68;
        telemetry.addData("stone color = ", colorSkystone);
        telemetry.update();
        while (colorSkystone == 'y' & testNumber < 3)
        {
            holo.driveStrafe_Inches(-8, .8);
            totalStrafeDist_In = totalStrafeDist_In + 8;
            colorSkystone = holo.getColor();
            testNumber = testNumber + 1;
            telemetry.addData("number of readings = ", testNumber);
            telemetry.addData("stone color 2 = ", colorSkystone);
            telemetry.update();

        }
        //grab skystone
        holo.driveStraight_Inches(-5,.8);
        holo.driveStrafe_Inches(totalStrafeDist_In, .8);
        //drop skystone
        //holo.driveStrafe_Inches(-(totalStrafeDist_In + 28), .8);
        //grab second skystone
        //holo.driveStrafe_Inches(totalStrafeDist_In + 20, .8);
        //drop skystone
        holo.driveStrafe_Inches(48,.8);
        holo.drivePivot_Degrees(175, .8);
        sleep(100);
        holo.kissWall(4,7,this);
        holo.driveStrafe_Inches(36,.8);
        holo.stopAllMotors();
    }
}
