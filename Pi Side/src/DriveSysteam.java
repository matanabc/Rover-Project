import com.pi4j.wiringpi.Gpio;

//https://javatutorial.net/raspberry-pi-dim-led-pwm-java

public class DriveSysteam {

	private static final DriveSysteam instance = new DriveSysteam();	

	protected DriveSysteam() {
		try {
			Gpio.wiringPiSetupGpio();

			Gpio.pinMode(Constants.LEFT_PWM_PORT, Constants.PWM_OUTPOT_MODE);
			Gpio.pwmSetMode(Constants.PWM_MODE);
			Gpio.pwmSetRange(Constants.PWM_RANGE);
			Gpio.pwmSetClock(Constants.PWM_CLOCK);
			Gpio.pwmWrite(Constants.LEFT_PWM_PORT, 0);

			Gpio.pinMode(Constants.RIGHT_PWM_PORT, Constants.PWM_OUTPOT_MODE);
			Gpio.pwmSetMode(Constants.PWM_MODE);
			Gpio.pwmSetRange(Constants.PWM_RANGE);
			Gpio.pwmSetClock(Constants.PWM_CLOCK);
			Gpio.pwmWrite(Constants.RIGHT_PWM_PORT, 0);
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	public static DriveSysteam getInstance() {
		return instance;
	}

	public void setMotorOutpot(int left, int right) { //if joystick value  > 50 will be + if not will be - 
		Gpio.pwmWrite(18, left * Constants.OUTPOT_RANGE);
		Gpio.pwmWrite(Constants.RIGHT_PWM_PORT, right * Constants.OUTPOT_RANGE);
	}

}
