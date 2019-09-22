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
        mecBot.driveStraight_Enc(50,.5f);
        while (!gamepad1.a && !isStopRequested())
        {

        }
        mecBot.driveStraight_Enc(100,.5f);
        while (!gamepad1.b && !isStopRequested())
        {

        }
        mecBot.driveStraight_Enc(250,.5f);
        while (!gamepad1.x && !isStopRequested())
        {

        }
        mecBot.driveStraight_Enc(400,.5f);
        while (!gamepad1.y && !isStopRequested())
        {

        }
        /*mecBot.driveStraight_Enc(500,.8f);
        while (!gamepad1.a && !isStopRequested())
        {

        }
        mecBot.driveStraight_Enc(750,.8f);
        while (!gamepad1.b && !isStopRequested())
        {

        }
        mecBot.driveStraight_Enc(850,.8f);
        while (!gamepad1.x && !isStopRequested())
        {

        }
        mecBot.driveStraight_Enc(1000,.8f);
        while (!gamepad1.y && !isStopRequested())
        {

        }*/
    }
}
