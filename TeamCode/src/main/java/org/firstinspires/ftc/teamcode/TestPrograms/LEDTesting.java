package org.firstinspires.ftc.teamcode.TestPrograms;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@Disabled
public class LEDTesting extends OpMode
{
    RevBlinkinLedDriver revBlinkinLedDriver;

    @Override
    public void init()
    {
        revBlinkinLedDriver = hardwareMap.get(revBlinkinLedDriver.getClass(), "blinkin");
    }

    @Override
    public void loop()
    {
        revBlinkinLedDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.TWINKLES_RAINBOW_PALETTE);
    }
}