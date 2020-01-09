import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Disabled()
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
        holo.driveStraight_Inches(24.5f, .8, this);
        holo.driveStrafe_Inches(-21,.8, this); //was 16 but added 8 for the sensor distance adjustment
        char colorSkystone = holo.getColor();
        int testNumber = 1;
        float totalStrafeDist_In = 75f;
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
        float requiredDist_in = 4.5f;
        float intervalDistance = 7; //What is this supposed to do?
        holo.clamp(false);
        holo.kissWall(requiredDist_in, intervalDistance, this);
        holo.grabStone(this);
        holo.driveStraight_Inches(-2,.9, this);
        holo.driveStrafe_Inches(totalStrafeDist_In, .8, this);
        holo.dropStone(this);
        if (Math.abs(totalStrafeDist_In) <= 59)
        {
            //back up one stone so not off the edge
            totalStrafeDist_In += 8;
            requiredDist_in = requiredDist_in + 0.5f; //further away from stone for just end block
        }
        holo.driveStrafe_Inches(-(totalStrafeDist_In - 23f), .8, this);
        holo.clamp(false);
        holo.drivePivot_Degrees(6,.8,this);
        holo.kissWall(requiredDist_in, intervalDistance, this);
        holo.grabStone(this);
        holo.driveStraight_Inches(-5,.8, this);
        holo.drivePivot_Degrees(-175, .8, this);
        sleep(100);
        holo.kissWall(4,7,this);
        //holo.driveStrafe_Inches(-36,.8, this);
        holo.stopAllMotors();
    }
}
