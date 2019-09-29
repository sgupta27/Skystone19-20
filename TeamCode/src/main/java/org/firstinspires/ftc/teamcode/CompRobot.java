package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
/*public class CompRobot extends BasicBot
{
    private LinearOpMode linearOpMode;

    private DistanceSensor frontDistSens, rightDistSens, leftDistSens;
    private DcMotorImplEx collectorPivoterMotor, collectorLifterMotor, climberMotor;
    private Servo wristCollectorServo, samplingServo;
    private CRServo rightGrabCRServo, leftGrabCRServo;
    private DigitalChannel switchSample, switchDepot, switchCrater, switchDelay;
    //note: rightGrabCRServo is now used for paddles on water mill

    public CompRobot(HardwareMap hardwareMap)
    {
        super(hardwareMap);
        initMotorsAndMechParts(hardwareMap);
    }

    public CompRobot(HardwareMap hardwareMap, LinearOpMode linearOpMode_In)
    {
        super(hardwareMap);
        initSensors(hardwareMap);
        linearOpMode = linearOpMode_In;
        initMotorsAndMechParts(hardwareMap);
        initAutoSwitches(hardwareMap);
        //Uncomment above as soon as switches are on robot and are configured
    }

    private void initSensors(HardwareMap hardwareMap)
    {
        rightDistSens = hardwareMap.get(DistanceSensor.class, "rightDistSens");
        frontDistSens = hardwareMap.get(DistanceSensor.class, "frontDistSens");
        leftDistSens = hardwareMap.get(DistanceSensor.class, "leftDistSens");
    }

    //Inits the autonomous switches
    private void initAutoSwitches(HardwareMap hardwareMap)
    {
        switchSample = hardwareMap.digitalChannel.get("switchSample");
        switchSample.setMode(DigitalChannel.Mode.INPUT);
        switchDepot = hardwareMap.digitalChannel.get("switchDepot");
        switchDepot.setMode(DigitalChannel.Mode.INPUT);
        switchCrater = hardwareMap.digitalChannel.get("switchCrater");
        switchCrater.setMode(DigitalChannel.Mode.INPUT);
        switchDelay = hardwareMap.digitalChannel.get("switchSample");
        switchDelay.setMode(DigitalChannel.Mode.INPUT);

    }

    private void initMotorsAndMechParts(HardwareMap hardwareMap)
    {
        wristCollectorServo = hardwareMap.servo.get("wristCollectorServo");
        wristCollectorServo.setPosition(0.4);

        rightGrabCRServo = hardwareMap.crservo.get("rightGrabCRServo");
        rightGrabCRServo.setDirection(DcMotorSimple.Direction.REVERSE);

        leftGrabCRServo = hardwareMap.crservo.get("leftGrabCRServo");
        leftGrabCRServo.setDirection(DcMotorSimple.Direction.FORWARD);

        samplingServo = hardwareMap.servo.get("samplingServo");
        samplingServo.setDirection(Servo.Direction.REVERSE);
        samplerUp();

        initCRServoAndServoPos();

        collectorLifterMotor = hardwareMap.get(DcMotorImplEx.class, "collectorLifterMotor");
        collectorLifterMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        collectorLifterMotor.setMode(DcMotorImplEx.RunMode.STOP_AND_RESET_ENCODER);
        collectorLifterMotor.setMode(DcMotorImplEx.RunMode.RUN_USING_ENCODER);
        collectorLifterMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        collectorPivoterMotor = hardwareMap.get(DcMotorImplEx.class, "collectorPivoterMotor");
        collectorPivoterMotor.setDirection(DcMotorImplEx.Direction.FORWARD);
        collectorPivoterMotor.setMode(DcMotorImplEx.RunMode.STOP_AND_RESET_ENCODER);
        collectorPivoterMotor.setMode(DcMotorImplEx.RunMode.RUN_USING_ENCODER);
        collectorPivoterMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        climberMotor = hardwareMap.get(DcMotorImplEx.class, "climberMotor");
        climberMotor.setDirection(DcMotorImplEx.Direction.FORWARD);
        climberMotor.setMode(DcMotorImplEx.RunMode.STOP_AND_RESET_ENCODER);
        climberMotor.setMode(DcMotorImplEx.RunMode.RUN_USING_ENCODER);
        climberMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void driveStraight(float dist_In, float pow)
    {
        super.resetEncoders();
        if (dist_In > 0)
            super.goForward(pow, pow);
        else
            super.goBackwards(pow, pow);

        dist_In = Math.abs(dist_In);
        float encTarget = Math.abs(dist_In * 20.167f - 46.111f);

        while ((Math.abs(super.getDriveLeftOne().getCurrentPosition()) < encTarget)
                && (Math.abs(super.getDriveRightOne().getCurrentPosition()) < encTarget)
                && (!linearOpMode.isStopRequested()))
        {

        }
        super.stopDriveMotors();
    }

    public void pivotenc(float degrees, float pow)
    {
        pow = Math.abs(pow);
        super.resetEncoders();

        if (degrees > 0)
            super.driveMotors(-pow, pow);
        else
            super.driveMotors(pow, -pow);

        degrees = Math.abs(degrees);
        float encTarget;

        if (Math.abs(degrees) < 45)
            encTarget = 2.715f * degrees + .10386f;
        else
            encTarget = 2.3313f * degrees - 2.11769f;

        while (Math.abs(super.getDriveLeftOne().getCurrentPosition()) < encTarget && Math.abs(super.getDriveRightOne().getCurrentPosition()) < encTarget && !linearOpMode.isStopRequested())
        {

        }
        super.stopDriveMotors();
    }

    public DistanceSensor getFrontRightDistSens()
    {
        return rightDistSens;
    }

    public double getRightDistance_IN()
    {
        return rightDistSens.getDistance(DistanceUnit.INCH);
    }

    public DistanceSensor getLeftDistSens()
    {
        return leftDistSens;
    }

    public double getLeftDistance_IN()
    {
        return leftDistSens.getDistance(DistanceUnit.INCH);
    }

    public DistanceSensor getFrontDistSens()
    {
        return frontDistSens;
    }

    public double getFrontDistance_IN()
    {
        return frontDistSens.getDistance(DistanceUnit.INCH);
    }

    public void hugWallToRight(float tooCloseToSideWall, float tooFarFromSideWall,
                               float distAwayFromFrontWall, float maximumDistance)
    {
        double straightDistanceTraveled = 0;
        float stepDistance = 10;
        float stepPivotAmtDeg = 15;

        linearOpMode.telemetry.addData("in hug wall", null);
        linearOpMode.telemetry.addData("front dist= ", getFrontDistance_IN());
        linearOpMode.telemetry.update();

        while (!linearOpMode.isStopRequested())
        {
            linearOpMode.sleep(500);
            double straightDist = getFrontDistance_IN();
            if (straightDist <= distAwayFromFrontWall)
            {
                linearOpMode.telemetry.addData("Close Enough", null);
                linearOpMode.telemetry.addData("front dist= ", straightDist);
                linearOpMode.telemetry.update();
                break;
            }
            linearOpMode.telemetry.addData("Going forward 3", null);
            driveStraight(3, .8f);
            straightDistanceTraveled = straightDistanceTraveled + 3;
            linearOpMode.telemetry.addData("front Dist: ", straightDist);
            linearOpMode.telemetry.update();
            if (straightDist > distAwayFromFrontWall)
            {
                double rightDist = getRightDistance_IN();

                linearOpMode.telemetry.addData("front Dist>18", null);
                linearOpMode.telemetry.addData("right Dist ", rightDist);
                linearOpMode.telemetry.update();
                if (rightDist < tooCloseToSideWall)
                {
                    linearOpMode.telemetry.addData("too Close", rightDist);
                    linearOpMode.telemetry.update();
                    pivotenc(stepPivotAmtDeg, .5f);
                    driveStraight(stepDistance, .5f);
                    straightDistanceTraveled = straightDistanceTraveled + stepDistance;
                    pivotenc(-stepPivotAmtDeg, .5f);
                } else if (rightDist > tooFarFromSideWall && rightDist < 80)
                {
                    linearOpMode.telemetry.addData("too Far", rightDist);
                    linearOpMode.telemetry.update();
                    pivotenc(-stepPivotAmtDeg, .5f);
                    driveStraight(stepDistance, .5f);
                    straightDistanceTraveled = straightDistanceTraveled + stepDistance;
                    pivotenc(stepPivotAmtDeg, .5f);
                } else //need this null zone for logic, this is where it goes straight, do not comment out
                {
                    driveStraight(stepDistance, .8f);
                    straightDistanceTraveled = straightDistanceTraveled + stepDistance;
                }
                linearOpMode.telemetry.update();
                if (straightDistanceTraveled >= maximumDistance)
                {
                    break;
                }
            }
            linearOpMode.sleep(300);
        }
    }

    public void hugWallToLeft(float lowerDistFromSideWall, float upperDistFromSideWall, float distAwayFromFrontWall, float maximumDistance)
    {
        double straightDist, rightDist, leftDist;
        double straightDistanceTraveled = 0;
        float stepDistance = 10;
        float stepPivotAmtDeg = 15;

        DistanceSensor usingDistSensor = frontDistSens;

        while (usingDistSensor.getDistance(DistanceUnit.INCH) > distAwayFromFrontWall && !linearOpMode.isStopRequested())
        {
            linearOpMode.sleep(500);
            straightDist = usingDistSensor.getDistance(DistanceUnit.INCH);
            if (straightDist < distAwayFromFrontWall + Math.abs(stepDistance))
            {
                super.stopDriveMotors();
                break;
            }
            else
            {
                linearOpMode.telemetry.addData("Going forward 11", null);
                driveStraight(stepDistance, .8f);
                straightDistanceTraveled = straightDistanceTraveled + stepDistance;
            }
            linearOpMode.telemetry.addData("front Dist: ", straightDist);

            if (straightDist > distAwayFromFrontWall)
            {
                leftDist = getLeftDistance_IN();
                linearOpMode.telemetry.addData("front Dist>18", null);
                linearOpMode.telemetry.addData("left Dist ", leftDist);
                if (leftDist < lowerDistFromSideWall)
                {
                    linearOpMode.telemetry.addData("left dist < 4", null);
                    pivotenc(-stepPivotAmtDeg, .5f);
                    driveStraight(stepDistance, .5f);
                    straightDistanceTraveled = straightDistanceTraveled + stepDistance;
                    pivotenc(stepPivotAmtDeg, .5f);
                }
                else if (leftDist > upperDistFromSideWall)
                {
                    linearOpMode.telemetry.addData("left dist > 7", null);
                    pivotenc(stepPivotAmtDeg, .5f);
                    driveStraight(stepDistance, .5f);
                    straightDistanceTraveled = straightDistanceTraveled + stepDistance;
                    pivotenc(-stepPivotAmtDeg, .5f);
                }
                else //need this null zone for logic, this is where it goes straight, do not comment out
                {
                    driveStraight(stepDistance, .8f);
                    straightDistanceTraveled = straightDistanceTraveled + stepDistance;
                }
                linearOpMode.telemetry.update();

                if (straightDistanceTraveled >= maximumDistance)
                {
                    break;
                }
            }
            linearOpMode.sleep(500);
        }
    }

    public void initCRServoAndServoPos()
    {
        wristCollectorServo.setPosition(0.4);
        rightGrabCRServo.setPower(0);
        leftGrabCRServo.setPower(0);
    }

    public void deployMarker()
    {
        wristCollectorServo.setPosition(.6);
        float encTarget = 200f;
        collectorPivoterMotor.setPower(1);
        while ((collectorPivoterMotor.getCurrentPosition() < encTarget)
                && (!linearOpMode.isStopRequested()))
        {

        }
        linearOpMode.sleep(300);
        rightGrabCRServo.setPower(-1);
        linearOpMode.sleep(3000);

        initCRServoAndServoPos();
    }

    public void climbDown()
    {
        climberMotor.setMode(DcMotorImplEx.RunMode.STOP_AND_RESET_ENCODER);
        climberMotor.setMode(DcMotorImplEx.RunMode.RUN_USING_ENCODER);
        climberMotor.setPower(-1);

        long wantedTime = System.currentTimeMillis() + 6000;

        while (climberMotor.getCurrentPosition() > -16550 && !linearOpMode.isStopRequested() && System.currentTimeMillis() <= wantedTime)
        {

        }
        climberMotor.setPower(0);
    }

    public void climbUp()
    {
        climberMotor.setMode(DcMotorImplEx.RunMode.STOP_AND_RESET_ENCODER);
        climberMotor.setMode(DcMotorImplEx.RunMode.RUN_USING_ENCODER);
        climberMotor.setPower(1);

        long wantedTime = System.currentTimeMillis() + 5000;
        while (climberMotor.getCurrentPosition() < 16500 && !linearOpMode.isStopRequested() && System.currentTimeMillis() <= wantedTime)
        {

        }
        climberMotor.setPower(0);
    }

    public void samplerUp()
    {
        samplingServo.setPosition(.78);
    }

    public void samplerDown()
    {
        samplingServo.setPosition(1);
    }

    public void setGrabberWheelPower(double pow)
    {
        rightGrabCRServo.setPower(pow);
        leftGrabCRServo.setPower(pow);
    }

    public Servo getWristCollectorServo()
    {
        return wristCollectorServo;
    }

    public DcMotorImplEx getClimberMotor()
    {
        return climberMotor;
    }

    public DcMotorImplEx getCollectorLifterMotor()
    {
        return collectorLifterMotor;
    }

    public DcMotorImplEx getCollectorPivoterMotor()
    {
        return collectorPivoterMotor;
    }

    public boolean getSwitchSample()
    {
        return switchSample.getState();
    }

    public boolean getSwitchDepot()
    {
        return switchDepot.getState();
    }

    public boolean getSwitchCrater()
    {
        return switchCrater.getState();
    }

    public boolean getSwitchDelay()
    {
        return switchDelay.getState();
    }
}*/
