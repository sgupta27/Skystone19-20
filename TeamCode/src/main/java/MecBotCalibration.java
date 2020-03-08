import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

@TeleOp(name = "MecBotCalibration")
public class MecBotCalibration extends LinearOpMode
{
    MecBot mecBot;

    public void runOpMode() throws InterruptedException
    {
        mecBot = new MecBot(hardwareMap, this);
        waitForStart();
        /*mecBot.driveStraight_Enc(1500,.8f);

        while (!gamepad1.a && !isStopRequested())
        {

        }
        if (isStopRequested())
        {
            mecBot.stopAllMotors();
            mecBot.stopDriveMotors();
        }
        mecBot.driveStraight_Enc(2000,.8f);
        while (!gamepad1.b && !isStopRequested())
        {

        }
        if (isStopRequested())
        {
            mecBot.stopAllMotors();
            mecBot.stopDriveMotors();
        }
        mecBot.driveStraight_Enc(2500,.8f);
        while (!gamepad1.x && !isStopRequested())
        {

        }
        if (isStopRequested())
        {
            mecBot.stopAllMotors();
            mecBot.stopDriveMotors();
        }
        mecBot.driveStraight_Enc(1000,.8f);
        while (!gamepad1.y && !isStopRequested())
        {

        }
        if (isStopRequested())
        {
            mecBot.stopAllMotors();
            mecBot.stopDriveMotors();
        }*/

        mecBot.pivot_enc(-150);//power is preset to 0.8 for pivots
        while (!gamepad1.a && !isStopRequested())
        {

        }
        if (isStopRequested())
        {
            mecBot.stopAllMotors();
            mecBot.stopDriveMotors();
        }
        mecBot.pivot_enc(-300);
        while (!gamepad1.b && !isStopRequested())
        {

        }
        if (isStopRequested())
        {
            mecBot.stopAllMotors();
            mecBot.stopDriveMotors();
        }
        mecBot.pivot_enc(-600);
        while (!gamepad1.x && !isStopRequested())
        {

        }
        if (isStopRequested())
        {
            mecBot.stopAllMotors();
            mecBot.stopDriveMotors();
        }
        mecBot.pivot_enc(-1200);
        while (!gamepad1.y && !isStopRequested())
        {

        }
        if (isStopRequested())
        {
            mecBot.stopAllMotors();
            mecBot.stopDriveMotors();
        }
        /*mecBot.strafe_enc(-500);//power is preset for 0.8 for strafes
        while (!gamepad1.a && !isStopRequested())
        {

        }
        if (isStopRequested())
        {
            mecBot.stopAllMotors();
            mecBot.stopDriveMotors();
        }
        mecBot.strafe_enc(-1000);
        while (!gamepad1.b && !isStopRequested())
        {

        }
        if (isStopRequested())
        {
            mecBot.stopAllMotors();
            mecBot.stopDriveMotors();
        }
        mecBot.strafe_enc(-1500);
        while (!gamepad1.x && !isStopRequested())
        {

        }
        if (isStopRequested())
        {
            mecBot.stopAllMotors();
            mecBot.stopDriveMotors();
        }
        mecBot.strafe_enc(-2000);
        while (!gamepad1.y && !isStopRequested())
        {

        }
        if (isStopRequested())
        {
            mecBot.stopAllMotors();
            mecBot.stopDriveMotors();
        }*/
    }
}
