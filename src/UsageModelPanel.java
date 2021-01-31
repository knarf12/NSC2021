import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class UsageModelPanel extends JPanel implements ActionListener{
	
	MyGUI app;
	Image bg;
	
	JButton btnProcess;
	JButton btnBack;
	static JTextField txtSearch;
	JButton btnSearch;
	private JRadioButton rdbtnLocal;
	private JRadioButton rdbtnPubmed;
	private static JTextArea textArea;
	private static JTextArea textArea_1;
	private JFileChooser fileChooser = new JFileChooser(); 
	private ButtonGroup bgSearch = new ButtonGroup();
	private ButtonGroup bgTec = new ButtonGroup();
	handlerActionSentence hsentence = new handlerActionSentence();
	boolean selected = false;
	
	
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
		rdbtnLocal.addActionListener(hsentence);
		add(rdbtnLocal);
		
		rdbtnPubmed = new JRadioButton("Multi Documents");
		rdbtnPubmed.setFont(new Font("Tahoma", Font.PLAIN, 14));
		rdbtnPubmed.setBounds(279, 190, 143, 23);
		rdbtnPubmed.addActionListener(hsentence);
		add(rdbtnPubmed);
		
		textArea = new JTextArea();
		textArea.setBackground(Color.WHITE);
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		//textArea.setBounds(333, 313, 504, 373);
		//add(textArea);
		//textArea.setText("1234");
		
		textArea_1 = new JTextArea();
		textArea_1.setBackground(Color.WHITE);
		textArea_1.setEditable(false);
		//textArea_1.setBounds(54, 315, 227, 373);
		//add(textArea_1);
		
		bgSearch.add(rdbtnLocal);
		bgSearch.add(rdbtnPubmed);
		
		JLabel lbsl = new JLabel("Select Algorithm :");
		lbsl.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lbsl.setBounds(127, 230, 132, 23);
		add(lbsl);
		
		JRadioButton rdKnn = new JRadioButton("KNN");
		rdKnn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		rdKnn.setBounds(651, 231, 66, 23);
		rdKnn.addActionListener(hsentence);
		add(rdKnn);
		
		JRadioButton rdBM25 = new JRadioButton("BM25+");
		rdBM25.setFont(new Font("Tahoma", Font.PLAIN, 14));
		rdBM25.setBounds(279, 231, 74, 23);
		rdBM25.addActionListener(hsentence);
		add(rdBM25);
		
		JRadioButton rdCosine = new JRadioButton("Cosine Similarity");
		rdCosine.setFont(new Font("Tahoma", Font.PLAIN, 14));
		rdCosine.setBounds(372, 231, 132, 23);
		rdCosine.addActionListener(hsentence);
		add(rdCosine);
		
		JRadioButton rdNaive = new JRadioButton("Naive Bayes");
		rdNaive.setFont(new Font("Tahoma", Font.PLAIN, 14));
		rdNaive.setBounds(521, 231, 109, 23);
		rdNaive.addActionListener(hsentence);
		add(rdNaive);
		
		JRadioButton rdDeep = new JRadioButton("Deep learning");
		rdDeep.setFont(new Font("Tahoma", Font.PLAIN, 14));
		rdDeep.setBounds(737, 231, 109, 23);
		rdDeep.addActionListener(hsentence);
		add(rdDeep);
		
		bgTec.add(rdBM25);
		bgTec.add(rdDeep);
		bgTec.add(rdNaive);
		bgTec.add(rdCosine);
		bgTec.add(rdKnn);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(54, 320, 224, 375);
		scrollPane.setViewportView(textArea);
		add(scrollPane);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(333, 320, 513, 375);
		scrollPane_1.setViewportView(textArea_1);
		add(scrollPane_1);
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
			if(hsentence.docchoice.equalsIgnoreCase("singleDoc") && selected) {
				System.out.println("siggle true");
				try {
					ClassifierSentence classifierSentence = new ClassifierSentence(txtSearch.getText());
				} catch (SAXException | IOException | ParserConfigurationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}else if(hsentence.docchoice.equalsIgnoreCase("mutiDoc") && selected) {
				System.out.println("muti true");
			}
		}
		if(obj.equals(btnSearch)) {
			setSearch();
			if(hsentence.docchoice.equalsIgnoreCase("singleDoc")) {
				
				fileChooser.setCurrentDirectory(new java.io.File("."));
				//fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setAcceptAllFileFilterUsed(false);
				if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				      txtSearch.replaceSelection(""+ fileChooser.getSelectedFile());
				      selected =true;
				}else {
				      System.out.println("No Selection ");
				      selected =false;
				}
			}else if(hsentence.docchoice.equalsIgnoreCase("mutiDoc")) {
				fileChooser.setCurrentDirectory(new java.io.File("."));
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setAcceptAllFileFilterUsed(false);
				if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				      txtSearch.replaceSelection(""+ fileChooser.getSelectedFile());
				      selected =true;
				}else {
				      System.out.println("No Selection ");
				      selected =false;
				}
			}
		}
	}
	
	protected static void setSearch() {
		// TODO Auto-generated method stub
		txtSearch.setText("");
	}
	
	protected static void clearDoc() {
		textArea.setText("");
	}
	
	protected static void clearSentence() {
		textArea_1.setText("");
	}
	
	@Override
	public void paintComponent(Graphics g) {
		bg = new ImageIcon(getClass().getResource("usege_bg.jpg"))
				.getImage();
		g.drawImage(bg, 0, 0, null);
		g.setColor(Color.WHITE);
	}
	
	public static void showDoc(String Doc) {
		// TODO Auto-generated method stub
		textArea.append(Doc+"\n");
	}
	
	public static void showSentence(String Doc) {
		// TODO Auto-generated method stub
		textArea_1.append(Doc+"\n");
	}
}
