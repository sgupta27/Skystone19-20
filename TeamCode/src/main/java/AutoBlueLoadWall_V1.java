import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;

@Disabled()
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
        holo.driveStraight_Inches(26, .8, this);
        sleep(100);
        holo.driveStrafe_Inches(19.5f,.8, this);
        char colorSkystone = holo.getColor();
        int testNumber = 1;
        float totalStrafeDist_In = -74.5f;
        telemetry.addData("stone color = ", colorSkystone);
        telemetry.update();
        while (colorSkystone == 'y' & testNumber < 3)
        {
            holo.driveStrafe_Inches(-8, .8, this);
            totalStrafeDist_In = totalStrafeDist_In + 8;
            colorSkystone = holo.getColor();
            testNumber = testNumber + 1;
            telemetry.addData("number of readings = ", testNumber);
            telemetry.addData("stone color 2 = ", colorSkystone);
            telemetry.update();
        }
        float requiredDist_in = 4.75f; //4.5
        float intervalDistance = 7; //What is this supposed to do?
        holo.clamp(false);
        holo.kissWall(requiredDist_in, intervalDistance, this);
        holo.drivePivot_Degrees(6,.4,this);
        holo.grabStone(this);
        holo.driveStraight_Inches(-5,.9, this);
        holo.driveStrafe_Inches(totalStrafeDist_In, .9, this);
        holo.dropStone(this);
        if (Math.abs(totalStrafeDist_In) <= 59)
        {
            // back up one stone so not off end
            totalStrafeDist_In -= 8;
        }
        holo.driveStrafe_Inches(-(totalStrafeDist_In + 25.5f), .8, this);
        holo.clamp(false);
        holo.kissWall(requiredDist_in, intervalDistance, this);
        holo.grabStone(this);
        holo.driveStraight_Inches(-6,.8, this);
        holo.drivePivot_Degrees(175,.9, this);
        sleep(100);
        holo.kissWall(4,7,this);
       //holo.driveStrafe_Inches(36,.8, this);
        holo.stopAllMotors();
    }
}
