import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "AutoLoad_V1")
public class AutoLoad_V1 extends LinearOpMode
{
    private MecBot mecBot;

    public void runOpMode()
    {
        mecBot = new MecBot(hardwareMap, this);
        waitForStart();
    }
}
