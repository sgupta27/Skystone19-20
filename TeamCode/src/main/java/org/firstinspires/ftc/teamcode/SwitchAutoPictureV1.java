package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sahithi Thumuluri on 12/24/18.
 */

@Autonomous(name = "SwitchAutoPictureV1")
public class SwitchAutoPictureV1 extends LinearOpMode
{
    CompRobot compRobot;
    VuforiaFunctions vuforiaFunctions;

    public void runOpMode()
    {
        compRobot = new CompRobot(hardwareMap, this);
        vuforiaFunctions = new VuforiaFunctions(this, hardwareMap);
        float yawAngle = 90;
        float frontSensorDepth = 2;
        float rightSensorDepth = 2;
        float  yawAngleTurn;
        float distanceTraveled = 0;
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
            if (vuforiaFunctions.getOneClosestRecognition().getLabel().equals(vuforiaFunctions.LABEL_GOLD_MINERAL))
            {
                isCenterGold = true;
                pos =   'c';
            }
        }
        telemetry.addData("Pos: ", pos);
        telemetry.update();

        compRobot.climbDown();

        if (switchDepot) //gonna need to edit these values when testing
//            if (switchDepot) //gonna need to edit these values when testing
        {
            compRobot.driveStraight(8, .8f);
            compRobot.pivotenc(90, .8f); //100 worked about 2/3 of the time

            while (true)
            {
                double frontDist = compRobot.getFrontDistance_IN();
                if (frontDist <= 7 + frontSensorDepth)
                    break;
                else
                    compRobot.driveStraight(10, .5f);
                distanceTraveled = distanceTraveled + 10;
                if (distanceTraveled >= 40)
                {
                    break;
                }
            }
            compRobot.stopDriveMotors();
            {
                if (vuforiaFunctions.hasSeenTarget())
                {
                    telemetry.addData(vuforiaFunctions.getCurrentNameOfTargetSeen(), null);
                    telemetry.addData("X (in): ", vuforiaFunctions.getXPosIn());
                    telemetry.addData("Y (in): ", vuforiaFunctions.getYPosIn());
                    telemetry.addData("X (ft): ", vuforiaFunctions.getXPosIn() / 12f);
                    telemetry.addData("Y (ft): ", vuforiaFunctions.getYPosIn() / 12f);
                    telemetry.addData("YAW ", vuforiaFunctions.getYawDeg());
                    telemetry.update();
                    sleep(100);
                    yawAngle = vuforiaFunctions.getYawDeg();
                    if (yawAngle < 0)
                    {
                        yawAngle = 180 + yawAngle;
                    }
                    yawAngleTurn = 100 - yawAngle;
                    compRobot.pivotenc(yawAngleTurn, .8f);
                } else
                {
                    telemetry.addData("Such target is not in my sight!", null);
                    compRobot.pivotenc(45, .8f);
                }

                telemetry.update();
            }
            compRobot.hugWallToRight(3 + rightSensorDepth, 7 + rightSensorDepth, 22, 48);
            telemetry.addData("Stopped", null);
            sleep(100);
            compRobot.deployMarker();
            telemetry.update();
        }

        if (switchCrater)
        {
            if (!switchDepot)
            {
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
                                pos =   'r';
                            } else
                                isRightGold = false;
                        }
                        else if (vuforiaFunctions.getTfod().getRecognitions().size() >= 2)
                        {
                            //there might be an issue in here if it sees more than two objects (like ones in the background
                            //you may need to have it only look at the one closest object but idk, this is pretty tough
                            telemetry.addData("See 2", null);
                            pos = vuforiaFunctions.getPositionOfGoldInTwoObjects();
                        }
                        else
                            telemetry.addData("See none", null);
                    }
                    else
                        telemetry.addData("See nothing", null);
                }

                if(seenAtBott && seenAtHang && pos == '?')
                {
                    if (!isRightGold && !isCenterGold)
                        pos =  'l';
                }
                switch (pos)
                {
                    case 'l':
                    case '1':
                        compRobot.driveStraight(4, .7f);
                        compRobot.pivotenc(35, .5f);
                        compRobot.driveStraight(24, .7f);
                        compRobot.pivotenc(-90, .5f);
                        compRobot.driveStraight(16, .7f);
                        break;
                    case 'r':
                        compRobot.driveStraight(4, .7f);
                        compRobot.pivotenc(-50, .5f);
                        compRobot.driveStraight(24, .7f);
                        compRobot.pivotenc(75, .5f);
                        compRobot.driveStraight(16, .7f);
                        break;
                    default:
                        compRobot.driveStraight(35, .8f);
                        compRobot.stopDriveMotors();
                        break;
                }
                compRobot.samplerUp();
            }
            else
            {
                compRobot.driveStraight(-20, .5f);
                compRobot.pivotenc(-270, .8f);
                compRobot.hugWallToLeft(3 + rightSensorDepth, 7 + rightSensorDepth, 22, 65);
                compRobot.stopDriveMotors();
            }
        }

        compRobot.stopDriveMotors();
    }

    public char getPosNewRecognition()
    {
        //To Sahithi
        // vuforiaFunctions.getPositionOfGoldInTwoObjects() now checks to see if both objects (the two) are both golds
        // keep in mind that it only looks at the two closest objects so if they both are gold, it'll pos =  '?' so i added
        //stuff for that too

        char pos = '?';
        boolean isCenterGold = false;
        boolean isRightGold = false;
        boolean seenAtHang = false;
        boolean seenAtBott = false;

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
            //pos = vuforiaFunctions.getPositionOfGoldInTwoObjects();
            // commented out b/c it might see a gold and a silver as two closest objects, and it'll give out a bad pos

            //if(pos == '?') //we changed getPositonOfGoldInTwoObjects to pos =  '?' if there are multiple golds.
            //{
            if (vuforiaFunctions.getOneClosestRecognition().getLabel().equals(vuforiaFunctions.LABEL_GOLD_MINERAL))
            {
                isCenterGold = true;
                pos =   'c';
            }
            //}
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
                        pos =   'r';
                    } else
                        isRightGold = false;
                }
                else if (vuforiaFunctions.getTfod().getRecognitions().size() >= 2)
                {
                    //there might be an issue in here if it sees more than two objects (like ones in the background
                    //you may need to have it only look at the one closest object but idk, this is pretty tough
                    telemetry.addData("See 2", null);
                    pos = vuforiaFunctions.getPositionOfGoldInTwoObjects();
                }
                else
                    telemetry.addData("See none", null);
            }
            else
                telemetry.addData("See nothing", null);
        }

        if(seenAtBott && seenAtHang && pos == '?')
        {
            if (!isRightGold && !isCenterGold)
                pos =  'l';
        }

        return pos;
    }
}
