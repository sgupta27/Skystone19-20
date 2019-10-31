import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "AutoDepot_V1")
public class AutoDepot_V1 extends LinearOpMode
{
    private MecBot mecBot;

    public void runOpMode()
    {
        mecBot = new MecBot(hardwareMap, this);
        waitForStart();
    }
}
