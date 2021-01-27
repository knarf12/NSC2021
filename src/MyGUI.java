import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.UIManager;

public class MyGUI extends JFrame{
	WelcomePanel wWelcomePanel;
	TrainModelPanel wTrainModelPanel;
	UsageModelPanel wUsageModelPanel;
	MenuBar mbar;
	public MyGUI(){
		
		
		getContentPane().setBackground(new Color(149, 149, 149));
		try{
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"); 
        }catch(Exception e){}
		initUI();
	}
	
	void initUI(){
		getContentPane().setLayout(null);
		mbar = new MenuBar();
		this.setJMenuBar(mbar);
		
		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		float width = screenSize.width;
		float height = screenSize.height;
		int xLocation = (int)((width-1024)/2);
		int yLocation = (int)((height-768)/2);
		
		
		
		wWelcomePanel = new WelcomePanel(this);
		wWelcomePanel.setLocation(xLocation, yLocation+20);
		wWelcomePanel.setSize(1024, 700);
		wWelcomePanel.setLayout(null); 
		wWelcomePanel.setVisible(true);
		getContentPane().add(wWelcomePanel);
		
		wTrainModelPanel = new TrainModelPanel(this);
		wTrainModelPanel.setLocation(xLocation, yLocation+20);
		wTrainModelPanel.setSize(1024, 700);
		wTrainModelPanel.setLayout(null); 
		wTrainModelPanel.setVisible(false);
		getContentPane().add(wTrainModelPanel);
		
		wUsageModelPanel = new UsageModelPanel(this);
		wUsageModelPanel.setLocation(xLocation, yLocation+20);
		wUsageModelPanel.setSize(1024, 700);
		wUsageModelPanel.setLayout(null); 
		wUsageModelPanel.setVisible(false);
		getContentPane().add(wUsageModelPanel); // เพิ่ม Model Frame

		
	}
	class MenuBar extends JMenuBar implements ActionListener {
		JButton mnModeling, mnModelUsage, mnModelStep, mnAbout, mnExit, mnMinimize;
		public MenuBar() {


			mnMinimize = new JButton("Minimize");
			mnMinimize.setFocusable(false);
			Color customColor = new Color(47, 141, 154);
			mnMinimize.setBackground(customColor);
			mnMinimize.setForeground(Color.white);
			mnMinimize.addActionListener(this);
			this.add(mnMinimize);

			mnExit = new JButton("Exit");
			mnExit.setFocusable(false);
			mnExit.setBackground(Color.RED);
			mnExit.setForeground(Color.WHITE);
			mnExit.addActionListener(this);
			this.add(mnExit);
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(mnExit)) {
				System.exit(0); // close Program
			}else if (e.getSource().equals(mnMinimize)) {
				setState(ICONIFIED);
			}else if (e.getSource().equals(mnAbout)) {
//				new About();
			}
			
		}
	}



}
