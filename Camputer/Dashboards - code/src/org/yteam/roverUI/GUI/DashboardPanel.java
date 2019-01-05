package org.yteam.roverUI.GUI;

import javax.swing.JPanel;

import org.yteam.roverUI.Main;
import org.yteam.roverUI.GUI.Elements.MjpgStreamViewer;
import org.yteam.roverUI.GUI.Elements.OnOff;

public class DashboardPanel extends JPanel {
	
	public DashboardPanel() {
		setLayout(null);
		
		MjpgStreamViewer stream = new MjpgStreamViewer("http://" + Main.getRoverIP() + ":8082/");
		stream.setBounds(0, 0, 640, 480);
		
		MjpgStreamViewer stream2 = new MjpgStreamViewer("http://" + Main.getRoverIP() + ":8081/");
		stream2.setBounds(680, 0, 640, 480);
		
		OnOff buttons = new OnOff();
		buttons.setBounds(0, 500, 1000, 60);
		
		add(stream);
		add(stream2);
		add(buttons);
		
		setVisible(true);
	}
}
