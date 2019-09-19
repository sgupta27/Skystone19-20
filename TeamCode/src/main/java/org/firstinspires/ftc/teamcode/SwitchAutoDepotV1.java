package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name = "SwitchAutoDepotV1")
public class SwitchAutoDepotV1 extends LinearOpMode
{
    CompRobot compRobot;
    VuforiaFunctions vuforiaFunctions;
    private float rightSensorDepth = .5f;
    private float frontSensorDepth = 2;

    public void runOpMode()
    {
        compRobot = new CompRobot(hardwareMap, this);
        vuforiaFunctions = new VuforiaFunctions(this, hardwareMap);
        char pos = '?';
        boolean isCenterGold = false;
        boolean isRightGold = false;
        boolean seenAtHang = false;
        boolean seenAtBott = false;
        boolean switchDepot = compRobot.getSwitchDepot();
        boolean switchCrater = compRobot.getSwitchCrater();
        waitForStart();
        sleep(100);

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
            compRobot.driveStraight(5,.6f);
            sleep(250);
            compRobot.pivotenc(-15f,.8f);
            sleep(800);
            compRobot.pivotenc(15f,.8f);
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
        compRobot.driveStraight(5,.8f);
        compRobot.samplerDown();
        switch (pos)
        {
            case 'l':
            case '1':
                compRobot.driveStraight(4, .7f);
                compRobot.pivotenc(35, .5f);
                compRobot.driveStraight(28, .7f);
                compRobot.pivotenc(-90, .5f);
                compRobot.driveStraight(12, .7f);
                break;
            case 'r':
                compRobot.driveStraight(4, .7f);
                compRobot.pivotenc(-50, .5f);
                compRobot.driveStraight(28, .7f);
                compRobot.pivotenc(75, .5f);
                compRobot.driveStraight(16, .7f);
                break;
            default:
                compRobot.driveStraight(16, .8f);
                while (compRobot.getFrontDistSens().getDistance(DistanceUnit.INCH) > 18 && compRobot.getFrontRightDistSens().getDistance(DistanceUnit.INCH) > 18)
                {
                    compRobot.driveMotors(.4f, .4f);
                }
                compRobot.samplerUp();
                compRobot.stopDriveMotors();
                break;
        }

        if (switchDepot)
        {
                while (compRobot.getFrontDistSens().getDistance(DistanceUnit.INCH) > 18 && compRobot.getFrontRightDistSens().getDistance(DistanceUnit.INCH) > 18)
                {
                    compRobot.driveMotors(.4f, .4f);
                }
                compRobot.stopDriveMotors();
                compRobot.deployMarker();
                switch (pos)
                {
                    case 'r':
                    {
                        compRobot.driveStraight(-16, .8f);
                        compRobot.pivotenc(-90,.6f);
                        compRobot.driveStraight(-5,.8f);
                        compRobot.stopDriveMotors();
                        break;
                    }
                    default:
                    {
                        break;
                    }
                }
                if (!switchCrater)
                {
                    compRobot.driveStraight(-30,.8f);
                }
        }

        if (switchCrater)
        {
                    if (!switchDepot)
                    {
                        compRobot.driveStraight(15, .8f); //adjust after sampling is made
                        while (compRobot.getFrontDistSens().getDistance(DistanceUnit.INCH) > 20 && compRobot.getFrontRightDistSens().getDistance(DistanceUnit.INCH) > 20)
                        {
                            compRobot.driveMotors(.4f, .4f);
                        }
                        compRobot.stopDriveMotors();
                    }
                    compRobot.driveStraight(-5, 1);
                    compRobot.pivotenc(155, .5f);

                    compRobot.driveStraight(16, .6f);

                    compRobot.pivotenc(-25, .5f); //9 works but its far from the wall

                    sleep(250);
                //hugwall stil is struggling to turn back!! fix this and figure out why the 25 degree turn isn't 25 degrees
                compRobot.hugWallToRight(3 + rightSensorDepth, 5 + rightSensorDepth, 22, 48);
                compRobot.driveStraight(24, .8f);
                telemetry.addData("Stopped", null);
                telemetry.update();

                compRobot.stopDriveMotors();
        }
    }
}
