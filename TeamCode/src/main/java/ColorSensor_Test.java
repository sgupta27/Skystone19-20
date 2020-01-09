import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import android.graphics.Color;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Disabled()
public class ColorSensor_Test extends OpMode
{
    //  private OmniDriveBot robot = new OmniDriveBot();
    //ColorSensor sensorRGB;
    //DeviceInterfaceModule cdim;
    boolean bPrevState;
    boolean bCurrState;
    boolean bLedOn;
    float hsvValues[] = {0F,0F,0F};
    float hsvValues_Ada[] = {0F,0F,0F};
    private MecBot holo;
    ColorSensor frontColorSens;
    // final float values[] = hsvValues;

    // we assume that the LED pin of the RGB sensor is connected to
    // digital port 5 (zero indexed).
    //static final int LED_CHANNEL = 5;

    public void init()
    {
        gamepad2.setJoystickDeadzone(.3f);//attachments
        gamepad1.setJoystickDeadzone(.3f);//driver
        holo = new MecBot(hardwareMap, this);
//        robot.init(hardwareMap);

        // get a reference to the RelativeLayout so we can change the background
        // color of the Robot Controller app to match the hue detected by the RGB sensor.

        // bPrevState and bCurrState represent the previous and current state of the button.
        bPrevState = false;
        bCurrState = false;

        // bLedOn represents the state of the LED.
        bLedOn = false;

        // get a reference to our DeviceInterfaceModule object.
        //cdim = hardwareMap.deviceInterfaceModule.get("dim");

        // set the digital channel to output mode.
        // remember, the Adafruit sensor is actually two devices.
        // It's an I2C sensor and it's also an LED that can be turned on or off.
        //  cdim.setDigitalChannelMode(LED_CHANNEL, DigitalChannelController.Mode.OUTPUT);

        // get a reference to our ColorSensor object.
        frontColorSens = hardwareMap.colorSensor.get("frontColorSens");
        // turn the LED on in the beginning, just so user will know that the sensor is active.
        //    cdim.setDigitalChannelState(LED_CHANNEL, bLedOn);
    }

    public void loop()
    {
        // check the status of the x button on gamepad.
        //bCurrState = gamepad1.x;
        // check for button-press state transitions.

        // update previous state variable.
        // bPrevState = bCurrState;

        // convert the RGB values to HSV values.
        Color.RGBToHSV((frontColorSens.red() * 255) / 800, (frontColorSens.green() * 255) / 800, (frontColorSens.blue() * 255) / 800, hsvValues);

        // send the info back to driver station using telemetry function.

        telemetry.addData("right" , null);
        telemetry.addData("RWC = ", holo.getColor());
        telemetry.addData("Hue ", hsvValues_Ada[0]);
        telemetry.addData("Saturation ", hsvValues_Ada[1]);
        telemetry.addData("Value ", hsvValues_Ada[2]);

        telemetry.addData("left" , null);
        telemetry.addData("LWC = ", holo.getColor());
        telemetry.addData("Hue " ,hsvValues[0]);
        telemetry.addData("Saturation ", hsvValues[1]);
        telemetry.addData("Value ", hsvValues[2]);

        // telemetry.addData("Hue= ", hsvValues[0]);
        telemetry.update();
    }
}
