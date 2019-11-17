import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "MecTestWaitRobot")
public class MecTestWaitRobot extends LinearOpMode
{
    private MecBot holo;
    MecBot.Result result = MecBot.Result.Right;

    public void runOpMode() {
        holo = new MecBot(hardwareMap, this);
        holo.resetRunTime();
        waitForStart();
        double maxLookDistance_in = 12;
        long timeToCheck_ms = 50;
        double maxWait_s = 8;
        boolean shiftLeft = false;
        double distance = holo.getFrontDistance_IN();
        telemetry.addData("maxLookDistance_in: ", maxLookDistance_in);
        telemetry.addData("maxWait_ms: ", maxWait_s);
        telemetry.addData("timeToCheck_ms: ", timeToCheck_ms);
        telemetry.addData("sensorDistance: ", distance);
        telemetry.addData("Shifting Left: ", shiftLeft);
        telemetry.addData("Time Passed: ", holo.getRunTime());
        telemetry.addData("Outside: ", true);
        telemetry.update();
        result = holo.wait_for_robot(maxLookDistance_in, timeToCheck_ms, maxWait_s, shiftLeft, this);
    }
}
