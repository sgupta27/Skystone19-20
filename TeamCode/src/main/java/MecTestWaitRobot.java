import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import static java.lang.Thread.sleep;

@TeleOp(name = "MecTestWaitRobot")
public class MecTestWaitRobot extends OpMode
{
    private MecBot holo;
    private float rValue, lValue;
    private boolean toggleSpeedMode = false;
    private boolean hasSpeedModeBeenActivated = false;
    MecBot.Result result = MecBot.Result.Right;


    public void init()
    {
        holo = new MecBot(hardwareMap);
        gamepad1.setJoystickDeadzone(.25f);
        gamepad2.setJoystickDeadzone(.25f);
        holo.resetRunTime();
    }

    public void loop()
    {
        double maxLookDistance_in = 12;
        long timeToCheck_ms = 50;
        int maxWait_ms = 5000;
        boolean shiftLeft = true;
        double distance = holo.getFrontDistance_IN();
        double time = holo.getRunTime();
        telemetry.addData("maxLookDistance_in: ", maxLookDistance_in);
        telemetry.addData("maxWait_ms: ", maxWait_ms);
        //telemetry.addData("timeToCheck_ms: ", timeToCheck_ms);
        telemetry.addData("sensorDistance: ", distance);
        telemetry.addData("Shifting Left: ", shiftLeft);
        telemetry.addData("Time Passed: ", holo.getRunTime());
        telemetry.addData("Outside: ", true);
        telemetry.addData("Output: ", result);
        telemetry.update();
        result = holo.wait_for_robot(maxLookDistance_in, timeToCheck_ms, maxWait_ms, shiftLeft);
    }
}
