import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;  
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;  
import org.w3c.dom.Element; 

public class ClassifierSentence {
	private static List<String> stopword;
	private static HashMap<Integer, ArrayList<String>> singleDoc = new HashMap<Integer, ArrayList<String>>();
	
	private static HashMap<Integer, ArrayList<Integer>> VtDoclist = new HashMap<Integer, ArrayList<Integer>>();
	private static HashMap<Integer, ArrayList<Integer>> VtDocTr = new HashMap<Integer, ArrayList<Integer>>();
	private static HashMap<Integer, ArrayList<String>> sigleType = new HashMap<Integer, ArrayList<String>>();
	static String type = "";
	
	private static ArrayList<String> originalDoc = new ArrayList<String>();
	private static ArrayList<String> wordList = new ArrayList<String>();
	static ArrayList<String> DocsTest = new ArrayList<String>();
	
	static HashMap<Integer, ArrayList<Double>> evaluaCosine = new HashMap<Integer, ArrayList<Double>>();
	static HashMap<Integer, ArrayList<Double>> evaluaBM25 = new HashMap<Integer, ArrayList<Double>>();
	
	//static HashMap<Integer, String> docKNN = new HashMap<Integer, String>();
	
	static ArrayList<Double> evaluaKNN = new ArrayList<>();
	static ArrayList<Double> evaluaNVB = new ArrayList<>();
	
	public ClassifierSentence(){
		
	}
	
	protected static void processSingle(String path) {
		// TODO Auto-generated method stub
		try {
			stopword = Files.readAllLines(Paths.get("./dic/stopwordAndSpc_eng.txt"));
			loadSingleFile(path);
			if (type.equalsIgnoreCase("diagnosis")) {
				loadTrainset("./data/data-Diag");
			}else if (type.equalsIgnoreCase("reflection")) {
				loadTrainset("./data/data-Reflec");
			}else if (type.equalsIgnoreCase("symptom")) {
				loadTrainset("./data/data-Sympt");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		
		//vector
		for (int i = 0; i < singleDoc.size(); i++) {
			VtDoclist.put(i,mathMethod.getVector(singleDoc.get(i), wordList));
		}
		
		for (int i = 0; i < sigleType.size(); i++) {
			VtDocTr.put(i, mathMethod.getVector(sigleType.get(i), wordList));
		}
		//end vector

		//cosine
		for (int i = 0; i < VtDoclist.size(); i++) {
			ArrayList<Double> similar = new ArrayList<Double>();
			for (int j = 0; j < sigleType.size(); j++) {
				similar.add(mathMethod.CosineSim(VtDocTr.get(j), VtDoclist.get(i)));
			}
			evaluaCosine.put(i, similar);
		}
		//end
		
		//bm25+
		bm25F.DocAVGST(VtDoclist);
		for (int j = 0; j < VtDoclist.size(); j++) {
			ArrayList<Double> similar = new ArrayList<Double>();
			for (int j2 = 0; j2 < VtDocTr.size(); j2++) {
				similar.add(bm25F.BM25PlusST(VtDocTr.get(j2), VtDoclist.get(j), bm25F.getAllST(VtDoclist)));
			}
			evaluaBM25.put(j, similar);
		}
		//end
		
		//KNN
		//System.out.println(VtDocTr.size());
		for (int i = 0; i < VtDoclist.size(); i++) {
			evaluaKNN.add(knnAlgorithm.checkKNN(VtDoclist.get(i), VtDocTr));
			//System.out.println(knnAlgorithm.checkKNN(VtDoclist.get(i), VtDocTr));
			evaluaNVB.add(NaiveBayes.NVBST(VtDoclist.get(i), VtDocTr));
			
		}
		//end 
	}
	
	protected static void getSTKNN() {
		// <= 4.0
		//System.out.println("KNN");
		for (int i = 0; i < evaluaKNN.size(); i++) {
			if (evaluaKNN.get(i)<=4.0) {
				if(originalDoc.get(i).length() > 10 ) {
					UsageModelPanel.showSentence(originalDoc.get(i));
				}
			}
		}
	}
	
	protected static void getSTCosine() {
		//System.out.println("getSTCosine");
		for (int i = 0; i < evaluaCosine.size(); i++) {
			int d=0;
			for (int j = 0; j <  evaluaCosine.get(1).size(); j++) {
				//System.out.println(evaluaCosine.get(i).get(j));
				if(evaluaCosine.get(i).get(j) >= 0.1) {
					d=1;
				}
			}
			if(d==1) {
				//System.out.println(originalDoc.get(i));
				if(originalDoc.get(i).length() > 10 ) {
					UsageModelPanel.showSentence(originalDoc.get(i));
				}
			}
		}
	}
	
	protected static void getSTNVB() {
		//System.out.println("NVB");
		for (int i = 0; i < evaluaNVB.size(); i++) {
			String str =  evaluaNVB.get(i).toString();
			String []spl =  str.split("\\.");
			if (Integer.parseInt(spl[0]) > 4) {
				//System.out.println(originalDoc.get(i));
				if(originalDoc.get(i).length() > 10 ) {
					UsageModelPanel.showSentence(originalDoc.get(i));
				}
			}
		}
	}
	
	protected static void getSTBM25() {
		
		DecimalFormat df2 = new DecimalFormat("#.##");
		for (int i = 0; i < evaluaBM25.size(); i++) {
			Double sum = 0.0d;
			int d=0;
			for (int k = 0; k < evaluaBM25.get(0).size(); k++) {
				
				sum += sum + evaluaBM25.get(i).get(k);
				if(evaluaBM25.get(i).get(k)>15.0d) {
					d=1;
				}
			}
			if(d==1) {
				//System.out.println(originalDoc.get(i));
				if(originalDoc.get(i).length() > 10 ) {
					UsageModelPanel.showSentence(originalDoc.get(i));
				}
			}
		}
	}
	
	static void loadSingleFile(String path) throws SAXException, IOException, ParserConfigurationException{
		ArrayList<String> data= new ArrayList<>();
		File file = new File(path);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
		
		//an instance of builder to parse the specified xml file  
		DocumentBuilder db = dbf.newDocumentBuilder();  
		Document doc = db.parse(file);  
		doc.getDocumentElement().normalize();  
		NodeList nodeList = doc.getElementsByTagName("Doc");  
		data.addAll(ReadXML(nodeList));
		String Doc = data.get(0);
		
		UsageModelPanel.showDoc(Doc);
		int i = 0;
		for(String sentence : Doc.split("[:\\.]")) {
			//System.out.println(sentence);
			originalDoc.add(sentence);
			ArrayList<String> oneDoc = new ArrayList<String>();
			for (String word : sentence.split("[\\-\\[,.() ; % : / \t]")) {
				word = word.toLowerCase();
				if(word.length()>1 && ! mathMethod.isNumeric(word)) {
					oneDoc.add(word);
					if(!wordList.contains(word)) {
						wordList.add(word);
					}
				}
			}
			data.removeAll(stopword);
			singleDoc.put(i, oneDoc);
			i+=1;
		}
	}
	
	private static void loadTrainset(String path) throws IOException{
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
        BufferedReader br ;
        int kk=0;
        for(int i = 0; i < listOfFiles.length; i++){
            String filename = listOfFiles[i].getName();
            
            if(filename.endsWith(".txt")||filename.endsWith(".TXT")) {
            	
                br = new BufferedReader(new FileReader(path+"/"+filename));
                String line = br.readLine();
                for(String sentence : line.split("[:\\.]")) {
                	ArrayList<String> oneDoc = new ArrayList<String>();
	    			for (String word : sentence.split("[,.() ; % : / \t -]")) {
						word = word.toLowerCase();
						if(word.length()>1 && ! mathMethod.isNumeric(word)) {
							oneDoc.add(word);
							if(!wordList.contains(word)) {
								wordList.add(word);
							}
						}
	    			}
					oneDoc.removeAll(stopword);
					sigleType.put(kk, oneDoc);
					kk +=1;
                }
    			br.close();
            }
        }
	}
	
	
	public static ArrayList<String> ReadXML(NodeList nodeList) {
    	ArrayList<String> Data = new  ArrayList<String>();
    	for (int itr = 0; itr < nodeList.getLength(); itr++)   
    	{  
	    	Node node = nodeList.item(itr);
	    	if (node.getNodeType() == Node.ELEMENT_NODE)   
	    	{  
	    		Element eElement = (Element) node; 
	    		if(eElement.getElementsByTagName("Abstract").item(0).getTextContent().length() > 1) {
	    			Data.add(eElement.getElementsByTagName("Abstract").item(0).getTextContent()); 
	    			type = eElement.getElementsByTagName("Class").item(0).getTextContent();
	    		}
	    	}
    	}
		return Data;
	}
}
