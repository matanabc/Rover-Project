package GUI;

import javax.swing.JPanel;

import GUI.Elements.MjpgStreamViewer;
import GUI.Elements.OnOf;

/**
 * This is the main panel, it sits within the {@link DashboardFrame} and
 * contains everything the user sees except for the window outline. This class
 * is the workhorse of the GUI. Inside here is where the logic is contained for
 * how to respond to new fields and various other things.
 *
 * @author Joe Grinstead
 */
public class DashboardPanel extends JPanel {
	
	public DashboardPanel() {
		setLayout(null);
		
		MjpgStreamViewer stream = new MjpgStreamViewer("");
		stream.setBounds(0, 0, 640, 480);
		
		MjpgStreamViewer stream2 = new MjpgStreamViewer("");
		stream2.setBounds(680, 0, 640, 480);
		
		OnOf buttons = new OnOf();
		buttons.setBounds(0, 500, 600, 60);
		
		add(stream);
		add(stream2);
		add(buttons);
		
		setVisible(true);
	}
}
