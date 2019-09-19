package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
@Disabled
@Autonomous(name = "!RedFinalPictureFirst")
public class AutonomousRedFinalPictureFirst extends LinearOpMode
{
    CompRobot compRobot;
    VuforiaFunctions vuforiaFunctions;

    public void runOpMode()
    {
        compRobot = new CompRobot(hardwareMap,this);
        vuforiaFunctions = new VuforiaFunctions(this, hardwareMap);
        float yawAngle = 90;
        float frontSensorDepth = 4.5f;
        float rightSensorDepth = 2;
        float leftSensorDepth = 2;
        float backSensorDepth = 2;
        float  yawAngleTurn;
        float distanceTraveled = 0;
        waitForStart();
        sleep(5000);
        //compRobot.climbDown();
        //sleep(200);
        compRobot.driveStraight(8,.8f);
        compRobot.pivotenc(95, .8f); //100 worked about 2/3 of the time

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
                sleep(100);
                yawAngle = vuforiaFunctions.getYawDeg();
                yawAngleTurn = 100 - yawAngle;
                compRobot.pivotenc(yawAngleTurn, .8f);
            }
            else
            {
                telemetry.addData("Such target is not in my sight!", null);
                compRobot.pivotenc(60, .8f);
            }

            telemetry.update();
        }
        compRobot.hugWallToRight(4 + rightSensorDepth, 8 + rightSensorDepth, 22, 48);
        //The hug wall code in the method is a bit different than the one that was in the original auto file
        //make sure that it still runs as intended.

        telemetry.addData("Stopped", null);
        compRobot.deployMarker();
        telemetry.update();
        compRobot.driveStraight(-20, .5f);
        compRobot.pivotenc(-230, .8f);
        compRobot.hugWallToLeft(6 + rightSensorDepth, 11 + rightSensorDepth, 38, 75);
        compRobot.driveStraight(15, .8f); //since the hugwall stops at the crater, this takes robot into crater
        compRobot.stopDriveMotors();
    }
}