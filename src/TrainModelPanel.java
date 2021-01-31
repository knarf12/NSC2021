import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;
import javax.swing.JRadioButton;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import com.jgoodies.forms.factories.DefaultComponentFactory;

public class TrainModelPanel extends JPanel implements ActionListener{
	
	MyGUI app;
	Image bg;
	static int number= 1;
	JButton btnProcess;
	JButton btnNext;
	JTextField txtSearch;
	JButton btnSearch;
	private JFileChooser fileChooser = new JFileChooser();  
	private JTextField textField;
	private JTextField textField_1;
	static JTextField txtRetmax;
	private handlerAction h = new handlerAction();
	private ButtonGroup bgSearch = new ButtonGroup();
	private ButtonGroup bgDoc = new ButtonGroup();
	private ButtonGroup bgWeight = new ButtonGroup();
	static ButtonGroup bgWeight2 = new ButtonGroup();
	private midProcess process;
	private static JTextArea textArea_1;
	private static JTextArea textArea;
	private static JRadioButton rdNV, rdKNN , rdBM25, rdCS;
	private static JTextField tftotal;
	private static JTextField tfload;
	
	
	public TrainModelPanel(MyGUI app) {
		this.app = app;
		initPanel();
	}
	
	void initPanel() {
		
		setSize(1024, 768);
		setLayout(null);
		
		Icon iconProcess = new ImageIcon(getClass().getResource("btn_process_model.png"));
		btnProcess = new JButton(iconProcess);
		btnProcess.setBounds(40, 639, 238, 45);
		btnProcess.setOpaque(false);
		btnProcess.setContentAreaFilled(false);
		btnProcess.setBorderPainted(false);
		btnProcess.addActionListener(this);
		add(btnProcess);
		
		Icon iconNext = new ImageIcon(getClass().getResource("btn_next_model.png"));
		btnNext = new JButton(iconNext);
		btnNext.setBounds(859, 639, 135, 45);
		btnNext.setOpaque(false);
		btnNext.setContentAreaFilled(false);
		btnNext.setBorderPainted(false);
		btnNext.addActionListener(this);
		add(btnNext);
		
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
		
		JRadioButton rdbtnLocal = new JRadioButton("Local Device");
		rdbtnLocal.setFont(new Font("Tahoma", Font.PLAIN, 14));
		rdbtnLocal.setBounds(215, 187, 114, 23);
		rdbtnLocal.addActionListener(h);
		add(rdbtnLocal);
		
		JRadioButton rdbtnPubmed = new JRadioButton("Pubmed");
		rdbtnPubmed.setFont(new Font("Tahoma", Font.PLAIN, 14));
		rdbtnPubmed.setBounds(331, 187, 91, 23);
		rdbtnPubmed.addActionListener(h);
		add(rdbtnPubmed);
		
		JLabel lbSource = new JLabel("Source Selection :");
		lbSource.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lbSource.setBounds(78, 194, 145, 14);
		add(lbSource);
		
		textField = new JTextField();
		textField.setBounds(368, 230, 86, 37);
		add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(492, 230, 86, 37);
		add(textField_1);
		
		txtRetmax = new JTextField();
		txtRetmax.setColumns(10);
		txtRetmax.setBounds(772, 230, 86, 37);
		//txtRetmax.setEditable(false);
		add(txtRetmax);
		
		JLabel lblNewLabel = new JLabel("Year Duration");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel.setBounds(244, 242, 114, 14);
		add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("to");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(464, 241, 33, 14);
		add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Number of documents");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_2.setBounds(605, 239, 158, 14);
		add(lblNewLabel_2);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(306, 465, 680, 155);
		
		textArea_1 = new JTextArea();
		textArea_1.setFont(new Font("Consolas", Font.PLAIN, 13));
		scrollPane_2.setViewportView(textArea_1);
		add(scrollPane_2);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		panel_1.setLayout(null);
		panel_1.setBounds(51, 297, 215, 134);
		
		
		TitledBorder Context,Technique;
		Border blackline;
		blackline = BorderFactory.createLineBorder(Color.black);
		Context = BorderFactory.createTitledBorder(
                blackline, "Types of Documents" );
		Context.setTitleFont(new Font("Consolas", Font.PLAIN, 14));
		Context.setTitleJustification(TitledBorder.CENTER);
		panel_1.setBorder(Context);
		add(panel_1);
		
		JRadioButton rdbtnST = new JRadioButton("Symptoms");
		rdbtnST.setFont(new Font("Tahoma", Font.PLAIN, 12));
		rdbtnST.setBounds(16, 21, 109, 23);
		rdbtnST.addActionListener(h);
		panel_1.add(rdbtnST);
		
		JRadioButton rdbtnDg = new JRadioButton("Diagnosis");
		rdbtnDg.setFont(new Font("Tahoma", Font.PLAIN, 12));
		rdbtnDg.setBounds(16, 49, 109, 23);
		rdbtnDg.addActionListener(h);
		panel_1.add(rdbtnDg);
		
		JRadioButton rdbtnRe = new JRadioButton("Reflection");
		rdbtnRe.setFont(new Font("Tahoma", Font.PLAIN, 12));
		rdbtnRe.setBounds(16, 77, 109, 23);
		rdbtnRe.addActionListener(h);
		panel_1.add(rdbtnRe);
		
		JRadioButton rdbtnAll = new JRadioButton("All");
		rdbtnAll.setSelected(true);
		rdbtnAll.setFont(new Font("Tahoma", Font.PLAIN, 12));
		rdbtnAll.setBounds(16, 103, 109, 23);
		rdbtnAll.addActionListener(h);
		panel_1.add(rdbtnAll);
		
		
		bgSearch.add(rdbtnLocal);
		bgSearch.add(rdbtnPubmed);
		
		bgDoc.add(rdbtnAll);
		bgDoc.add(rdbtnRe);
		bgDoc.add(rdbtnDg);
		bgDoc.add(rdbtnST);
		
		JPanel panel_1_1 = new JPanel();
		panel_1_1.setBackground(Color.WHITE);
		panel_1_1.setLayout(null);
		panel_1_1.setBounds(51, 442, 215, 178);
		add(panel_1_1);
		
		JRadioButton rdbtnDeep = new JRadioButton("Deep Learning");
		rdbtnDeep.setFont(new Font("Tahoma", Font.PLAIN, 12));
		rdbtnDeep.setBounds(18, 148, 136, 23);
		rdbtnDeep.addActionListener(h);
		panel_1_1.add(rdbtnDeep);
		
		JRadioButton rdbtnUTW = new JRadioButton("Unsupervised Learning");
		rdbtnUTW.setFont(new Font("Tahoma", Font.PLAIN, 12));
		rdbtnUTW.setBounds(18, 70, 172, 23);
		rdbtnUTW.addActionListener(h);
		panel_1_1.add(rdbtnUTW);
		//rdbtnUTW.setEnabled(false);
		
		JRadioButton rdbtnSTW = new JRadioButton("Supervised Learning");
		rdbtnSTW.setFont(new Font("Tahoma", Font.PLAIN, 12));
		rdbtnSTW.setBounds(18, 22, 172, 23);
		rdbtnSTW.addActionListener(h);
		panel_1_1.add(rdbtnSTW);
		//rdbtnSTW.setEnabled(false);
		
		
		Technique = BorderFactory.createTitledBorder(
                blackline, "Selection Technique");
		Technique.setTitleFont(new Font("Consolas", Font.PLAIN, 14));
		Technique.setTitleJustification(TitledBorder.CENTER);
		panel_1_1.setBorder(Technique);
		
		rdCS = new JRadioButton("Cosine Similarity");
		rdCS.setFont(new Font("Tahoma", Font.PLAIN, 12));
		rdCS.setBounds(45, 96, 145, 23);
		rdCS.addActionListener(h);
		panel_1_1.add(rdCS);
		
		rdBM25 = new JRadioButton("BM25+ Similarity");
		rdBM25.setFont(new Font("Tahoma", Font.PLAIN, 12));
		rdBM25.setBounds(45, 122, 145, 23);
		rdBM25.addActionListener(h);
		panel_1_1.add(rdBM25);
		
		rdKNN = new JRadioButton("KNN");
		rdKNN.setFont(new Font("Tahoma", Font.PLAIN, 12));
		rdKNN.setBounds(45, 44, 50, 23);
		rdKNN.addActionListener(h);
		panel_1_1.add(rdKNN);
		
		rdNV = new JRadioButton("Naive Bayes");
		rdNV.setFont(new Font("Tahoma", Font.PLAIN, 12));
		rdNV.setBounds(104, 44, 105, 23);
		rdNV.addActionListener(h);
		panel_1_1.add(rdNV);
		
		bgWeight2.add(rdBM25);
		bgWeight2.add(rdNV);
		bgWeight2.add(rdKNN);
		bgWeight2.add(rdCS);
		
		bgWeight.add(rdbtnDeep);
		bgWeight.add(rdbtnUTW);
		bgWeight.add(rdbtnSTW);
		
		textArea = new JTextArea();
		textArea.setFont(new Font("Consolas", Font.PLAIN, 13));
		textArea.setEditable(false);
		textArea_1.setEditable(false);
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(306, 297, 680, 139);
		scrollPane_1.setViewportView(textArea);
		add(scrollPane_1);
		
		JLabel lblNewLabel_3 = new JLabel("Total :");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_3.setBounds(645, 650, 46, 14);
		add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("Total :");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_4.setBounds(645, 440, 46, 14);
		add(lblNewLabel_4);
		
		tftotal = new JTextField();
		tftotal.setFont(new Font("Tahoma", Font.PLAIN, 9));
		tftotal.setBounds(690, 648, 70, 23);
		tftotal.setEditable(false);
		add(tftotal);
		tftotal.setColumns(10);
		
		tfload = new JTextField();
		tfload.setFont(new Font("Tahoma", Font.PLAIN, 9));
		tfload.setColumns(10);
		tfload.setEditable(false);
		tfload.setBounds(690, 439, 70, 23);
		add(tfload);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(obj.equals(btnNext)) {
			setVisible(false);
			
			app.wUsageModelPanel.setVisible(true);
		}
	
		if(obj.equals(btnProcess)) {
			//System.out.println("Click Process... 1");
			if (h.choice.equalsIgnoreCase("local") && txtSearch.getText().length() > 10) {
				//System.out.println("local");
				try {
					 process=new midProcess(txtSearch.getText());
					 setText2(""+midProcess.nameDoc.size());
				} catch (ParserConfigurationException | SAXException | IOException e1) {
					e1.printStackTrace();
				}
					
			}else if (h.choice.equalsIgnoreCase("Pubmed") && WebClawer.done){
				try {
					process=new midProcess("./data/dlData/");
					setText2(""+midProcess.nameDoc.size());
				} catch (ParserConfigurationException | SAXException | IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		if(obj.equals(btnSearch)) {
			if (h.choice.equalsIgnoreCase("Pubmed")) {
				//System.out.println("Pubmed");
				if(txtSearch.getText().length()>2 && txtRetmax.getText().length() >= 1) {
					try {
						WebClawer WebClawer = new WebClawer(txtSearch.getText(),Integer.parseInt(txtRetmax.getText()));
					} catch (NumberFormatException | IOException | InterruptedException | ParserConfigurationException
							| TransformerException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}else if (h.choice.equalsIgnoreCase("local")){
				fileChooser.setCurrentDirectory(new java.io.File("."));
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setAcceptAllFileFilterUsed(false);
				
				if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				      txtSearch.replaceSelection(""+ fileChooser.getSelectedFile());
				}else {
				      //System.out.println("No Selection ");
				}
			}
		}
	}
	
	protected static void addTextArea(ArrayList<String> Docid) {
		for (String string : Docid) {
			textArea.append("  https://pubmed.ncbi.nlm.nih.gov/"+string+"\n");
		}
	}
	
	protected static void clearTextArea() {
		textArea_1.setText("");
	}
	
	protected static void setText(String s) {
		tftotal.setText(s);
	}
	
	protected static void setText2(String s) {
		tfload.setText(s);
	}
	
	protected static void addTextAreaWC(String name) {
			textArea.append("  https://pubmed.ncbi.nlm.nih.gov/"+name+"\n");
	}
	
	protected static void addTextAreaWC2(String name) {
		textArea_1.append("  https://pubmed.ncbi.nlm.nih.gov/"+name+"\n");
		number +=1;
}
	
	protected static void setFalse(String name) {
		bgWeight2.clearSelection();
		if(name.equalsIgnoreCase("STW")) {
			rdKNN.setSelected(true);
			rdBM25.setSelected(false);
			rdCS.setSelected(false);
		}else if(name.equalsIgnoreCase("UTW")) {
			rdCS.setSelected(true);
			rdKNN.setSelected(false);
			rdNV.setSelected(false);
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		bg = new ImageIcon(getClass().getResource("model_bg.jpg"))
				.getImage();
		g.drawImage(bg, 0, 0, null);
		g.setColor(Color.WHITE);
	}
}
