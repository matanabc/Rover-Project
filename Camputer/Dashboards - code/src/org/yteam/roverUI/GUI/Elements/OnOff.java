package org.yteam.roverUI.GUI.Elements;

import java.awt.Color;
import java.awt.Font;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
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
import javax.swing.KeyStroke;

import org.yteam.roverUI.Main;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

public class OnOff extends JPanel{

	protected static JButton enableButton;
	protected static JButton disableButton;
	protected static JButton driveSlowButton;

	protected JButton joystickIsConnected;

	protected JoystickThread joystickThred;

	public OnOff() {
		setLayout(null);

		enableButton = new JButton("Enable");
		disableButton = new JButton("Disable");
		joystickIsConnected = new JButton("Joystick Status");
		driveSlowButton = new JButton("Drive Slow");

		enableButton.setBackground(Color.CYAN);
		disableButton.setBackground(Color.RED);
		joystickIsConnected.setBackground(Color.RED);
		driveSlowButton.setBackground(Color.RED);

		enableButton.setFocusable(false);
		disableButton.setFocusable(false);
		joystickIsConnected.setFocusable(false);
		driveSlowButton.setFocusable(false);

		enableButton.setFont(new Font(Font.DIALOG, Font.BOLD, 25));
		disableButton.setFont(new Font(Font.DIALOG, Font.BOLD, 25));
		joystickIsConnected.setFont(new Font(Font.DIALOG, Font.BOLD, 25));
		driveSlowButton.setFont(new Font(Font.DIALOG, Font.BOLD, 25));

		enableButton.setBounds(0, 0, 150, 60);
		disableButton.setBounds(150, 0, 150, 60);
		joystickIsConnected.setBounds(320, 0, 250, 60);
		driveSlowButton.setBounds(600, 0, 250, 60);

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

					JOptionPane.showMessageDialog(null, "Joystick Is Not Connected! close pogram and open it agen with joystic plog to computer");
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

		driveSlowButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(joystickThred.getSlowDrive()) {
					driveSlowButton.setBackground(Color.RED);
					joystickThred.setSlowDrive(false);
				} else {
					driveSlowButton.setBackground(Color.GREEN);
					joystickThred.setSlowDrive(true);
				}
			}
		});

		add(enableButton);
		add(disableButton);
		add(joystickIsConnected);
		add(driveSlowButton);

		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(joystickThred);

		setVisible(true);
	}
}


class JoystickThread extends Thread implements KeyEventDispatcher{
	protected boolean sendValue = false;
	protected double driveSlow = 1;

	private ArrayList<Controller> foundControllers = new ArrayList<>();

	protected JButton joystickIsConnected;

	private InetAddress IPAddress;
	private DatagramSocket clientSocket;

	private int left;
	private int right;
	private String send = "0;0;" + driveSlow;
	
	private char slowDriveKey = 's';
	private char enableKey = 'e';
	private char disableKey = 'd';
	

	public JoystickThread(JButton joystickIsConnected) {
		super("Joystick");

		this.joystickIsConnected = joystickIsConnected;

		try{
			clientSocket = new DatagramSocket();
			//IPAddress = InetAddress.getByName("localhost");	
			IPAddress = InetAddress.getByName(Main.getRoverIP());//("192.168.14.150");

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
		sendingJoystickData();


		/*
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

				sleep(25);

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
		}*/
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

		if(foundControllers.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Joystick Is Not Connected! close pogram and open it agen with joystic plog to computer");
			System.exit(1);
		}
	}

	private void sendingJoystickData(){
		joystickIsConnected.setBackground(Color.GREEN);

		while(true)
		{
			Controller controller = foundControllers.get(0);//!

			// Pull controller for current data, and break while loop if controller is disconnected.
			if( !controller.poll() ){
				foundControllers.clear();
				send = "0;0;" + driveSlow;
				try {
					clientSocket.send(new DatagramPacket(send.getBytes(), send.length(), IPAddress, 9876));

					joystickIsConnected.setBackground(Color.RED);
					JOptionPane.showMessageDialog(null, "Joystick Is Not Connected! close pogram and open it agen with joystic plog to computer");

					System.exit(1);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

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

					if(component.getName().equals("Y Rotation")) {
						right = getAxisValueInPercentage(axisValue);
					}
				}
			}
			// We have to give processor some rest.
			try {
				if(sendValue) {
					if(left >= 48 && left <= 52) {
						left = 0;
					} else if(left > 50) {
						left = (left - 50) * 2;
						//left *= 0.8;
					} else {
						left = 200 + left * -2;
						//left = (int)(left * 0.8) + 20;
					}

					if(right >= 48 && right <= 52) {
						right = 0;
					} else if(right > 50) {
						right = (right - 50) * 2;
						//right *= 0.8;
					} else {
						right = 200 + right * -2;
						//right = (int)(right * 0.8) + 20;
					}

					byte[] sendData = new byte[15];//512

					send = left + ";" + right + ";" + driveSlow;

					sendData = send.getBytes();
					System.out.println(send);
					clientSocket.send(new DatagramPacket(sendData, sendData.length, IPAddress, 9876));
				}
				Thread.sleep(25);
			} catch (InterruptedException ex) {
				Logger.getLogger(JoystickThread.class.getName()).log(Level.SEVERE, null, ex);

				try {
					send = "0;0;" + driveSlow;
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
			send = "0;0;" + driveSlow;
			clientSocket.send(new DatagramPacket(send.getBytes(), send.length(), IPAddress, 9876));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setSlowDrive(boolean driveSlow) {
		this.driveSlow = driveSlow ? 0.6 : 1;
	}
	public boolean getSlowDrive() {
		return driveSlow != 1;
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		/*
		if (e.getID() == KeyEvent.KEY_PRESSED) {
			System.out.println("tester");
		} else if (e.getID() == KeyEvent.KEY_RELEASED) {
			System.out.println("2test2");
		} else if (e.getID() == KeyEvent.KEY_TYPED) {
			System.out.println("3test3");
		}*/
		
		//System.out.println(e.getKeyChar());

		if (e.getID() == KeyEvent.KEY_TYPED && e.getKeyChar() == enableKey) {
			setSendValue(true);
			OnOff.enableButton.setBackground(Color.GREEN);
			OnOff.disableButton.setBackground(Color.CYAN);
		} else if(e.getID() == KeyEvent.KEY_TYPED && e.getKeyChar() == disableKey) {
			setSendValue(false);
			OnOff.disableButton.setBackground(Color.RED);
			OnOff.enableButton.setBackground(Color.CYAN);
		} else if(e.getID() == KeyEvent.KEY_TYPED && e.getKeyChar() == slowDriveKey) {
			setSlowDrive(!getSlowDrive());
			
			if(getSlowDrive()) {
				OnOff.driveSlowButton.setBackground(Color.GREEN);
			} else {
				OnOff.driveSlowButton.setBackground(Color.RED);
			}
		}

		return false;
	}
}
