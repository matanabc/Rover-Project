package org.yteam.roverUI;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.yteam.roverUI.GUI.DashboardFrame;


public class Main {

	private static String IP;
	private static double slowSpeed;

	public static void main(final String[] args) {
		if(args.length != 0) {
			System.out.println(args[0]);
			String[] data = args[0].split(";");
			
			if(data.length >= 2) {
				System.out.println("Rover IP = " + data[0] + " , Slow Drive Value = " + data[1]);
				IP = data[0];
				slowSpeed = Double.parseDouble(data[1]);
			} else {
				JOptionPane.showMessageDialog(null, "No Rover IP have given or slow speed, plese fix it!");
				System.exit(1);
			}
		} else {
			JOptionPane.showMessageDialog(null, "No Rover IP have given or slow speed, plese fix it!");
			System.exit(1);
		}

		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					DashboardFrame frame = new DashboardFrame();
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(2);
		}
	}

	public static String getRoverIP() {
		return IP;
	}
	public static double getRoverSlowDrive() {
		return slowSpeed;
	}
}
