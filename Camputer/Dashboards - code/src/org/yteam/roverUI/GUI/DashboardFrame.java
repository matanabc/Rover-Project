package org.yteam.roverUI.GUI;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class DashboardFrame extends JFrame {
	private JTabbedPane tabbedPanels = new JTabbedPane(JTabbedPane.TOP);	
	
	public DashboardFrame() {
		setLayout(new BorderLayout());
		
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Dashboard");
		
		tabbedPanels.addTab("Home", new DashboardPanel());
		//tabbedPanels.addTab("Map", new MapPanel());
		
		add(tabbedPanels);	
		setVisible(true);
	}
}
