package org.firstinspires.ftc.teamcode.TestPrograms;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "TestSamplingServo", group = "Test")
public class TestSamplingServo extends OpMode
{
    Servo servo;
    double servoPos = .78;
    @Override
    public void init()
    {
        servo = hardwareMap.servo.get("SamplingServo");
        servo.setDirection(Servo.Direction.REVERSE);
        servo.setPosition(servoPos);
    }

    @Override
    public void loop()
    {
        if (gamepad2.right_trigger > .2f)
        {
            servoPos += .003;
            if (servoPos > 1)
                servoPos = 1;
        }
        else if (gamepad2.right_bumper)
        {
            servoPos -= .003;
            if (servoPos < .78)
                servoPos = .78;
        }
        servo.setPosition(servoPos);
        telemetry.addData("ServoPos :", servoPos);
        telemetry.addData("ActualPos :", servo.getPosition());
        telemetry.addData("Servo Dir", servo.getDirection());
        telemetry.update();
    }
}
