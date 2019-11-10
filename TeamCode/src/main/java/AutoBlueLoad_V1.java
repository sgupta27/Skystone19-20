import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;

@Autonomous(name = "AutoBlueLoad_V1")
public class AutoBlueLoad_V1 extends LinearOpMode
{
    private MecBot holo;

    public void runOpMode()
    {
        holo = new MecBot(hardwareMap, this);
        waitForStart();

        //blue loading side, robot facing the cubes in the middle, outside of the left wheels lined
        //up with the outside edge closer to the build zone of the second square from the audience side
        holo.driveStraight_Inches(25, .8);
        telemetry.addData("starting looking for color", null);
        telemetry.update();
        sleep(3000);
        char colorSkystone = holo.getColor(holo.getFrontColorSens);
        telemetry.addData("stone color = ", colorSkystone);
        telemetry.update();
        //check for color of cubes and get correct cube
        /*holo.driveStraight_Inches(-5, .8);
        holo.driveStrafe_Inches(-52, .8);
        //drop off cube
        holo.driveStrafe_Inches(16, .8);*/
    }
}
