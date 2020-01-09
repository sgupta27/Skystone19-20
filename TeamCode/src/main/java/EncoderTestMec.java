import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Disabled()
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
