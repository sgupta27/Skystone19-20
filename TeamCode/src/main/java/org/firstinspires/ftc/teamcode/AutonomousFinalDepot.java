package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Disabled
@Autonomous(name = "!FinalDepot")
public class AutonomousFinalDepot extends LinearOpMode
{
    private CompRobot compRobot;
    VuforiaFunctions vuforiaFunctions;
    private float rightSensorDepth = .5f;

    public void runOpMode()
    {
        char pos = '?';
        boolean isCenterGold = false;
        boolean isRightGold = false;
        boolean seenAtHang = false;
        boolean seenAtBott = false;
        compRobot = new CompRobot(hardwareMap, this);
        vuforiaFunctions = new VuforiaFunctions(this, hardwareMap);
        waitForStart();
        sleep(1000);

        if(vuforiaFunctions.getTfod().getRecognitions().size() == 1)
        {
            telemetry.addData("SAW 1 OBJECT", null);
            seenAtHang = true;
            if(vuforiaFunctions.getTfod().getRecognitions().get(0).getLabel().equals(vuforiaFunctions.LABEL_GOLD_MINERAL))
            {
                telemetry.addData("SAW GOLD", null);
                isCenterGold = true;
                pos = 'c';
            }
            else
            {
                isCenterGold = false;
                telemetry.addData("Center no gold", null);
            }
        }
        else if(vuforiaFunctions.getTfod().getRecognitions().size() >= 2)
        {
            telemetry.addData("SAW 2", null);
            pos = vuforiaFunctions.getPositionOfGoldInTwoObjects();
        }
        telemetry.addData("Pos: ", pos);
        telemetry.update();

        compRobot.climbDown();

        if (pos == '?')
        {
            telemetry.addData("In ? Block", null);
            if(vuforiaFunctions.getTfod().getRecognitions() != null)
            {
                if (vuforiaFunctions.getTfod().getRecognitions().size() == 1)
                {
                    telemetry.addData("See 1 object", null);
                    seenAtBott = true;
                    if (vuforiaFunctions.getTfod().getRecognitions().get(0).getLabel().equals(vuforiaFunctions.LABEL_GOLD_MINERAL))
                    {
                        isRightGold = true;
                        pos = 'r';
                    } else
                        isRightGold = false;
                }
                else if (vuforiaFunctions.getTfod().getRecognitions().size() >= 2)
                {
                    telemetry.addData("See 2", null);
                    pos = vuforiaFunctions.getPositionOfGoldInTwoObjects();
                }
                else
                {
                    telemetry.addData("See none", null);
                }
            }
            else
                telemetry.addData("See nothing", null);
        }

        if(seenAtBott && seenAtHang && pos == '?')
        {
            if (!isRightGold && !isCenterGold)
                pos = 'l';
        }

        telemetry.addData("Pos: ", pos);
        telemetry.update();

        sleep(1000);
        switch (pos)
        {
            case 'l':
                compRobot.driveStraight(4, .7f);
                compRobot.pivotenc(35, .5f);
                compRobot.driveStraight(28, .7f);
                compRobot.pivotenc(-60, .5f);
                compRobot.driveStraight(20, .7f);
                break;
            case 'r':
                compRobot.driveStraight(4, .7f);
                compRobot.pivotenc(-35, .5f);
                compRobot.driveStraight(28, .7f);
                compRobot.pivotenc(60, .5f);
                compRobot.driveStraight(20, .7f);
                break;
            default:
                compRobot.driveStraight(25, .8f);
                while (compRobot.getFrontDistSens().getDistance(DistanceUnit.INCH) > 18 && compRobot.getFrontRightDistSens().getDistance(DistanceUnit.INCH) > 18)
                {
                    compRobot.driveMotors(.4f, .4f);
                }
                compRobot.stopDriveMotors();
        }
        //compRobot.deployMarker();
        compRobot.driveStraight(-5, 1);

        switch (pos)
        {
            case 'l':
                compRobot.pivotenc(165, .5f);
                break;
            case 'r':
                compRobot.pivotenc(80, .5f);
                compRobot.driveStraight(8, .5f);
                compRobot.pivotenc(50, .5f);
                break;
            default:
                compRobot.pivotenc(155, .5f);
                compRobot.driveStraight(16, .6f);
                compRobot.pivotenc(9, .5f);
        }
        compRobot.hugWallToRight(4 + rightSensorDepth, 8 + rightSensorDepth, 22, 60);
        compRobot.driveStraight(6, .8f);

        while (!isStopRequested())
            compRobot.stopDriveMotors();
    }
}



