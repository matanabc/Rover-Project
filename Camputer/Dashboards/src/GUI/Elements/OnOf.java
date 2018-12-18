package GUI.Elements;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

public class OnOf extends JPanel{

	protected JButton enableButton;
	protected JButton disableButton;

	protected JButton joystickIsConnected;

	protected JoystickThread joystickThred;

	public OnOf() {
		setLayout(null);

		enableButton = new JButton("Enable");
		disableButton = new JButton("Disable");
		joystickIsConnected = new JButton("Joystick Status");

		enableButton.setBackground(Color.CYAN);
		disableButton.setBackground(Color.RED);
		joystickIsConnected.setBackground(Color.RED);

		enableButton.setFocusable(false);
		disableButton.setFocusable(false);
		joystickIsConnected.setFocusable(false);

		enableButton.setFont(new Font(Font.DIALOG, Font.BOLD, 25));
		disableButton.setFont(new Font(Font.DIALOG, Font.BOLD, 25));
		joystickIsConnected.setFont(new Font(Font.DIALOG, Font.BOLD, 25));

		enableButton.setBounds(0, 0, 150, 60);
		disableButton.setBounds(150, 0, 150, 60);
		joystickIsConnected.setBounds(320, 0, 250, 60);

		joystickIsConnected.setEnabled(false);

		joystickThred = new JoystickThread(joystickIsConnected);
		//joystickThred.sendStop();
		joystickThred.start();

		enableButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(joystickThred.joystickIsPlog()) {
					enableButton.setBackground(Color.GREEN);
					disableButton.setBackground(Color.CYAN);

					joystickThred.setSendValue(true);
				} else {
					disableButton.setBackground(Color.RED);
					enableButton.setBackground(Color.CYAN);

					joystickThred.setSendValue(false);

					JOptionPane.showMessageDialog(null, "Joystick Is Not Connected!");
				}
			}
		});

		disableButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				disableButton.setBackground(Color.RED);
				enableButton.setBackground(Color.CYAN);

				joystickThred.setSendValue(false);
				joystickThred.sendStop();
			}
		});

		add(enableButton);
		add(disableButton);
		add(joystickIsConnected);

		setVisible(true);
	}
}

class JoystickThread extends Thread {
	protected boolean sendValue = false;
	private ArrayList<Controller> foundControllers = new ArrayList<>();

	protected JButton joystickIsConnected;

	private InetAddress IPAddress;
	private DatagramSocket clientSocket;

	private int left;
	private int right;
	private String send = "0;0";

	public JoystickThread(JButton joystickIsConnected) {
		super("Joystick");

		this.joystickIsConnected = joystickIsConnected;

		try{
			clientSocket = new DatagramSocket();
			//IPAddress = InetAddress.getByName("localhost");
			IPAddress = InetAddress.getByName("192.168.14.150");

		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	@Override
	public void interrupt() {
		try {
			clientSocket.send(new DatagramPacket(send.getBytes(), send.length(), IPAddress, 9876));
		} catch (Exception e) {
			e.printStackTrace();
		}

		super.interrupt();
	}

	@Override
	public void run() {

		searchForControllers();

		if(foundControllers.isEmpty()) {
			joystickIsConnected.setBackground(Color.RED);
		} else {
			joystickIsConnected.setBackground(Color.GREEN);
		}

		while(true) {
			try {
				/*while(foundControllers.isEmpty()) {
					searchForControllers();
					System.out.println("No Joystick");

					joystickIsConnected.setBackground(Color.RED);

					sleep(25);
				}

				joystickIsConnected.setBackground(Color.GREEN);

				while (sendValue && !foundControllers.isEmpty()) {
					System.out.println("sending Joystick Value");
					sendingJoystickData();				
				}

				sleep(25);*/

				sendingJoystickData();
				sleep(25);	

			} catch (InterruptedException e) {
				e.printStackTrace();
				try {
					clientSocket.send(new DatagramPacket(send.getBytes(), send.length(), IPAddress, 9876));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
				try {
					clientSocket.send(new DatagramPacket(send.getBytes(), send.length(), IPAddress, 9876));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

	public void setSendValue(boolean sendValue) {
		this.sendValue = sendValue;
	}

	public boolean joystickIsPlog() {
		return !foundControllers.isEmpty();
	}


	/**
	 * Search (and save) for controllers of type Controller.Type.STICK,
	 * Controller.Type.GAMEPAD, Controller.Type.WHEEL and Controller.Type.FINGERSTICK.
	 */
	private void searchForControllers() {
		Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();

		for(int i = 0; i < controllers.length; i++){
			Controller controller = controllers[i];

			if (
					controller.getType() == Controller.Type.STICK || 
					controller.getType() == Controller.Type.GAMEPAD || 
					controller.getType() == Controller.Type.WHEEL ||
					controller.getType() == Controller.Type.FINGERSTICK
					)
			{
				// Add new controller to the list of all controllers.
				foundControllers.add(controller);
			}
		}
	}

	/**
	 * Starts showing controller data on the window.
	 */
	private void sendingJoystickData(){
		while(true)
		{
			Controller controller = foundControllers.get(0);//!

			// Pull controller for current data, and break while loop if controller is disconnected.
			if( !controller.poll() ){
				foundControllers.clear();
				//send 0

				break;
			}

			// Go trough all components of the controller.
			Component[] components = controller.getComponents();
			for(int i=0; i < components.length; i++)
			{
				Component component = components[i];
				Identifier componentIdentifier = component.getIdentifier();

				// Axes
				if(component.isAnalog()){
					float axisValue = component.getPollData();
					//int axisValueInPercentage = getAxisValueInPercentage(axisValue);

					// Y axis
					if(componentIdentifier == Component.Identifier.Axis.Y){
						//System.out.println("Y 1:" + axisValueInPercentage);

						left = getAxisValueInPercentage(axisValue);

						continue; // Go to next component.
					}

					if(component.getName().equals("Z Rotation")) {
						right = getAxisValueInPercentage(axisValue);
					}
				}
			}
			// We have to give processor some rest.
			try {
				if(sendValue) {
					if(left >= 49 && left <= 51) {
						left = 0;
					} else if(left > 50) {
						left = (left - 50) * 2;
					} else {
						left = 200 + left * -2;
					}
					
					if(right >= 49 && right <= 51) {
						right = 0;
					} else if(right > 50) {
						right = (right - 50) * 2;
					} else {
						right = 200 + right * -2;
					}
					
					byte[] sendData = new byte[512];
				
					send = left + ";" + right;
					sendData = send.getBytes();
					System.out.println(send);
					clientSocket.send(new DatagramPacket(sendData, sendData.length, IPAddress, 9876));
				}
				Thread.sleep(25);
			} catch (InterruptedException ex) {
				Logger.getLogger(JoystickThread.class.getName()).log(Level.SEVERE, null, ex);

				try {
					clientSocket.send(new DatagramPacket(send.getBytes(), send.length(), IPAddress, 9876));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}



	/**
	 * Given value of axis in percentage.
	 * Percentages increases from left/top to right/bottom.
	 * If idle (in center) returns 50, if joystick axis is pushed to the left/top 
	 * edge returns 0 and if it's pushed to the right/bottom returns 100.
	 * 
	 * @return value of axis in percentage.
	 */
	public int getAxisValueInPercentage(float axisValue)
	{
		return (int)(((2 - (1 - axisValue)) * 100) / 2);
	}

	public void sendStop() {
		try {
			send = "0;0";
			clientSocket.send(new DatagramPacket(send.getBytes(), send.length(), IPAddress, 9876));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
