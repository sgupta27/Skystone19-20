package org.firstinspires.ftc.teamcode.TestPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.VuforiaFunctions;

import java.util.ArrayList;
import java.util.List;

@Autonomous(name = "TensorFlowDecisionTree")
public class TensorFlowDecisionTree extends LinearOpMode
{
    @Override
    public void runOpMode() throws InterruptedException
    {
        VuforiaFunctions vuforiaFunctions = new VuforiaFunctions(this, hardwareMap);
        waitForStart();

        while (opModeIsActive())
        {
/*
            ArrayList<Recognition> closest = vuforiaFunctions.getTwoClosestRecognitions();
            i    {
                        telemetry.addData(temp.toString(), null);
                f(closest != null)
            {
                if ( closest.size() != 0)
                {
                    for (Recognition temp : closest)
                        telemetry.addData("New line", null);
                    }
                }
            }
            */
            telemetry.addData("Pos: ", vuforiaFunctions.getPositionOfGoldInTwoObjects());
            telemetry.addData("Closest Rec: ", vuforiaFunctions.getOneClosestRecognition());
            telemetry.update();
        }
    }
}
