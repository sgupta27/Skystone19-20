import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
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
        holo.setLightsColor(RevBlinkinLedDriver.BlinkinPattern.SKY_BLUE);
        waitForStart();

        //blue loading side, robot facing the cubes in the middle, outside of the right wheels lined
        //up with the outside edge closer to the build zone of the second square from the audience side
        holo.driveStraight_Inches(26, .8);
        holo.driveStrafe_Inches(18,.8);
        char colorSkystone = holo.getColor();
        int testNumber = 1;
        float totalStrafeDist_In = -74;
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

        float requiredDist_in = 4.75f;
        float intervalDistance = 7; //What is this supposed to do?
        holo.setLightsColor(RevBlinkinLedDriver.BlinkinPattern.RED);
        holo.clamp(false);
        holo.setLightsColor(RevBlinkinLedDriver.BlinkinPattern.BLUE);
        holo.kissWall(requiredDist_in, intervalDistance, this);
        holo.setLightsColor(RevBlinkinLedDriver.BlinkinPattern.YELLOW);
        holo.grabStone(this);
        /*holo.driveStraight_Inches(-5,.8);
        holo.driveStrafe_Inches(totalStrafeDist_In, .8);
        holo.dropStone(this);
        holo.driveStrafe_Inches(-(totalStrafeDist_In + 25.5f), .8);
        holo.driveStraight_Inches(7.75f,.8);
        holo.grabStone(this);
        holo.driveStraight_Inches(-5,.8);
        holo.driveStrafe_Inches(totalStrafeDist_In + 22, .8);
        holo.dropStone(this);
        holo.driveStrafe_Inches(18,.8);
        holo.driveStraight_Inches(6,.8);*/
        holo.stopAllMotors();
    }
}
