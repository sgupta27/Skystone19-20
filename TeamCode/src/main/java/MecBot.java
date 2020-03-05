import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import android.graphics.Color;


import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class MecBot
{
    private DcMotorImplEx driveLeftFront, driveRightFront, driveLeftBack, driveRightBack, shoulderMotor, armMotor;

        private OpMode systemAccess; //need access to telemetry (OpMode) and sometimes sleep (LinearOpMode)
        private DistanceSensor frontDistSens, rightDistSens, leftDistSens;
        private Servo rightGrabServo, leftGrabServo, wristServo, clampServo;
        RevBlinkinLedDriver lights;
        Orientation angles;
        Acceleration gravity;
        int loops = 0;
        double velocitiesR = 0;
        double velocitiesL = 0;

        private ColorSensor frontColorSens;
        private TouchSensor blockTouchSens;

    private int encCountsPerRev = 1120; //Based on Nevverest 40 motors
        private float roboDiameterCm = (float) (45.7 * Math.PI); // can be adjusted
        private float wheelCircIn = 4 * (float) Math.PI; //Circumference of wheels used
        private float wheelCircCm = (float) (9.8 * Math.PI);
        private ElapsedTime runtime = new ElapsedTime();
        private int extTargetPos;
        private int shoulderTargetPos;
        private short currentLvl = 0;
        private MecBot.directions extDirection = directions.Stop;
        private MecBot.directions shoulderDirection = directions.Stop;
        public ColorSensor getFrontColorSens;

    enum Result
    {
        Left, Right, Moved;
    }
    enum directions
    {
        Up, Down, Stop
    }

    public MecBot(HardwareMap hMap, OpMode systemAccessIN)
    {
        systemAccess = systemAccessIN;
        initMotors(hMap);
        initSensors(hMap);
    }

        private void initSensors(HardwareMap hMap)
        {
            frontColorSens = hMap.get(ColorSensor.class, "frontColorSens");
            blockTouchSens = hMap.get(TouchSensor.class, "blockTouchSens");
            int tempColor = getFrontColorSens().getVersion();
            systemAccess.telemetry.addData("version", tempColor);
            systemAccess.telemetry.addData("Class: ", RevBlinkinLedDriver.class);
            systemAccess.telemetry.update();
            lights = hMap.get(RevBlinkinLedDriver.class, "blinkin");
            frontDistSens = hMap.get(DistanceSensor.class, "frontDistSens");
            //rightDistSens = hMap.get(DistanceSensor.class, "rightDistSens");
            //leftDistSens = hMap.get(DistanceSensor.class, "leftDistSens");
        }

        public void initMotors(HardwareMap hMap)
        {
            driveLeftFront = hMap.get(DcMotorImplEx.class, "driveLeftFront");
            driveRightBack = hMap.get(DcMotorImplEx.class, "driveRightBack");
            driveLeftBack = hMap.get(DcMotorImplEx.class, "driveLeftBack");
            driveRightFront = hMap.get(DcMotorImplEx.class, "driveRightFront");

            shoulderMotor = hMap.get(DcMotorImplEx.class, "shoulderMotor");
            armMotor = hMap.get(DcMotorImplEx.class, "armMotor");

            rightGrabServo = hMap.servo.get("rightGrabServo");
            rightGrabServo.setDirection(Servo.Direction.FORWARD);

            leftGrabServo = hMap.servo.get("leftGrabServo");
            leftGrabServo.setDirection(Servo.Direction.FORWARD);

            clampServo = hMap.servo.get("clampServo");
            clampServo.setDirection(Servo.Direction.FORWARD);
            clampServo.setPosition(0.55);

            wristServo = hMap.servo.get("wristServo");
            wristServo.setDirection(Servo.Direction.FORWARD);
            wristServo.setPosition(0.0);

            driveRightFront.setDirection(DcMotorSimple.Direction.FORWARD);
            driveRightBack.setDirection(DcMotorSimple.Direction.FORWARD);
            driveLeftFront.setDirection(DcMotorSimple.Direction.REVERSE);
            driveLeftBack.setDirection(DcMotorSimple.Direction.REVERSE);

            driveLeftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            driveRightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            driveLeftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            driveRightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            shoulderMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            shoulderMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            armMotor.setDirection(DcMotorSimple.Direction.FORWARD);

            platformRelease();

            stopAllMotors();

        }

        public void checkEncTest()
        {
            systemAccess.telemetry.addData("rightFront encoders: ", getRightFrontEncoderPos());
            systemAccess.telemetry.addData("leftFront encoders: ", getLeftBackEncoderPos());
            systemAccess.telemetry.addData("rightBack encoders: ", getRightBackEncoderPos());
            systemAccess.telemetry.addData("leftBack encoders: ", getLeftFrontEncoderPos());
            systemAccess.telemetry.update();
        }

        public float getHueValue()
        {
            float hsvValues[] = {0F, 0F, 0F};
            Color.RGBToHSV(frontColorSens.red(), frontColorSens.green(), frontColorSens.blue(), hsvValues);

            //Above function is used to conert from RGB to HSV
            return hsvValues[0];
        }
        /*public float getSatValue(ColorSensor frontColorSens)
        {
            float hsvValues[] = {0F, 0F, 0F};
            Color.RGBToHSV(frontColorSens.red(), frontColorSens.green(), frontColorSens.blue(), hsvValues);
            return hsvValues[1];
        }

        public float getValueValue(ColorSensor frontColorSens)
        {
            float hsvValues[] = {0F, 0F, 0F};
            Color.RGBToHSV(frontColorSens.red(), frontColorSens.green(), frontColorSens.blue(), hsvValues);
            return hsvValues[2];
        }*/
        public char getColor()
        {
            float hue = getHueValue();
            //float value = getValueValue(frontColorSens);

            if (hue > 0 && hue < 50)
                return 'y';
            else
                return 's'; //s for skystone
        }
        public void driveStraight_Enc(float encoders, double pow)
        {
            resetDriveEncoders();
            holonomic(0,0, pow, 1);
//            driveRightFront.setPower(pow);
//            driveLeftBack.setPower(pow);
//            driveRightBack.setPower(pow);
//            driveLeftFront.setPower(pow);

          /*  systemAccess.telemetry.addData("rightFront encoders: ", getRightFrontEncoderPos());
            systemAccess.telemetry.addData("leftFront encoders: ", getLeftBackEncoderPos());
            systemAccess.telemetry.addData("rightBack encoders: ", getRightBackEncoderPos());
            systemAccess.telemetry.addData("leftBack encoders: ", getLeftFrontEncoderPos());
            systemAccess.telemetry.addData("power", pow);
            systemAccess.telemetry.update();  */

            encoders=Math.abs(encoders);

            while (Math.abs(driveRightFront.getCurrentPosition()) < encoders && Math.abs(driveLeftBack.getCurrentPosition()) < encoders && Math.abs(driveRightBack.getCurrentPosition()) < encoders && Math.abs(driveLeftFront.getCurrentPosition()) < encoders)
            {

            }
            stopDriveMotors();
           /* systemAccess.telemetry.addData("rightFront encoders: ", getRightFrontEncoderPos());
            systemAccess.telemetry.addData("leftBack encoders: ", getLeftBackEncoderPos());
            systemAccess.telemetry.addData("rightBack encoders: ", getRightBackEncoderPos());
            systemAccess.telemetry.addData("leftFront encoders: ", getLeftFrontEncoderPos());
            systemAccess.telemetry.update();  */
        }

        public void driveMotorsAuto(float lPow, float rPow)
        {
            driveMotors(-lPow, -rPow);
        }

        public void driveStraight_In_Stall(float inches, double pow, Telemetry telemetry)
        {
            float encTarget = 1120 / wheelCircIn * inches;
            //You get the number of encoder counts per unit and multiply it by how far you want to go

            float absPow = (float) Math.abs(pow);
            resetDriveEncoders();
            //Notes: We are using Andymark Neverrest 40
            // 1120 counts per rev

            if (pow < 0)
            {
                inches *= -1;
            }
            if (inches < 0)
            {
                driveMotorsAuto(-absPow, -absPow);

                while (driveLeftFront.getCurrentPosition() < -encTarget && driveRightFront.getCurrentPosition() > encTarget)
                {
                    // if (Math.abs(driveLeftOne.getVelocity(AngleUnit.DEGREES) <  *.75 )
                    double rVel = getDriveRightFront().getVelocity(AngleUnit.DEGREES);
                    double lVel = getDriveLeftFront().getVelocity(AngleUnit.DEGREES);
                    loops++;
                    velocitiesR += rVel;
                    velocitiesL += lVel;
                    telemetry.addData("RightVel ", rVel);
                    telemetry.addData("LeftVel ", lVel);
                    telemetry.addData("Average", null);
                    telemetry.addData("LAvg ", velocitiesL / loops);
                    telemetry.addData("RAvg ", velocitiesR / loops);
                    telemetry.update();
                    if (loops > 3 && (Math.abs(driveRightFront.getVelocity(AngleUnit.DEGREES)) < 5 || Math.abs(driveLeftFront.getVelocity(AngleUnit.DEGREES)) < 5))
                        break;
                }
            }
            else
            {
                driveMotorsAuto(absPow, absPow);

                while (driveLeftFront.getCurrentPosition() > -encTarget && driveRightFront.getCurrentPosition() < encTarget)
                {
                    double rVel = getDriveRightFront().getVelocity(AngleUnit.DEGREES);
                    double lVel = getDriveLeftFront().getVelocity(AngleUnit.DEGREES);
                    loops++;
                    velocitiesR += rVel;
                    velocitiesL += lVel;
                    telemetry.addData("RightVel ", rVel);
                    telemetry.addData("LeftVel ", lVel);
                    telemetry.addData("Average", null);
                    telemetry.addData("LAvg ", velocitiesL / loops);
                    telemetry.addData("RAvg ", velocitiesR / loops);
                    telemetry.update();
                    if (loops > 3 && (Math.abs(driveRightFront.getVelocity(AngleUnit.DEGREES)) < 5 || Math.abs(driveLeftFront.getVelocity(AngleUnit.DEGREES)) < 5))
                        break;
                }

                stopDriveMotors();
            }
        }

    public void stopDriveMotors()
        {
            holonomic(0,0,0,0);
        }

        public void spin_Right(float degrees)
        {
            spin_Right(degrees, 1);
        }

        public void spin_Right(float degrees, double pow)// Right Motor only moves!
        {
            double degToRad = degrees * Math.PI / 180.0f; // converts it to Radians

            double encTarget = (roboDiameterCm / 2 * degToRad) * (encCountsPerRev / wheelCircCm);
            //To explain, the first set of parenthesis gets the radius of robot and multiplies it by the degrees in radians
            //second set gets encoder counts per centimeter

            if (degrees < 0) //spins clockwise
            {
                driveRightFront.setPower(-Math.abs(pow));
                driveRightBack.setPower(-Math.abs(pow));
                while (driveRightFront.getCurrentPosition() > encTarget)
                {
                }
            } else //spins cc
            {
                driveRightFront.setPower(Math.abs(pow));
                driveRightBack.setPower(Math.abs(pow));
                while (driveRightFront.getCurrentPosition() < encTarget)
                {
                }
            }

            stopDriveMotors();
        }

    /*public void spin_Right_IMU(float degrees, double pow)
    {
        while (degrees > 180)
        {
            degrees -= 360;
        }
        while (degrees < -180)
        {
            degrees += 360;
        }
        if (degrees < 0)
        {
            driveRightOne.setPower(-Math.abs(pow));
            while (getYaw() > degrees)
            {
            }
        } else
        {
            driveRightOne.setPower(Math.abs(pow));
            while (getYaw() < degrees)
            {
            }
        }

        stopAllMotors();
    }*/

        public void spin_Left(float degrees)
        {
            spin_Left(degrees, 1);
        }

        public void spin_Left(float degrees, double pow)//Left Motor only moves!!!!!!!
        {
            double degToRad = degrees * Math.PI / 180.0f; // converts it to Radians

            double encTarget = (roboDiameterCm / 2 * degToRad) * (encCountsPerRev / wheelCircCm);
            //To explain, the first set of parenthesis gets the radius of robot and multiplies it by the degrees in radians
            //second set gets encoder counts per centimeter


            if (degrees > 0) //This spins the robot counterclockwise
            {
                driveLeftFront.setPower(Math.abs(pow));
                driveRightFront.setPower(Math.abs(pow));
                while (driveLeftFront.getCurrentPosition() < encTarget)
                {
                }

            } else //spins clockwise
            {
                driveLeftFront.setPower(-Math.abs(pow));
                driveLeftBack.setPower(-Math.abs(pow));
                while (driveLeftFront.getCurrentPosition() > encTarget)
                {
                }
            }

            stopDriveMotors();
        }

        /*public void spin_Left_IMU(float deg, double pow)
        {
            float degrees = deg;
            if (deg > 0)
            {
                degrees -= 5.2f;
            } else
            {
                degrees += 5.2f;
            }

            while (degrees > 180)
            {
                degrees -= 360;
            }
            while (degrees < -180)
            {
                degrees += 360;
            }

            initIMU();
            if (degrees < 0)
            {
                driveLeftOne.setPower(-Math.abs(pow));
                while (getYaw() > degrees)
                {
                }
            } else
            {
                driveLeftOne.setPower(Math.abs(pow));
                while (getYaw() < degrees)
                {
                }
            }

            stopAllMotors();
        }*/
        public void pivot_enc(double encoder)//Utilizes two motors at a time; spins in place
        {
            //It pivots in the direction of how to unit circle spins
            resetDriveEncoders();

            if (encoder < 0) //Pivot Counterclockwise
            {
                holonomic(-.8, 0,0,1);
//                driveRightFront.setPower(.8);
//                driveRightBack.setPower(.8);
//                driveLeftFront.setPower(-.8);
//                driveLeftBack.setPower(-.8);

            } else //Clockwise
            {
                holonomic(.8,0,0,1);
//                driveRightFront.setPower(-.8);
//                driveRightBack.setPower(-.8);
//                driveLeftFront.setPower(.8);
//                driveLeftBack.setPower(.8);
            }
            encoder=Math.abs(encoder);

            while (Math.abs(driveRightFront.getCurrentPosition()) < encoder && Math.abs(driveRightBack.getCurrentPosition()) < encoder && Math.abs(driveLeftFront.getCurrentPosition()) < encoder && Math.abs(driveLeftBack.getCurrentPosition()) < encoder)

            {

            }
            stopDriveMotors();
        }

    public void strafe_enc(double encoder)//Utilizes two motors at a time; spins in place
    {
    //It pivots in the direction of how to unit circle spins
        resetDriveEncoders();

        if (encoder < 0) //left
        {
            holonomic(0,-.8,0,1);
//            driveRightFront.setPower(.8);
//            driveRightBack.setPower(-.8);
//            driveLeftFront.setPower(-.8);
//            driveLeftBack.setPower(.8);

        } else //right
        {
            holonomic(0,.8,0,1);
//            driveRightFront.setPower(-.8);
//            driveRightBack.setPower(.8);
//            driveLeftFront.setPower(.8);
//            driveLeftBack.setPower(-.8);
        }
        encoder=Math.abs(encoder);

        while (Math.abs(driveRightFront.getCurrentPosition()) < encoder && Math.abs(driveRightBack.getCurrentPosition()) < encoder && Math.abs(driveLeftFront.getCurrentPosition()) < encoder && Math.abs(driveLeftBack.getCurrentPosition()) < encoder)

        {

        }
        stopDriveMotors();
    }
    public void holonomic(double Turn, double Strafe, double Forward, double MAX_SPEED)
    {

        double Magnitude = Math.abs(Turn) + Math.abs(Strafe) + Math.abs(Forward);
        Magnitude = (Magnitude > 1) ? Magnitude: 1; //Set Scaling to keep -1 to +1 range
        if (Magnitude > 1)
        {
            Turn = Turn/Magnitude;
            Strafe = Strafe/Magnitude;
            Forward = Forward/Magnitude;
        }
        systemAccess.telemetry.addData("turn", Turn);
        systemAccess.telemetry.addData("strafe", Strafe);
        systemAccess.telemetry.addData("forward", Forward);
        systemAccess.telemetry.update();

        driveLeftFront.setPower(-Turn - Strafe + Forward);
        if (driveRightBack != null)
        {
            driveRightBack.setPower(Turn - Strafe + Forward);

        }

        driveLeftBack.setPower(-Turn + Strafe + Forward);
        if (driveRightFront !=null)
        {
            driveRightFront.setPower(Turn + Strafe + Forward);
        }


    }
    /*public MecBot.Result wait_for_robot(double maxLookDistance_in, long timeToCheck_ms, double maxWait_s, boolean shiftLeft, LinearOpMode linearOpMode)
    {
        double sensorDist = getFrontDistance_IN();
        resetRunTime();
        //timeToCheck_ms = 100;
        while (sensorDist <= maxLookDistance_in && getRunTime() < maxWait_s && !linearOpMode.isStopRequested())
        {
            linearOpMode.sleep(timeToCheck_ms);
            sensorDist = getFrontDistance_IN();
            systemAccess.telemetry.addData("maxLookDistance_in: ", maxLookDistance_in);
            systemAccess.telemetry.addData("sensorDistance: ", sensorDist);
            systemAccess.telemetry.addData("maxWait_ms: ", maxWait_s);
            systemAccess.telemetry.addData("Time ran for: ", getRunTime());
            systemAccess.telemetry.addData("timeToCheck_ms: ", timeToCheck_ms);
            systemAccess.telemetry.addData("Shifting Left: ", shiftLeft);
            systemAccess.telemetry.addData("Outside: ", false);
            systemAccess.telemetry.addData("Output: ", "Currently Running");
            systemAccess.telemetry.update();
            setLightsColor(RevBlinkinLedDriver.BlinkinPattern.STROBE_GOLD);
        }
        setLightsColor(RevBlinkinLedDriver.BlinkinPattern.BLACK);
        if (getRunTime() >= maxWait_s)
        {
            if (shiftLeft)//Left for now
            {
                driveStrafe_Inches(-20, 1, this);
                return Result.Left;//Return the fact that we shifted left
            } else //Right for now
            {
                driveStrafe_Inches(20, 1, this);
                return Result.Right;//Return the fact that we shifted right
            }
        } else
        {
            return Result.Moved;//Return the fact that the object moved out of the way
        }
    }*/
    public void driveStraight_Inches(float dist_in, double pow, LinearOpMode linearOpMode)// this is the correct drive straight inches derived from our linear regression as of oct 23
    {
        resetDriveEncoders();

        if (dist_in < 0) //back
        {
            pow = -pow;

        }

        holonomic(0,0,pow,1);
//            driveRightFront.setPower(-.8);
//            driveRightBack.setPower(.8);
//            driveLeftFront.setPower(.8);
//            driveLeftBack.setPower(-.8);

        float encoders_count = (float) (69.55*Math.abs(dist_in) - 84.56); //ultrplanetary.V1: 87.38x - 114.96   22.62x -13.02      29.61x-8.462     91.83x  - 63.03

       // systemAccess.telemetry.addData("power", pow);
       // systemAccess.telemetry.update();

        while (linearOpMode.opModeIsActive() && Math.abs(driveRightFront.getCurrentPosition()) < encoders_count && Math.abs(driveLeftBack.getCurrentPosition()) < encoders_count && Math.abs(driveRightBack.getCurrentPosition()) < encoders_count && Math.abs(driveLeftFront.getCurrentPosition()) < encoders_count)
        {

        }
        stopDriveMotors();
    }
    public void driveStrafe_Inches(float dist_in, double pow, LinearOpMode linearOpMode)
    {

        float dist_togo_in = dist_in;

        while (Math.abs(dist_in)> 0) {
            if (Math.abs(dist_in) < 24) {
                dist_togo_in = dist_in;
                dist_in = 0.0f;
            } else if (dist_in > 0) {
                dist_togo_in = 20;
                dist_in -= 20;
            } else {
                dist_togo_in = -20;
                dist_in += 20;
            }

            resetDriveEncoders();
            if (dist_togo_in < 0) //left
            {
                holonomic(0, pow, 0, 1);

            } else //right
            {
                holonomic(0, -pow, 0, 1);
            }
            float encoders_count = (float) (78.37* Math.abs(dist_togo_in) - 82.21); //ultrplanetary.V1: 99.32x - 36.94 corehex comp:24.83x - 7.55     CoreHex practice:32.74x - 14.72

            while (linearOpMode.opModeIsActive() && Math.abs(driveRightFront.getCurrentPosition()) < encoders_count && Math.abs(driveLeftBack.getCurrentPosition()) < encoders_count && Math.abs(driveRightBack.getCurrentPosition()) < encoders_count && Math.abs(driveLeftFront.getCurrentPosition()) < encoders_count) {

            }
            stopDriveMotors();
        }
    }
    public void drivePivot_Degrees(float angle_deg, double pow, LinearOpMode linearOpMode)
    {
        resetDriveEncoders();

        if (angle_deg < 0) //Pivot clockwise
        {
            holonomic(-pow, 0,0,1);
        }
        else //Counterclockwise
        {
            holonomic(pow,0,0,1);
        }

        float encoders_count = (float) (16.72* Math.abs(angle_deg) + 67.63); //ultrplanetary.V1: 20.474x + 47.1804    6.57x - 12.24    7.339x+8.147

        while (linearOpMode.opModeIsActive() && Math.abs(driveRightFront.getCurrentPosition()) < encoders_count && Math.abs(driveLeftBack.getCurrentPosition()) < encoders_count && Math.abs(driveRightBack.getCurrentPosition()) < encoders_count && Math.abs(driveLeftFront.getCurrentPosition()) < encoders_count)
        {

        }
        stopDriveMotors();
    }
    public void calcTarget (int adjLevels)
    {
        double extPos = getArmPosition();
        double shoulderPos = getShoulderPosition();
        int extCloseEnough = 5;
        int shoulderCloseEnough = 5;
        final int shoulderLevels[] = {0, -175, -274, -375, -463, -540, -613, -647};
        final int extLevels[] = {0, -140, -140, -203, -276, -414, -676, -900};
        currentLvl += adjLevels;
        if (currentLvl < 0)
        {
            currentLvl = 0;
        }
        else if (currentLvl >= extLevels.length)
        {
            currentLvl = (short) (extLevels.length - 1);
        }
        //Calculate target
        extTargetPos = (extLevels[currentLvl]);
        shoulderTargetPos = (shoulderLevels[currentLvl]);
        if (shoulderPos > shoulderTargetPos+shoulderCloseEnough)
        {
            shoulderDirection = directions.Up;
        }
        else if (shoulderPos < shoulderTargetPos-shoulderCloseEnough)
        {
            shoulderDirection = directions.Down;
        }
        else
        {
            shoulderDirection = directions.Stop;
        }

        if (extPos > extTargetPos+extCloseEnough)
        {
            extDirection = directions.Up;
        }
        else if (extPos < extTargetPos-extCloseEnough)
        {
            extDirection = directions.Down;
        }
        else
        {
            extDirection = directions.Stop;
        }
    }
    public boolean AdjDir()
    {
        double extPos = getArmPosition();
        double shoulderPos = getShoulderPosition();
        int extCloseEnough = 5;
        int shoulderCloseEnough = 5;
        int shoulderHorizontal = -280;
        boolean shoulderMoved = false;
        if (shoulderDirection == directions.Up)
        {
            if (shoulderPos > shoulderTargetPos+shoulderCloseEnough)
            {
                if (shoulderPos < shoulderHorizontal) //less then horizontal is above horizontal on robot
                {
                    setShoulderPower(-.5);
                }
                else
                {
                    setShoulderPower(-.4);
                }
                shoulderMoved = true;
            }
            else
            {
                shoulderDirection = directions.Stop;
                setShoulderPower(0);
            }
        }
        else if (shoulderDirection == directions.Down)
        {
            if (shoulderPos < shoulderTargetPos-shoulderCloseEnough)
            {
                setShoulderPower(-.05);
                shoulderMoved = true;
            }
            else
            {
                shoulderDirection = directions.Stop;
                setShoulderPower(0);
            }
        }

        if (extDirection == directions.Up)
        {
            if (extPos > extTargetPos+extCloseEnough)
            {
                setArmPower(-.8);
            }
            else
            {
                extDirection = directions.Stop;
                setArmPower(0);
            }
        }
        else if (extDirection == directions.Down)
        {
            if (extPos < extTargetPos-extCloseEnough)
            {
                setArmPower(.8);
            } else
            {
                extDirection = directions.Stop;
                setArmPower(0);
            }
        }
        return shoulderMoved;
    }
    public boolean isIndexing(Telemetry telemetry)
    {
        boolean indexing = true;
        telemetry.addLine("shoulder:");
        telemetry.addData("p: ", getShoulderPosition());
        telemetry.addData("t: ", shoulderTargetPos);
        telemetry.addData("d: ", shoulderDirection);
        telemetry.addLine("arm");
        telemetry.addData("p: ", getArmPosition());
        telemetry.addData("t: ", extTargetPos);
        telemetry.addData("d: ", extDirection);
        telemetry.update();
        if (shoulderDirection == directions.Stop && extDirection == directions.Stop)
        {
            indexing = false;
        }
        return indexing;
    }
    public void cancelIndex ()
    {
        shoulderDirection = directions.Stop;
        extDirection = directions.Stop;
    }
    public void wristDown (LinearOpMode linearOpMode)
    {
        wristServo.setPosition(0.4);
        linearOpMode.sleep(500);
    }
    public void wristUp (LinearOpMode linearOpMode)
    {
        wristServo.setPosition(0);
        linearOpMode.sleep(700);
    }
    public void grabStone(LinearOpMode linearOpMode)
    {
        wristServo.setPosition(0.4);
        linearOpMode.sleep(750);
        clamp(true);
        linearOpMode.sleep(750);
        wristServo.setPosition(0);
        linearOpMode.sleep(1200);
    }
    public void dropStone(LinearOpMode linearOpMode)
    {
        wristServo.setPosition(0.1);
        clamp(false);
        linearOpMode.sleep(750);
        wristServo.setPosition(0);
        clamp(true);
        linearOpMode.sleep(1000);//original 700
    }
    public double setWristPosition (double wristPosition)
    {
        if (wristPosition < 0)
            wristPosition = 0;
        else if (wristPosition > .75)
            wristPosition = .75;
        wristServo.setPosition(wristPosition);
        return wristPosition;
    }
    public double setShoulderPower (double shoulderPower_PCT)
    {
        if (shoulderPower_PCT < -1.0)
            shoulderPower_PCT = -1.0;
         else if (shoulderPower_PCT > 0.1)
             shoulderPower_PCT = 0.1;
        shoulderMotor.setPower(shoulderPower_PCT);
        return shoulderPower_PCT;
    }
    public double getShoulderPosition()
    {
        return shoulderMotor.getCurrentPosition();
    }
    public double getArmPosition() {return armMotor.getCurrentPosition();}
    public void platformGrab()
    {
        leftGrabServo.setPosition(0.6);
        rightGrabServo.setPosition(0.0);
    }
    public void platformRelease()
    {
        leftGrabServo.setPosition(0.1);
        rightGrabServo.setPosition(0.6);
    }
    public double setArmPower(double armPower_PCT)
    {
        if (armPower_PCT < -1)
            armPower_PCT = -1;
        else if (armPower_PCT > 0.5)
            armPower_PCT = 0.5;
        armMotor.setPower(armPower_PCT);
        return armPower_PCT;
    }
    public void clamp(boolean close)
    {
            if (close)
            {
                clampServo.setPosition(0.55);
            }
            else
            {
                clampServo.setPosition(1.0);
            }
    }
    /*public void pivot_IMU(float degrees_IN)
    {
        pivot_IMU(degrees_IN, .8);
    }

    public void pivot_IMU(float degrees_In, double pow)
    {
        float degreesToStopAt;

        if (degrees_In > 0)
            degreesToStopAt = Math.abs(1.0661f * Math.abs(degrees_In) - 21.0936f);// at .8 pow
        else
            degreesToStopAt = -Math.abs(1.0661f * Math.abs(degrees_In) - 21.0936f);

        initIMU();

        systemAccess.sleep(100);
        if (degreesToStopAt < 0)
        {
            driveRightOne.setPower(-Math.abs(pow));
            driveLeftOne.setPower(-Math.abs(pow));
            while (getYaw() > degreesToStopAt && !systemAccess.isStopRequested())
            {
                systemAccess.sleep(160);
            }
        } else
        {
            driveRightOne.setPower(Math.abs(pow));
            driveLeftOne.setPower(Math.abs(pow));
            while (getYaw() < degreesToStopAt && !systemAccess.isStopRequested())
            {
                systemAccess.sleep(160);
            }
        }

        stopAllMotors();
    }*/

        public int getRightFrontEncoderPos()
        {
            return driveRightFront.getCurrentPosition();
        }
        public int getRightBackEncoderPos()
        {
            return driveRightBack.getCurrentPosition();
        }
        public int getLeftFrontEncoderPos()
        {
            return driveLeftFront.getCurrentPosition();
        }
        public int getLeftBackEncoderPos()
        {
            return driveLeftBack.getCurrentPosition();
        }

        public void driveMotors(float lPow, float rPow)
        {
            driveRightFront.setPower(-rPow);
            driveRightBack.setPower(-rPow);
            driveLeftFront.setPower(lPow);
            driveLeftBack.setPower(lPow);
        }
        public void driveRightFront(float rpow)
        {
            driveRightFront.setPower(rpow);
        }
        public void driveRightBack(float rpow)
        {
            driveRightBack.setPower(rpow);
        }
        public void driveLeftFront(float lpow)
        {
            driveLeftFront.setPower(lpow);
        }
        public void driveLeftBack(float lpow)
        {
            driveLeftBack.setPower(lpow);
        }
        public void stopAllMotors()
        {
            stopDriveMotors();
            setShoulderPower(0.0);
            setArmPower(0.0);
        }

    /*public void initIMU()
    {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu.initialize(parameters);
        imu.startAccelerationIntegration(new Position(), new Velocity(), 100);
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        //gravity  = imu.getGravity();
    }*/

    public DistanceSensor getFrontDistSens()
    {
        return frontDistSens;
    }

    public DistanceSensor getRightDistSens()
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

    public double getFrontDistance_IN()
    {
        return frontDistSens.getDistance(DistanceUnit.INCH);
    }
    public void setLightsColor(RevBlinkinLedDriver.BlinkinPattern pattern) { lights.setPattern(pattern);}

    //public class getLightsClass()
    //{
    //    return lights.getClass();
    //}
    public double getRunTime()
    {
        return runtime.time();
    }
    public void resetRunTime()
    {
        runtime.reset();
    }
    public double anglePerpToGrav()
        {
            return Math.atan(gravity.yAccel / gravity.zAccel);
        }

        public String getGravToString()
        {
            return gravity.toString();
        }

        public float getYaw()
        {
            return angles.firstAngle;
        }

        public void resetDriveEncoders()//sets encoders to 0 for motors
        {
            driveRightFront.setMode(DcMotorImplEx.RunMode.STOP_AND_RESET_ENCODER);
            driveLeftFront.setMode(DcMotorImplEx.RunMode.STOP_AND_RESET_ENCODER);
            driveRightBack.setMode(DcMotorImplEx.RunMode.STOP_AND_RESET_ENCODER);
            driveLeftBack.setMode(DcMotorImplEx.RunMode.STOP_AND_RESET_ENCODER);

            driveRightFront.setMode(DcMotorImplEx.RunMode.RUN_USING_ENCODER);
            driveLeftFront.setMode(DcMotorImplEx.RunMode.RUN_USING_ENCODER);
            driveRightBack.setMode(DcMotorImplEx.RunMode.RUN_USING_ENCODER);
            driveLeftBack.setMode(DcMotorImplEx.RunMode.RUN_USING_ENCODER);
        }

        public DcMotorImplEx getDriveLeftFront()
        {
            return driveLeftFront;
        }
        public DcMotorImplEx getDriveLeftBack()
        {
            return driveLeftBack;
        }
        public DcMotorImplEx getDriveRightFront()
        {
            return driveRightFront;
        }
        public DcMotorImplEx getDriveRightBack()
        {
            return driveRightBack;
        }

    public ColorSensor getFrontColorSens()
    {
        return frontColorSens;
    }

    public void kissWall(float requiredDist_in, float intervalDist, LinearOpMode linearOpMode)
    {
        double straightDist, rightDist, leftDist;
        double straightDistanceTraveled = 0;
        float stepDistance = 10;
        float stepPivotAmtDeg = 15;
        float initialEncoderCount = driveRightFront.getCurrentPosition();
        float currentEncoderCount = 0;

        wristServo.setPosition(0.33); //drops wrist before goes forward to avoid the plate from hitting the blocks when trying to pick up the blocks
        linearOpMode.sleep(600);
        DistanceSensor usingDistSensor = frontDistSens;
        holonomic(0,0,.2, 1);
        while (usingDistSensor.getDistance(DistanceUnit.INCH) > requiredDist_in && !linearOpMode.isStopRequested())
        {
              linearOpMode.sleep(50);
              currentEncoderCount = driveRightFront.getCurrentPosition();
              if (initialEncoderCount + 700 < currentEncoderCount)
              {
                  break;
              }
         //   linearOpMode.telemetry.addData("distanceFromWall: ", usingDistSensor.getDistance(DistanceUnit.INCH));
         //   linearOpMode.telemetry.addData("Required Distance: ", requiredDist_in);
         //   linearOpMode.telemetry.update();
        }
        stopDriveMotors();
    }
    public void kissBlock(LinearOpMode linearOpMode)
    {
        wristServo.setPosition(0.4);
        linearOpMode.sleep(500);
        holonomic(0,0,.2,1);
        while (!blockTouchSens.isPressed() && !linearOpMode.isStopRequested())
        {
            linearOpMode.sleep(50);
        }
        stopDriveMotors();
    }
}






