import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class MecBot
{
    private DcMotorImplEx driveLeftFront, driveRightFront, driveLeftBack, driveRightBack;

        private LinearOpMode linearOpMode;
        private DistanceSensor frontDistSens;
        Orientation angles;
        Acceleration gravity;
        int loops = 0;
        double velocitiesR = 0;
        double velocitiesL = 0;

        private int encCountsPerRev = 1120; //Based on Nevverest 40 motors
        private float roboDiameterCm = (float) (45.7 * Math.PI); // can be adjusted
        private float wheelCircIn = 4 * (float) Math.PI; //Circumference of wheels used
        private float wheelCircCm = (float) (9.8 * Math.PI);
        private ElapsedTime runtime = new ElapsedTime();
        enum Result{
            Left, Right, Moved;
        }

    public MecBot(HardwareMap hardwareMap)
    {
//        driveLeftFront = hardwareMap.get(DcMotorImplEx.class, "driveLeftFront");
//        driveRightFront = hardwareMap.get(DcMotorImplEx.class, "driveRightFront");
//        driveLeftBack = hardwareMap.get(DcMotorImplEx.class, "driveLeftBack");
//        driveRightBack = hardwareMap.get(DcMotorImplEx.class, "driveRightBack");
        initMotors(hardwareMap);
        initMotorsAndMechParts(hardwareMap);
    }

    private void initMotorsAndMechParts(HardwareMap hMap)
    {
    }

        public MecBot(HardwareMap hMap, LinearOpMode linearOpModeIN)
        {
            linearOpMode = linearOpModeIN;
            initMotors(hMap);
        }

        public void initMotors(HardwareMap hMap)
        {
            driveLeftFront = hMap.get(DcMotorImplEx.class, "driveLeftFront");
            driveRightBack = hMap.get(DcMotorImplEx.class, "driveRightBack");
            driveLeftBack = hMap.get(DcMotorImplEx.class, "driveLeftBack");
            driveRightFront = hMap.get(DcMotorImplEx.class, "driveRightFront");
            frontDistSens = hMap.get(DistanceSensor.class, "frontDistSens");



            driveRightFront.setDirection(DcMotorSimple.Direction.REVERSE);
            driveRightBack.setDirection(DcMotorSimple.Direction.REVERSE);
            driveLeftFront.setDirection(DcMotorSimple.Direction.FORWARD);
            driveLeftBack.setDirection(DcMotorSimple.Direction.FORWARD);

            driveLeftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            driveRightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            driveLeftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            driveRightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            stopAllMotors();

        }

        public void checkEncTest()
        {
            linearOpMode.telemetry.addData("rightFront encoders: ", getRightFrontEncoderPos());
            linearOpMode.telemetry.addData("leftFront encoders: ", getLeftBackEncoderPos());
            linearOpMode.telemetry.addData("rightBack encoders: ", getRightBackEncoderPos());
            linearOpMode.telemetry.addData("leftBack encoders: ", getLeftFrontEncoderPos());
            linearOpMode.telemetry.update();
        }
        public void driveStraight_Enc(float encoders, double pow)
        {
            resetDriveEncoders();
            holonomic(0,0, pow, 1);
//            driveRightFront.setPower(pow);
//            driveLeftBack.setPower(pow);
//            driveRightBack.setPower(pow);
//            driveLeftFront.setPower(pow);

            linearOpMode.telemetry.addData("rightFront encoders: ", getRightFrontEncoderPos());
            linearOpMode.telemetry.addData("leftFront encoders: ", getLeftBackEncoderPos());
            linearOpMode.telemetry.addData("rightBack encoders: ", getRightBackEncoderPos());
            linearOpMode.telemetry.addData("leftBack encoders: ", getLeftFrontEncoderPos());
            linearOpMode.telemetry.update();

            encoders=Math.abs(encoders);

            while (Math.abs(driveRightFront.getCurrentPosition()) < encoders && Math.abs(driveLeftBack.getCurrentPosition()) < encoders && Math.abs(driveRightBack.getCurrentPosition()) < encoders && Math.abs(driveLeftFront.getCurrentPosition()) < encoders)
            {

            }
            stopAllMotors();
            linearOpMode.telemetry.addData("rightFront encoders: ", getRightFrontEncoderPos());
            linearOpMode.telemetry.addData("leftBack encoders: ", getLeftBackEncoderPos());
            linearOpMode.telemetry.addData("rightBack encoders: ", getRightBackEncoderPos());
            linearOpMode.telemetry.addData("leftFront encoders: ", getLeftFrontEncoderPos());
            linearOpMode.telemetry.update();
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
            } else
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
//            driveLeftFront.setPower(0);
//            driveLeftBack.setPower(0);
//            driveRightFront.setPower(0);
//            driveRightBack.setPower(0);
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

            stopAllMotors();
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

            stopAllMotors();
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
            stopAllMotors();
            stopAllMotors();
            stopDriveMotors();
        }
        public void pivot_deg(float degrees)
        {
            pivot(degrees, .8);
        }
        public void pivot_degpow(double degrees, double pow)//Utilizes two motors at a time; spins in place
        {

            double encTarget;
            encTarget = Math.abs(17.254 * Math.abs(degrees) + 367.295);

            //It pivots in the direction of how to unit circle spins
            if (degrees < 0) //Pivot Clockwise
            {
                holonomic(-Math.abs(pow), 0,0,1);
//                driveRightFront.setPower(-Math.abs(pow));
//                driveRightBack.setPower(-Math.abs(pow));
//                driveLeftFront.setPower(-Math.abs(pow));
//                driveLeftBack.setPower(-Math.abs(pow));
            } else //CounterClockwise
            {
                holonomic(Math.abs(pow),0,0,1);
//                driveRightFront.setPower(Math.abs(pow));
//                driveRightBack.setPower(Math.abs(pow));
//                driveLeftFront.setPower(Math.abs(pow));
//                driveLeftBack.setPower(Math.abs(pow));
            }

            while (Math.abs(driveLeftFront.getCurrentPosition()) < encTarget && Math.abs(driveRightFront.getCurrentPosition()) < encTarget && !linearOpMode.isStopRequested())
            {
            }
            stopAllMotors();
            stopDriveMotors();
        }

        public void pivot(float degrees, double pow)//Utilizes two motors at a time; spins in place
        {

            double encTarget;
            encTarget = Math.abs(17.254 * Math.abs(degrees) + 367.295);

            //It pivots in the direction of how to unit circle spins
            if (degrees < 0) //Pivot Clockwise
            {
                holonomic(-Math.abs(pow),0,0,1);
//                driveRightFront.setPower(-Math.abs(pow));
//                driveRightBack.setPower(-Math.abs(pow));
//                driveLeftFront.setPower(-Math.abs(pow));
//                driveLeftBack.setPower(-Math.abs(pow));
            }
            else //CounterClockwise
            {
                holonomic(Math.abs(pow),0,0,1);
//                driveRightFront.setPower(Math.abs(pow));
//                driveRightBack.setPower(Math.abs(pow));
//                driveLeftFront.setPower(Math.abs(pow));
//                driveLeftBack.setPower(Math.abs(pow));
            }

            while (Math.abs(driveLeftFront.getCurrentPosition()) < encTarget && Math.abs(driveRightFront.getCurrentPosition()) < encTarget && !linearOpMode.isStopRequested())
            {
            }
            stopAllMotors();
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
        stopAllMotors();
        stopAllMotors();
        stopDriveMotors();
    }
    public void holonomic(double Turn, double Strafe, double Forward, double MAX_SPEED)
    {
        //left front= +Turn +Strafe +Forward
        //right front= +Turn -Strafe -Forward
        //left rear= +Turn +Strafe -Forward
        //right rear= +Turn -Strafe +Forward

        double Magnitude = Math.abs(Turn) + Math.abs(Strafe) + Math.abs(Forward);
        Magnitude = (Magnitude > 1) ? Magnitude: 1; //Set Scaling to keep -1 to +1 range
        if (Magnitude > 1)
        {
            Turn = Turn/Magnitude;
            Strafe = Strafe/Magnitude;
            Forward = Forward/Magnitude;
        }

        driveLeftFront.setPower(-Turn - Strafe + Forward);
        if (driveLeftBack != null)
        {
            driveLeftBack.setPower(-Turn + Strafe + Forward);
        }

        driveRightFront.setPower(Turn + Strafe + Forward);
        if (driveRightBack !=null)
        {
            driveRightBack.setPower(Turn - Strafe + Forward);
        }
    }
    public MecBot.Result wait_for_robot(double maxLookDistance_in, long timeToCheck_ms, int maxWait_ms, boolean shiftLeft)
    {
        double sensorDist = getFrontDistance_IN();
        runtime.reset();
        while (sensorDist <= maxLookDistance_in && runtime.time() < maxWait_ms)
        {
            //linearOpMode.sleep(timeToCheck_ms);
            sensorDist = getFrontDistance_IN();
            //linearOpMode.telemetry.addData("maxLookDistance_in: ", maxLookDistance_in);
            //linearOpMode.telemetry.addData("sensorDistance: ", sensorDist);
            //linearOpMode.telemetry.addData("maxWait_ms: ", maxWait_ms);
            //linearOpMode.telemetry.addData("Time ran for: ", runtime.time());
            //linearOpMode.telemetry.addData("timeToCheck_ms: ", timeToCheck_ms);
            //linearOpMode.telemetry.addData("Shifting Left: ", shiftLeft);
            //linearOpMode.telemetry.update();
            //Flash the lights however we wish to
        }
        //set the lights back to normal
        if (runtime.time() >= maxWait_ms)
        {
            if (shiftLeft)//Left for now
            {
                strafe_enc(-2f);//Don't know how far we wish to move yet...
                return Result.Left;//Return the fact that we shifted left
            } else //Right for now
            {
                strafe_enc(2f);//Don't know how far we wish to move yet...
                return Result.Right;//Return the fact that we shifted right
            }
        } else
        {
            return Result.Moved;//Return the fact that the object moved out of the way
        }
    }
    public void driveStraight_Inches(float dist_in, double pow)
    {
        float encoders_count = 0;
        encoders_count= (float) (22.62*dist_in - 13.02);

        resetDriveEncoders();
        holonomic(0,0, pow, 1);
//            driveRightFront.setPower(pow);
//            driveLeftBack.setPower(pow);
//            driveRightBack.setPower(pow);
//            driveLeftFront.setPower(pow);

        linearOpMode.telemetry.addData("rightFront encoders: ", getRightFrontEncoderPos());
        linearOpMode.telemetry.addData("leftFront encoders: ", getLeftBackEncoderPos());
        linearOpMode.telemetry.addData("rightBack encoders: ", getRightBackEncoderPos());
        linearOpMode.telemetry.addData("leftBack encoders: ", getLeftFrontEncoderPos());
        linearOpMode.telemetry.update();


        while (Math.abs(driveRightFront.getCurrentPosition()) < encoders_count && Math.abs(driveLeftBack.getCurrentPosition()) < encoders_count && Math.abs(driveRightBack.getCurrentPosition()) < encoders_count && Math.abs(driveLeftFront.getCurrentPosition()) < encoders_count)
        {

        }
        stopAllMotors();
        linearOpMode.telemetry.addData("rightFront encoders: ", getRightFrontEncoderPos());
        linearOpMode.telemetry.addData("leftBack encoders: ", getLeftBackEncoderPos());
        linearOpMode.telemetry.addData("rightBack encoders: ", getRightBackEncoderPos());
        linearOpMode.telemetry.addData("leftFront encoders: ", getLeftFrontEncoderPos());
        linearOpMode.telemetry.update();
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

        linearOpMode.sleep(100);
        if (degreesToStopAt < 0)
        {
            driveRightOne.setPower(-Math.abs(pow));
            driveLeftOne.setPower(-Math.abs(pow));
            while (getYaw() > degreesToStopAt && !linearOpMode.isStopRequested())
            {
                linearOpMode.sleep(160);
            }
        } else
        {
            driveRightOne.setPower(Math.abs(pow));
            driveLeftOne.setPower(Math.abs(pow));
            while (getYaw() < degreesToStopAt && !linearOpMode.isStopRequested())
            {
                linearOpMode.sleep(160);
            }
        }

        stopAllMotors();
    }*/

        public int getRightFrontEncoderPos()
        {
            return driveRightFront.getCurrentPosition();
        }
        public int getRightBackEncoderPos() { return driveRightBack.getCurrentPosition(); }
        public int getLeftFrontEncoderPos() { return driveLeftFront.getCurrentPosition(); }
        public int getLeftBackEncoderPos() { return driveLeftBack.getCurrentPosition(); }

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
            holonomic(0,0,0,0);
//            driveLeftFront.setPower(0);
//            driveLeftBack.setPower(0);
//            driveRightFront.setPower(0);
//            driveRightBack.setPower(0);
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

    public double getFrontDistance_IN()
    {
        return frontDistSens.getDistance(DistanceUnit.INCH);
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
}
