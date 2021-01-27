import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

public class WelcomePanel extends JPanel implements ActionListener{
	
	MyGUI app;
	Image bg;
	JButton btnEnterNow;
	
	public WelcomePanel(MyGUI app) {
		this.app = app;
		initPanel();
	}

	void initPanel() {
		
		setSize(1024, 768);
		setLayout(null);
		
		Icon icon = new ImageIcon(getClass().getResource("btn_welcome.png"));
		btnEnterNow = new JButton(icon);
		btnEnterNow.setBounds(253, 585, 514, 71);
		btnEnterNow.setOpaque(false);
		btnEnterNow.setContentAreaFilled(false);
		btnEnterNow.setBorderPainted(false);
		btnEnterNow.addActionListener(this);
		add(btnEnterNow);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(obj.equals(btnEnterNow)) {
			setVisible(false);
			
			app.wTrainModelPanel.setVisible(true);
		}
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		bg = new ImageIcon(getClass().getResource("welcome_bg.jpg"))
				.getImage();
		g.drawImage(bg, 0, 0, null);
		g.setColor(Color.WHITE);
	}
}
