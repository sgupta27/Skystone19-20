import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

@Autonomous (name = "MecBotCalibration")
public class MecBotCalibration extends LinearOpMode
{
    MecBot mecBot;

    public void runOpMode() throws InterruptedException
    {
        mecBot = new MecBot(hardwareMap, this);
        waitForStart();
        mecBot.driveStraight_Enc(50,.8f);

        while (!gamepad1.a && !isStopRequested())
        {

        }
        mecBot.driveStraight_Enc(100,.8f);
        while (!gamepad1.b && !isStopRequested())
        {

        }
        mecBot.driveStraight_Enc(250,.8f);
        while (!gamepad1.x && !isStopRequested())
        {

        }
        mecBot.driveStraight_Enc(400,.8f);
        while (!gamepad1.y && !isStopRequested())
        {

        }
        /*
        mecBot.pivot_enc(-600);//power is preset to 0.8 for pivots
        while (!gamepad1.a && !isStopRequested())
        {

        }
        mecBot.pivot_enc(-1000);
        while (!gamepad1.b && !isStopRequested())
        {

        }
        mecBot.pivot_enc(-1500);
        while (!gamepad1.x && !isStopRequested())
        {

        }
        mecBot.pivot_enc(-2000);
        while (!gamepad1.y && !isStopRequested())*/
        {

        }
        /*mecBot.strafe_enc(-1200); //power is preset for 0.8 for pivots
        while (!gamepad1.a && !isStopRequested())
        {

        }
        mecBot.strafe_enc(-1600);
        while (!gamepad1.b && !isStopRequested())
        {

        }
        mecBot.strafe_enc(-500);
        while (!gamepad1.x && !isStopRequested())
        {

        }
        mecBot.strafe_enc(-800);
        while (!gamepad1.y && !isStopRequested())*/
        {

        }
    }
}
