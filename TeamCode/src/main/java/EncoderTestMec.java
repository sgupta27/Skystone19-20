import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "EncoderTestMec")
public class EncoderTestMec extends LinearOpMode
{
    MecBot mecbot;

    public void runOpMode() throws InterruptedException
    {
        mecbot = new MecBot(hardwareMap, this);
        mecbot.resetDriveEncoders();
        waitForStart();

        while (!isStopRequested())
        {
            mecbot.checkEncTest();
        }
    }
}
