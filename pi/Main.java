
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.util.CommandArgumentParser;
import com.pi4j.util.Console;
import java.util.concurrent.TimeUnit;


public class Main {

	/**
	 * [ARGUMENT/OPTION "--pin (#)" | "-p (#)" ]
	 * This example program accepts an optional argument for specifying the GPIO pin (by number)
	 * to use with this GPIO listener example. If no argument is provided, then GPIO #1 will be used.
	 * -- EXAMPLE: "--pin 4" or "-p 0".
	 *
	 * @param args
	 * @throws InterruptedException
	 */

	public static void main(String[] args) throws InterruptedException {

		// create Pi4J console wrapper/helper
		// (This is a utility class to abstract some of the boilerplate code)
		Console console = new Console();

		// print program title/header
		console.title("<-- The Pi4J Project -->", "PWM Example");

		// allow for user to exit program using CTRL-C
		console.promptForExit();

		// create GPIO controller instance
		GpioController gpio = GpioFactory.getInstance();

		// All Raspberry Pi models support a hardware PWM pin on GPIO_01.
		// Raspberry Pi models A+, B+, 2B, 3B also support hardware PWM pins: GPIO_23, GPIO_24, GPIO_26
		//
		// by default we will use gpio pin #01; however, if an argument
		// has been provided, then lookup the pin by address
		Pin pin = CommandArgumentParser.getPin(
				RaspiPin.class,    // pin provider class to obtain pin instance from
				RaspiPin.GPIO_01,  // default pin if no pin argument found
				args);             // argument array to search in

		Pin pin2 = CommandArgumentParser.getPin(
				RaspiPin.class,    // pin provider class to obtain pin instance from
				RaspiPin.GPIO_23,  // default pin if no pin argument found
				args);             // argument array to search in

		GpioPinPwmOutput leftMotors = gpio.provisionPwmOutputPin(pin);
		GpioPinPwmOutput rightMotors = gpio.provisionPwmOutputPin(pin2);

		// you can optionally use these wiringPi methods to further customize the PWM generator
		// see: http://wiringpi.com/reference/raspberry-pi-specifics/
		com.pi4j.wiringpi.Gpio.pwmSetMode(com.pi4j.wiringpi.Gpio.PWM_MODE_MS);
		com.pi4j.wiringpi.Gpio.pwmSetRange(1000);
		com.pi4j.wiringpi.Gpio.pwmSetClock(500);




		try {
			DatagramSocket serverSocket = new DatagramSocket(9876);
			byte[] receiveData = new byte[15];

			int left;
			int right;
			double slowDrive;
			
			System.out.println("starting");

			while(true)
			{

				Arrays.fill(receiveData, (byte)0);
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
				String sentence = new String(receivePacket.getData());

				//System.out.println("RECEIVED: " + sentence);

				String[] received = sentence.split(";");

				left = Integer.parseInt(received[1].trim());
				right = Integer.parseInt(received[0].trim());
				slowDrive = Double.parseDouble(received[2].trim());

				//58 is 0, 80 is 1, -1 is 1
				if(left > 100) {
					//System.out.println(forward(left, slowDrive));
					leftMotors.setPwm(forward(left, slowDrive));
				} else {
					//System.out.println(backward(left, slowDrive));
					leftMotors.setPwm(backward(left, slowDrive));	
				}

				if(right > 100) {
					//System.out.println(forward(right, slowDrive));
					rightMotors.setPwm(forward(right, slowDrive));
				} else {
					//System.out.println(backward(right, slowDrive));
					rightMotors.setPwm(backward(right, slowDrive));	
				}


				TimeUnit.MILLISECONDS.sleep(25);
				leftMotors.setPwm(0);	
				rightMotors.setPwm(0);	
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}

		// stop all GPIO activity/threads by shutting down the GPIO controller
		// (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
		//gpio.shutdown();
	}

	public static int forward(int joystickValue, double slowDrivepercentage) {
		//System.out.println((int)(58 - ((joystickValue - 100) * 0.22 * slowDrivepercentage)));
		return (int)(58 - ((joystickValue - 100) * 0.22 * slowDrivepercentage));//0.57
	}
	
	public static int backward(int joystickValue, double slowDrivepercentage) {
		return (int)(58 + joystickValue * 0.22 * slowDrivepercentage);
	}
}
