import com.pi4j.wiringpi.Gpio;

public class Constants {
	public static final int LEFT_PWM_PORT = 18;
	public static final int RIGHT_PWM_PORT = 19;
	
	public static final int PWM_OUTPOT_MODE = Gpio.PWM_OUTPUT;
	public static final int PWM_MODE = Gpio.PWM_MODE_MS;
	public static final int PWM_RANGE = 1000;
	public static final int PWM_CLOCK = 500;
	
	public static final int OUTPOT_RANGE = 1024 / 50;
}
