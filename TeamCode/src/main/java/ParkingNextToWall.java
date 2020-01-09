import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Disabled()
public class ParkingNextToWall extends LinearOpMode
{
    private MecBot holo;
    MecBot.Result result = MecBot.Result.Right;

    public void runOpMode()
    {
        holo = new MecBot(hardwareMap, this);
        waitForStart();
        float requiredDist_in = 4;
        float intervalDistance = 7; //What is this supposed to do?
        telemetry.addData("distanceFromWall: ", holo.getFrontDistance_IN());
        telemetry.addData("Required Distance: ", requiredDist_in);
        telemetry.update();
        holo.kissWall(requiredDist_in, intervalDistance, this);
        sleep(3000);
    }
}
