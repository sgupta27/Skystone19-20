import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "AutoRedLoad_V1")
public class AutoRedLoad_V1 extends LinearOpMode
{
    private MecBot holo;

    public void runOpMode()
    {
        holo = new MecBot(hardwareMap, this);
        holo.setLightsColor(RevBlinkinLedDriver.BlinkinPattern.RED);
        waitForStart();

        //red loading side, robot facing the cubes in the middle, outside of the left wheels lined
        //up with the outside edge closer to the build zone of the second square from the audience side
        holo.driveStraight_Inches(24.5f, .9, this);
        holo.driveStrafe_Inches(-21,.9, this); //was 16 but added 8 for the sensor distance adjustment
        char colorSkystone = holo.getColor();
        int testNumber = 1;
        float totalStrafeDist_In = -74;
        telemetry.addData("stone color = ", colorSkystone);
        telemetry.update();
        while (colorSkystone == 'y' & testNumber < 3)
        {
            holo.driveStrafe_Inches(-8, .9, this);
            totalStrafeDist_In = totalStrafeDist_In + 8;
            colorSkystone = holo.getColor();
            testNumber = testNumber + 1;
            telemetry.addData("number of readings = ", testNumber);
            telemetry.addData("stone color 2 = ", colorSkystone);
            telemetry.update();
        }
        float requiredDist_in = 4.5f;
        float intervalDistance = 7; //What is this supposed to do?
        holo.clamp(false);
        holo.kissWall(requiredDist_in, intervalDistance, this);
        holo.grabStone(this);
        /*holo.driveStraight_Inches(-5,.9, this);
        holo.driveStrafe_Inches(totalStrafeDist_In, .9, this);
        holo.dropStone(this);
        holo.driveStrafe_Inches(-(totalStrafeDist_In + 28), .9, this);
        holo.grabStone(this);
        holo.driveStrafe_Inches(totalStrafeDist_In + 20, .9, this);
        holo.dropStone(this);
        holo.driveStrafe_Inches(-10,.9, this);
        holo.driveStraight_Inches(4,.9, this);*/
        holo.stopAllMotors();
    }
}
