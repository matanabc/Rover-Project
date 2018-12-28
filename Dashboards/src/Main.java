import javax.swing.SwingUtilities;

import GUI.DashboardFrame;


public class Main {
	public static void main(final String[] args) {
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
}
