package GUI;

import javax.swing.JPanel;

import GUI.Elements.MjpgStreamViewer;

public class MapPanel extends JPanel{

	public MapPanel(){
		setLayout(null);

		MjpgStreamViewer stream = new MjpgStreamViewer("");
		stream.setBounds(0, 0, 640, 480);

		add(stream);

		setVisible(true);
	}
}
