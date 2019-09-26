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
        mecBot.driveStraight_Enc(500,.5f);
        while (!gamepad1.a && !isStopRequested())
        {

        }
        mecBot.driveStraight_Enc(650,.5f);
        while (!gamepad1.b && !isStopRequested())
        {

        }
        mecBot.driveStraight_Enc(850,.5f);
        while (!gamepad1.x && !isStopRequested())
        {

        }
        mecBot.driveStraight_Enc(1000,.5f);
        while (!gamepad1.y && !isStopRequested())
        {

        }
        mecBot.pivot_enc(25);//power is preset to 0.8 for pivots
        while (!gamepad1.a && !isStopRequested())
        {

        }
        mecBot.pivot_enc(50);
        while (!gamepad1.b && !isStopRequested())
        {

        }
        mecBot.pivot_enc(100);
        while (!gamepad1.x && !isStopRequested())
        {

        }
        mecBot.pivot_enc(200);
        while (!gamepad1.y && !isStopRequested())
        {

        }
        mecBot.strafe_enc(50); //power is preset for 0.8 for pivots
        while (!gamepad1.a && !isStopRequested())
        {

        }
        mecBot.strafe_enc(100);
        while (!gamepad1.b && !isStopRequested())
        {

        }
        mecBot.strafe_enc(250);
        while (!gamepad1.x && !isStopRequested())
        {

        }
        mecBot.strafe_enc(400);
        while (!gamepad1.y && !isStopRequested())
        {

        }
    }
}
