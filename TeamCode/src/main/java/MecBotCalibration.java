import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

@TeleOp()
public class MecBotCalibration extends LinearOpMode
{
    MecBot mecBot;

    public void runOpMode() throws InterruptedException
    {
        mecBot = new MecBot(hardwareMap, this);
        waitForStart();
        /*mecBot.driveStraight_Enc(150,.8f);

        while (!gamepad1.a && !isStopRequested())
        {

        }
        mecBot.driveStraight_Enc(300,.8f);
        while (!gamepad1.b && !isStopRequested())
        {

        }
        mecBot.driveStraight_Enc(750,.8f);
        while (!gamepad1.x && !isStopRequested())
        {

        }
        mecBot.driveStraight_Enc(1000,.8f);
        while (!gamepad1.y && !isStopRequested())
        {

        }
        */
        mecBot.pivot_enc(150);//power is preset to 0.8 for pivots
        while (!gamepad1.a && !isStopRequested())
        {

        }
        mecBot.pivot_enc(300);
        while (!gamepad1.b && !isStopRequested())
        {

        }
        mecBot.pivot_enc(600);
        while (!gamepad1.x && !isStopRequested())
        {

        }
        mecBot.pivot_enc(1200);
        while (!gamepad1.y && !isStopRequested())
        {

        }
        /*
        mecBot.strafe_enc(500);//power is preset for 0.8 for strafes
        while (!gamepad1.a && !isStopRequested())
        {

        }
        mecBot.strafe_enc(1000);
        while (!gamepad1.b && !isStopRequested())
        {

        }
        mecBot.strafe_enc(1500);
        while (!gamepad1.x && !isStopRequested())
        {

        }
        mecBot.strafe_enc(2000);
        while (!gamepad1.y && !isStopRequested())
        {

        }*/
    }
}
