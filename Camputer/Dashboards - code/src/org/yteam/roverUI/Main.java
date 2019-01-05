package org.yteam.roverUI;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.yteam.roverUI.GUI.DashboardFrame;


public class Main {

	private static String IP;

	public static void main(final String[] args) {
		if(args.length != 0) {
			System.out.println(args[0]);
			IP = args[0];
		} else {
			JOptionPane.showMessageDialog(null, "No Rover IP have given");
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
}
