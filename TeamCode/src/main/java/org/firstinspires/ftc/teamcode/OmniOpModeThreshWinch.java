package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/**
* This file contains an example of an iterative (Non-Linear) "OpMode".
* An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
* The names of OpModes appear on the menu of the FTC Driver Station.
* When an selection is made from the menu, the corresponding OpMode
* class is instantiated on the Robot Controller and executed.
*
* This particular OpMode includes all the skeletal structure that all iterative OpModes contain.
*
* Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
* Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
*/
@TeleOp(name="OmniOpModeThreshWinch", group="Iterative Opmode")
//@Disabled
public class OmniOpModeThreshWinch extends OpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor FLDrive = null;
    private DcMotor FRDrive = null;
    private DcMotor BLDrive = null;
    private DcMotor BRDrive = null;
    private DcMotor Winch = null;
    private Servo Servo = null;
    /*
    * Code to run ONCE when the driver hits INIT
    */
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        FLDrive = hardwareMap.get(DcMotor.class, "FL");
        FRDrive = hardwareMap.get(DcMotor.class, "FR");
        BLDrive = hardwareMap.get(DcMotor.class, "BL");
        BRDrive = hardwareMap.get(DcMotor.class, "BR");
        Winch = hardwareMap.get(DcMotor.class, "Winch");
        Servo = hardwareMap.get(Servo.class, "SL");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        FRDrive.setDirection(DcMotor.Direction.FORWARD);
        BRDrive.setDirection(DcMotor.Direction.REVERSE);
        FLDrive.setDirection(DcMotor.Direction.REVERSE);
        BLDrive.setDirection(DcMotor.Direction.FORWARD);
        Winch.setDirection(DcMotor.Direction.FORWARD);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
    }

    /*
    * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
    */
    @Override
    public void init_loop() {
    }

    /*
    * Code to run ONCE when the driver hits PLAY
    */
    @Override
    public void start() {
        runtime.reset();
    }

    /*
    * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
    */
    @Override
    public void loop() {
        // The default below is Omni.

        // Left stick for translation, right for rotation.
        // - This uses basic math to combine motions and is easier to drive straight.
        double y_linear = gamepad1.left_stick_y/3;
        double x_linear = gamepad1.left_stick_x/3;
        double rotate = gamepad1.right_stick_x/5;
        double FLPower = Range.clip(-y_linear + x_linear + rotate, -1.0, 1.0);
        double FRPower = Range.clip(-y_linear - x_linear - rotate, -1.0, 1.0);
        double BRPower = Range.clip(y_linear - x_linear + rotate, -1.0, 1.0);
        double BLPower = Range.clip(y_linear + x_linear - rotate, -1.0, 1.0);


        // Send calculated power to wheels if gamepad1 signal exceeds threshold
        if(Math.abs(FLPower) + Math.abs(FRPower) + Math.abs(BLPower) + Math.abs(BRPower) > 0.1) {
            FLDrive.setPower(FLPower);
            FRDrive.setPower(FRPower);
            BLDrive.setPower(BLPower);
            BRDrive.setPower(BRPower);
        }
        else {
            FLDrive.setPower(0.0);
            FRDrive.setPower(0.0);
            BLDrive.setPower(0.0);
            BRDrive.setPower(0.0);
        }

        //Operate Winch with Gamepad2 Left Bumper (up) and Left Trigger (down)
        //Up is digital, Down is analog
        //double WinchPower = 0.0;
        //if (Gamepad2.left_trigger > 0.1) {
        //}
        //else {
        //    if (Gamepad2.left_bumper) {
        //        WinchPower = 0.1;
        //        }
        //    else {
        //        WinchPower = 0.0;
        //    }
        //
        //}
        //Operate winch with left stick control on gamepad2


         double    WinchPower = gamepad2.left_stick_y/3;
        Winch.setPower(WinchPower);

        //Code to control the grip Servo
        double GripSpeed = 0.02;
        if (gamepad2.b) {
        //    double GripPosition = Range.clip(GripPosition + GripSpeed,0.0,0.3); //Increase servo position if Y
        Servo.setPosition(0.6);
        }
        else if (gamepad2.a) {
        //   double GripPosition = Range.clip(GripPosition - GripSpeed,0.0,0.3); //Decrease servo position if A
        Servo.setPosition(0.2);
        }
        //Servo.setPosition(GripPosition);

        // Show the elapsed game time and wheel power.
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Motors", "left (%.2f), right (%.2f), winch (%.2f)", FLPower, FRPower, WinchPower);
    }

    /*
    * Code to run ONCE after the driver hits STOP
    */
    @Override
    public void stop() {
    }
}
