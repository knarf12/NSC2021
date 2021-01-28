import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

public class UsageModelPanel extends JPanel implements ActionListener{
	
	MyGUI app;
	Image bg;
	
	JButton btnProcess;
	JButton btnBack;
	JTextField txtSearch;
	JButton btnSearch;
	private JRadioButton rdbtnLocal;
	private JRadioButton rdbtnPubmed;
	private JTextArea textArea;
	private JTextArea textArea_1;
	private JFileChooser fileChooser = new JFileChooser(); 
	private ButtonGroup bgSearch = new ButtonGroup();
	
	public UsageModelPanel(MyGUI app) {
		this.app = app;
		initPanel();
	}
	
	void initPanel() {
		
		setSize(1024, 768);
		setLayout(null);
		
		Icon iconProcess = new ImageIcon(getClass().getResource("btn_process_usege.png"));
		btnProcess = new JButton(iconProcess);
		btnProcess.setBounds(872, 290, 135, 60);
		btnProcess.setOpaque(false);
		btnProcess.setContentAreaFilled(false);
		btnProcess.setBorderPainted(false);
		btnProcess.addActionListener(this);
		
		JLabel lblNewLabel_1 = new JLabel("Knowledge Extraction");
		lblNewLabel_1.setFont(new Font("Consolas", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(333, 300, 188, 14);
		add(lblNewLabel_1);
		
		JLabel lblNewLabel = new JLabel("Original Documents");
		lblNewLabel.setFont(new Font("Consolas", Font.PLAIN, 14));
		lblNewLabel.setBounds(54, 300, 153, 14);
		add(lblNewLabel);
		add(btnProcess);
		
		Icon iconBack = new ImageIcon(getClass().getResource("btn_back_usege.png"));
		btnBack = new JButton(iconBack);
		btnBack.setBounds(872, 653, 135, 45);
		btnBack.setOpaque(false);
		btnBack.setContentAreaFilled(false);
		btnBack.setBorderPainted(false);
		btnBack.addActionListener(this);
		add(btnBack);
		
		txtSearch = new JTextField();
		txtSearch.setBounds(428, 182, 430, 37);
		add(txtSearch);
		txtSearch.setColumns(10);
		
		
		
		Icon iconSearch = new ImageIcon(getClass().getResource("btn_search.png"));
		btnSearch = new JButton(iconSearch);
		btnSearch.setBounds(870, 182, 52, 52);
		btnSearch.setOpaque(false);
		btnSearch.setContentAreaFilled(false);
		btnSearch.setBorderPainted(false);
		btnSearch.addActionListener(this);
		add(btnSearch);
		
		rdbtnLocal = new JRadioButton("Single Document");
		rdbtnLocal.setFont(new Font("Tahoma", Font.PLAIN, 14));
		rdbtnLocal.setBounds(114, 190, 164, 23);
		add(rdbtnLocal);
		
		rdbtnPubmed = new JRadioButton("Multi Document");
		rdbtnPubmed.setFont(new Font("Tahoma", Font.PLAIN, 14));
		rdbtnPubmed.setBounds(279, 190, 143, 23);
		add(rdbtnPubmed);
		
		textArea = new JTextArea();
		textArea.setBounds(333, 313, 504, 373);
		add(textArea);
		
		textArea_1 = new JTextArea();
		textArea_1.setBounds(54, 315, 227, 373);
		add(textArea_1);
		
		bgSearch.add(rdbtnLocal);
		bgSearch.add(rdbtnPubmed);
		
		JLabel lbsl = new JLabel("Select Algorithm :");
		lbsl.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lbsl.setBounds(127, 230, 132, 23);
		add(lbsl);
		
		JRadioButton rdKnn = new JRadioButton("KNN");
		rdKnn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		rdKnn.setBounds(651, 231, 66, 23);
		add(rdKnn);
		
		JRadioButton rdBM25 = new JRadioButton("BM25+");
		rdBM25.setFont(new Font("Tahoma", Font.PLAIN, 14));
		rdBM25.setBounds(279, 231, 74, 23);
		add(rdBM25);
		
		JRadioButton rdCosine = new JRadioButton("Cosine Similarity");
		rdCosine.setFont(new Font("Tahoma", Font.PLAIN, 14));
		rdCosine.setBounds(372, 231, 132, 23);
		add(rdCosine);
		
		JRadioButton rdNaive = new JRadioButton("Naive Bayes");
		rdNaive.setFont(new Font("Tahoma", Font.PLAIN, 14));
		rdNaive.setBounds(521, 231, 109, 23);
		add(rdNaive);
		
		JRadioButton rdDeep = new JRadioButton("Deep learning");
		rdDeep.setFont(new Font("Tahoma", Font.PLAIN, 14));
		rdDeep.setBounds(737, 231, 109, 23);
		add(rdDeep);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(obj.equals(btnBack)) {
			setVisible(false);
			app.wTrainModelPanel.setVisible(true);
		}
		
		if(obj.equals(btnProcess)) {
			//System.out.println("Click Process... 2");
		}
		if(obj.equals(btnSearch)) {
			fileChooser.setCurrentDirectory(new java.io.File("."));
			//fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			
			fileChooser.setAcceptAllFileFilterUsed(false);
			if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			      txtSearch.replaceSelection(""+ fileChooser.getSelectedFile());
			}else {
			      System.out.println("No Selection ");
			}
		}
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		bg = new ImageIcon(getClass().getResource("usege_bg.jpg"))
				.getImage();
		g.drawImage(bg, 0, 0, null);
		g.setColor(Color.WHITE);
	}
}
