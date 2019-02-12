package org.yteam.roverUI.GUI;

import java.awt.BorderLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

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
		
		//KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        //manager.addKeyEventDispatcher(new MyDispatcher());
        
		setVisible(true);
	}

	private class MyDispatcher implements KeyEventDispatcher {
		@Override
		public boolean dispatchKeyEvent(KeyEvent e) {
			if (e.getID() == KeyEvent.KEY_PRESSED) {
				System.out.println("tester");
			} else if (e.getID() == KeyEvent.KEY_RELEASED) {
				System.out.println("2test2");
			} else if (e.getID() == KeyEvent.KEY_TYPED) {
				System.out.println("3test3");
			}
			return false;
		}
	}
}
