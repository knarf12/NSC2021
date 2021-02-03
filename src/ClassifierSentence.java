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
	private static HashMap<Integer, ArrayList<String>> singleDoc ;
	
	private static HashMap<Integer, ArrayList<Integer>> VtDoclist ;
	private static HashMap<Integer, ArrayList<Integer>> VtDocTr ;
	private static HashMap<Integer, ArrayList<String>> sigleType ;
	static String type = "";
	
	private static ArrayList<String> originalDoc = new ArrayList<String>();
	private static ArrayList<String> wordList = new ArrayList<String>();
	static ArrayList<String> DocsTest = new ArrayList<String>();
	
	static HashMap<Integer, ArrayList<Double>> evaluaCosine;
	static HashMap<Integer, ArrayList<Double>> evaluaBM25 ;
	static double[] maxBM25;
//	static double[] maxKNN;
//	static double[] maxNVB;
	static double[] maxCS;
	//static HashMap<Integer, String> docKNN = new HashMap<Integer, String>();
	
	static ArrayList<Double> evaluaKNN ;
	static ArrayList<Double> evaluaNVB ;
	
	public ClassifierSentence(){
		
	}
	
	protected static void processSingle(String path) {
		// TODO Auto-generated method stub
		singleDoc = new HashMap<Integer, ArrayList<String>>();
		VtDoclist = new HashMap<Integer, ArrayList<Integer>>();
		VtDocTr = new HashMap<Integer, ArrayList<Integer>>();
		sigleType = new HashMap<Integer, ArrayList<String>>();
		
		evaluaCosine = new HashMap<Integer, ArrayList<Double>>();
		evaluaBM25 = new HashMap<Integer, ArrayList<Double>>();
		evaluaKNN = new ArrayList<>();
		evaluaNVB = new ArrayList<>();
		
		try {
			stopword = Files.readAllLines(Paths.get("./dic/stopwordAndSpc_eng.txt"));
			loadSingleFile(path);
			if (type.equalsIgnoreCase("diagnosis")) {
				//System.out.println(type);
				loadTrainset("./data/SentenceTrain/data-Diag");
			}else if (type.equalsIgnoreCase("reflection")) {
				loadTrainset("./data/SentenceTrain/data-Reflec");
			}else if (type.equalsIgnoreCase("symptom")) {
				loadTrainset("./data/SentenceTrain/data-Sympt");
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
		maxCS = new double[VtDoclist.size()];
		for (int i = 0; i < VtDoclist.size(); i++) {
			ArrayList<Double> similar = new ArrayList<Double>();
			for (int j = 0; j < sigleType.size(); j++) {
				similar.add(mathMethod.CosineSim(VtDocTr.get(j), VtDoclist.get(i)));
				
			}
			//System.out.println(i);
			//maxCS[i] = bubbleSort(similar);
			evaluaCosine.put(i, similar);
		}
		//end
		
		//bm25+
		bm25F.DocAVGST(VtDoclist);
		maxBM25 = new double[VtDoclist.size()];
		for (int j = 0; j < VtDoclist.size(); j++) {
			ArrayList<Double> similar = new ArrayList<Double>();
			for (int j2 = 0; j2 < VtDocTr.size(); j2++) {
				//System.out.println();
				similar.add(bm25F.BM25PlusST(VtDocTr.get(j2), VtDoclist.get(j), bm25F.getAllST(VtDoclist)));
			}
			//System.out.println(similar);
			maxBM25[j] = bubbleSort(similar);
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
		try {
			for (int i = 0; i < evaluaKNN.size(); i++) {
				System.out.println(i+" "+originalDoc.get(i) +" \n"+ evaluaKNN.get(i));
				if (evaluaKNN.get(i)<3.2) {
					if(originalDoc.get(i).length() > 17 ) {
						UsageModelPanel.showSentence(" "+originalDoc.get(i)+".");
					}
				}
			}
		} catch (IndexOutOfBoundsException e) {
			// TODO: handle exception
		}
		// <= 4.0
		//System.out.println("KNN");
		
	}
	
	protected static void getSTCosine() {
		try {
			for (int i = 0; i < evaluaCosine.size(); i++) {
				int d=0;
				for (int j = 0; j < evaluaCosine.get(1).size(); j++) {
					if(evaluaCosine.get(i).get(j)> 0.45) {
						d=1;
					}
				}
				if(d==1) {
					if(originalDoc.get(i).length() > 17 ) {
						UsageModelPanel.showSentence(" "+originalDoc.get(i)+".");
					}
				}
			}
		} catch (IndexOutOfBoundsException e) {
			// TODO: handle exception
		}
		
		
	}
	
	protected static void getSTNVB() {
		//System.out.println("NVB");
		try {
			for (int i = 0; i < evaluaNVB.size(); i++) {
				String str =  evaluaNVB.get(i).toString();
				String []spl =  str.split("\\.");
				if (Integer.parseInt(spl[0]) > 7) {
					//System.out.println(originalDoc.get(i));
					if(originalDoc.get(i).length() > 17 ) {
						UsageModelPanel.showSentence(" "+originalDoc.get(i)+".");
					}
				}
			}
		} catch (IndexOutOfBoundsException e) {
			// TODO: handle exception
		}
	
	}
	
	protected static void getSTBM25() {
		try {
			for (int i = 0; i < evaluaBM25.size(); i++) {
				int d=0;
				for (int j = 0; j < evaluaBM25.get(1).size(); j++) {
					if(evaluaBM25.get(i).get(j)> 22.5) {
						d=1;
					}
				}
				if(d==1 && i> 1) {
					if(originalDoc.get(i).length() > 17 ) {
						UsageModelPanel.showSentence(" "+originalDoc.get(i)+".");
					}
				}
			}
		} catch (IndexOutOfBoundsException e) {
			// TODO: handle exception
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
			for (String word : sentence.split("[\\-\\[,.();% : / \t]")) {
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
            //System.out.println(filename);
            if(filename.endsWith(".txt")||filename.endsWith(".TXT")) {
            	//System.out.println(filename.endsWith(".txt"));
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
	
	//sort
		public static double bubbleSort(ArrayList<Double> similar) {  
			double max[] =new double[similar.size()];
			for (int i = 0; i < max.length; i++) {
				if (similar.get(i).isNaN()) {
					max[i] = 0;
				}else {
					max[i] = similar.get(i);
				}
			}
	        int n = max.length;  
	        double temp = 0; 
	        
	         for(int i=0; i < n; i++){  
	                 for(int j=1; j < (n-i); j++){  
	                          if(max[j-1] < max[j]){  
	                                 //swap elements  
	                                 temp = max[j-1];  
	                                 max[j-1] = max[j];  
	                                 max[j] = temp; 
	                         }    
	                 }
	         }
	         //System.out.println(max[0]);
	         //System.out.println(max[1]);
	         return max[0];  
	    }
}
