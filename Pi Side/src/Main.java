
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

		GpioPinPwmOutput pwm = gpio.provisionPwmOutputPin(pin);
		System.out.println(pin.getAddress());

		// you can optionally use these wiringPi methods to further customize the PWM generator
		// see: http://wiringpi.com/reference/raspberry-pi-specifics/
		com.pi4j.wiringpi.Gpio.pwmSetMode(com.pi4j.wiringpi.Gpio.PWM_MODE_MS);
		com.pi4j.wiringpi.Gpio.pwmSetRange(1000);
		com.pi4j.wiringpi.Gpio.pwmSetClock(500);




		try {
			DatagramSocket serverSocket = new DatagramSocket(9876);
			byte[] receiveData = new byte[512];

			int left;
			int right;

			while(true)
			{

				Arrays.fill(receiveData, (byte)0);
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
				String sentence = new String(receivePacket.getData());

				System.out.println("RECEIVED: " + sentence);

				String[] received = sentence.split(";");

				left = Integer.parseInt(received[0].trim());
				right = Integer.parseInt(received[1].trim());



				//58 is 0, 80 is 1, -1 is 1
				if(left > 100) {
					//System.out.println((int)(58 + ((left -100) * 0.22)));
					pwm.setPwm((int)(58 + ((left -100) * 0.22)));
				} else {
					//System.out.println((int)(58 - (left * 0.58)));
					pwm.setPwm((int)(58 - (left * 0.58)));	
				}

				if(right > 100) {
					System.out.println((int)(58 + ((right -100) * 0.22)));
					pwm.setPwm((int)(58 + ((right -100) * 0.22)));
				} else {
					System.out.println((int)(58 - (right * 0.58)));
					pwm.setPwm((int)(58 - (right * 0.58)));	
				}
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}



		/*
		Scanner s = new Scanner(System.in);

	    while(true) {
	    	System.out.println("enter output");
	    	pwm.setPwm(s.nextInt());
		}*/


		/*
		while(true) {
			for(int i = 0; i < 1000; i++) {
				pwm.setPwm(i);
				i++;
				TimeUnit.MILLISECONDS.sleep(1);
			}

			for(int i = 1000; i > 0; i--) {
				pwm.setPwm(i);
				TimeUnit.MILLISECONDS.sleep(1);
			}
		}*/


		/*
        // set the PWM rate to 500
        pwm.setPwm(500);
        console.println("PWM rate is: " + pwm.getPwm());

        console.println("Press ENTER to set the PWM to a rate of 250");
        System.console().readLine();

        // set the PWM rate to 250
        pwm.setPwm(250);
        console.println("PWM rate is: " + pwm.getPwm());


        console.println("Press ENTER to set the PWM to a rate to 0 (stop PWM)");
        System.console().readLine();


        // set the PWM rate to 0
        pwm.setPwm(0);
        console.println("PWM rate is: " + pwm.getPwm());

		 */
		// stop all GPIO activity/threads by shutting down the GPIO controller
		// (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
		//gpio.shutdown();


	}
}
