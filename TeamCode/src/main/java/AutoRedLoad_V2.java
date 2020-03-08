import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "AutoRedLoad_V2")
public class AutoRedLoad_V2 extends LinearOpMode
{
    private MecBot holo;

    public void runOpMode()
    {
        holo = new MecBot(hardwareMap, this);
        holo.setLightsColor(RevBlinkinLedDriver.BlinkinPattern.RED);
        waitForStart();
        //competition ready for States

        //red loading side, robot facing the cubes in the middle, outside of the left wheels lined
        //up with the outside edge closer to the build zone of the second square from the audience side
        holo.driveStraight_Inches(26, .8, this);
        sleep(100);
        holo.driveStrafe_Inches(-2,.5, this); //was 16 but added 8 for the sensor distance adjustment
        char colorSkystone = holo.getColor();
        int testNumber = 1;
        float totalStrafeDist_In = 60;
        boolean isFirstBlock = true;
        telemetry.addData("stone color = ", colorSkystone);
        telemetry.update();
        while (colorSkystone == 'y' & testNumber < 3)
        {
            holo.driveStrafe_Inches(-8, .5, this);
            totalStrafeDist_In = totalStrafeDist_In + 8;
            colorSkystone = holo.getColor();
            testNumber = testNumber + 1;
            isFirstBlock = false;
            telemetry.addData("number of readings = ", testNumber);
            telemetry.addData("stone color 2 = ", colorSkystone);
            telemetry.update();
        }
        float requiredDist_in = 4.75f;
        float intervalDistance = 7;
        holo.clamp(false);
        holo.kissWall(requiredDist_in, intervalDistance, this);
        holo.grabStone(this);
        holo.driveStraight_Inches(-3,.9,this);
        holo.driveStrafe_Inches(totalStrafeDist_In,.5, this);
        holo.dropStone(this);
        if (isFirstBlock)
        {
            totalStrafeDist_In += 8;
            requiredDist_in = requiredDist_in + 0.75f; //further away from stone for just end block
        }
        holo.driveStrafe_Inches(-((totalStrafeDist_In - 24f)), .5, this);
        holo.clamp(false);
        holo.kissWall(requiredDist_in, intervalDistance, this);
        holo.grabStone(this);
        holo.driveStraight_Inches(-5,.8, this);
        holo.drivePivot_Degrees(-90,.9, this);
        holo.driveStrafe_Inches(-3,.9,this);
        holo.driveStraight_Inches(totalStrafeDist_In - 23, .9, this);
        holo.driveStraight_Inches(-15,.9, this);
        while (opModeIsActive() && !isStopRequested())
        {
            holo.stopDriveMotors();
            holo.stopAllMotors();
        }
    }
}
