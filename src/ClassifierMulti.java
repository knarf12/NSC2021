import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ClassifierMulti {
	private static List<String> stopword;
	
	//original sentence
	private static ArrayList<String> originalsympST;
	private static ArrayList<String> originaldiagST;
	private static ArrayList<String> originalreflecST;
	
	//link doc
	static ArrayList<String> nameDoc;
	//sentence doc train
	private static HashMap<Integer, ArrayList<String>> reflecType ;
	private static HashMap<Integer, ArrayList<String>> diagType ;
	private static HashMap<Integer, ArrayList<String>> sympType ;
	
	//sentence doc test
	private static HashMap<Integer, ArrayList<String>> sympST ;
	private static HashMap<Integer, ArrayList<String>> diagST ;
	private static HashMap<Integer, ArrayList<String>> reflecST ;
	
	//vector train and test
	private static HashMap<Integer, ArrayList<Integer>> Vt_sympST ;
	private static HashMap<Integer, ArrayList<Integer>> Vt_diagST ;
	private static HashMap<Integer, ArrayList<Integer>> Vt_reflecST ;
	
	private static HashMap<Integer, ArrayList<Integer>> VtTr_sympST ;
	private static HashMap<Integer, ArrayList<Integer>> VtTr_diagST ;
	private static HashMap<Integer, ArrayList<Integer>> VtTr_reflecST ;
	
	//doc test all
	private static ArrayList<String> sympDoc;
	private static ArrayList<String> diagDoc;
	private static ArrayList<String> reflecDoc;
	
	//word all 
	private static ArrayList<String> wordListsymp;
	private static ArrayList<String> wordListdiag;
	private static ArrayList<String> wordListreflec;
	
	static HashMap<Integer, ArrayList<Double>> evaluaCosinesymp;
	static HashMap<Integer, ArrayList<Double>> evaluaBM25symp;
	static ArrayList<Double> evaluaKNNsymp;
	static ArrayList<Double> evaluaNVBsymp;
	
	static HashMap<Integer, ArrayList<Double>> evaluaCosinediag;
	static HashMap<Integer, ArrayList<Double>> evaluaBM25diag;
	static ArrayList<Double> evaluaKNNdiag;
	static ArrayList<Double> evaluaNVBdiag;
	
	static HashMap<Integer, ArrayList<Double>> evaluaCosinereflec;
	static HashMap<Integer, ArrayList<Double>> evaluaBM25reflec;
	static ArrayList<Double> evaluaKNNreflec;
	static ArrayList<Double> evaluaNVBreflec;
	
	
	protected static void processMulti(String path) throws ParserConfigurationException, SAXException, IOException {
		
		originalsympST = new ArrayList<String>();
		originaldiagST = new ArrayList<String>();
		originalreflecST = new ArrayList<String>();
		
		//link doc
		 nameDoc = new ArrayList<String>();
		//sentence doc train
		reflecType = new HashMap<Integer, ArrayList<String>>();
		diagType = new HashMap<Integer, ArrayList<String>>();
		sympType = new HashMap<Integer, ArrayList<String>>();
		
		//sentence doc test
		sympST = new HashMap<Integer, ArrayList<String>>();
		diagST = new HashMap<Integer, ArrayList<String>>();
		reflecST = new HashMap<Integer, ArrayList<String>>();
		
		//vector train and test
		Vt_sympST = new HashMap<Integer, ArrayList<Integer>>();
		Vt_diagST = new HashMap<Integer, ArrayList<Integer>>();
		Vt_reflecST = new HashMap<Integer, ArrayList<Integer>>();
		
		VtTr_sympST = new HashMap<Integer, ArrayList<Integer>>();
		VtTr_diagST = new HashMap<Integer, ArrayList<Integer>>();
		VtTr_reflecST = new HashMap<Integer, ArrayList<Integer>>();
		
		//doc test all
		sympDoc = new ArrayList<String>();
		diagDoc = new ArrayList<String>();
		reflecDoc = new ArrayList<String>();
		
		//word all 
		wordListsymp = new ArrayList<String>();
		wordListdiag = new ArrayList<String>();
		wordListreflec = new ArrayList<String>();
		
		evaluaCosinesymp = new HashMap<Integer, ArrayList<Double>>();
		evaluaBM25symp = new HashMap<Integer, ArrayList<Double>>();
		evaluaKNNsymp = new ArrayList<>();
		evaluaNVBsymp = new ArrayList<>();
		
		evaluaCosinediag = new HashMap<Integer, ArrayList<Double>>();
		evaluaBM25diag = new HashMap<Integer, ArrayList<Double>>();
		evaluaKNNdiag = new ArrayList<>();
		evaluaNVBdiag = new ArrayList<>();
		
		evaluaCosinereflec = new HashMap<Integer, ArrayList<Double>>();
		evaluaBM25reflec = new HashMap<Integer, ArrayList<Double>>();
		evaluaKNNreflec = new ArrayList<>();
		evaluaNVBreflec = new ArrayList<>();
		
		
		
		
		stopword = Files.readAllLines(Paths.get("./dic/stopwordAndSpc_eng.txt"));
		loadMultiData(path);
		loadTrainsetMuti("./data/SentenceTrain/data-Diag",diagType,wordListdiag );
		loadTrainsetMuti("./data/SentenceTrain/data-Reflec",reflecType,wordListreflec );
		loadTrainsetMuti("./data/SentenceTrain/data-Sympt",sympType,wordListsymp );
		splitSentence();
		vectorforMulti(Vt_sympST, VtTr_sympST, sympType, sympST, evaluaCosinesymp, evaluaBM25symp,evaluaKNNsymp,evaluaNVBsymp, wordListsymp);
		vectorforMulti(Vt_diagST, VtTr_diagST, diagType, diagST, evaluaCosinediag, evaluaBM25diag,evaluaKNNdiag,evaluaNVBdiag, wordListdiag);
		vectorforMulti(Vt_reflecST, VtTr_reflecST, reflecType, reflecST, evaluaCosinereflec, evaluaBM25reflec, evaluaKNNreflec, evaluaNVBreflec, wordListreflec);
		
		
	}
	
	private static void vectorforMulti(
			HashMap<Integer, ArrayList<Integer>> VtDoclist, 
			HashMap<Integer, ArrayList<Integer>> VtDocTr,
			HashMap<Integer, ArrayList<String>> Type,
			HashMap<Integer, ArrayList<String>> ST,
			HashMap<Integer, ArrayList<Double>>evaluaCosine,
			HashMap<Integer, ArrayList<Double>>evaluaBM25,
			ArrayList<Double> evaluaKNN,
			ArrayList<Double> evaluaNVB,
			ArrayList<String> wordList
			) {
		// TODO Auto-generated method stub
		//vector
		for (int i = 0; i < ST.size(); i++) {
			VtDoclist.put(i,mathMethod.getVector(ST.get(i), wordList));
		}
		
		for (int i = 0; i < Type.size(); i++) {
			VtDocTr.put(i, mathMethod.getVector(Type.get(i), wordList));
		}
		//end vector
		
		//cosine
		for (int i = 0; i < VtDoclist.size(); i++) {
			ArrayList<Double> similar = new ArrayList<Double>();
			for (int j = 0; j < Type.size(); j++) {
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
		for (int i = 0; i < VtDoclist.size(); i++) {
			evaluaKNN.add(knnAlgorithm.checkKNN(VtDoclist.get(i), VtDocTr));
			evaluaNVB.add(NaiveBayes.NVBST(VtDoclist.get(i), VtDocTr));
			
		}
		//end 
		
	}
	
	//KNN
	protected static void getSTKNN() {
		// <= 4.0
		//System.out.println("KNN");
		UsageModelPanel.showSentence("Diagnosis :");
		for (int i = 0; i < evaluaKNNdiag.size(); i++) {
			//System.out.println(evaluaKNNdiag.get(i)<3.2);
			if (evaluaKNNdiag.get(i)<=3.2) {
				if(originaldiagST.get(i).length() > 15 ) {
					UsageModelPanel.showSentence(originaldiagST.get(i)+".");
				}
			}
		}
		UsageModelPanel.showSentence("\n");
		UsageModelPanel.showSentence("Reflection :");
		for (int i = 0; i < evaluaKNNreflec.size(); i++) {
			if (evaluaKNNreflec.get(i)<=3.2) {
				if(originalreflecST.get(i).length() > 15 ) {
					UsageModelPanel.showSentence(originalreflecST.get(i)+".");
				}
			}
		}
		UsageModelPanel.showSentence("\n");
		UsageModelPanel.showSentence("Symptom :");
		for (int i = 0; i < evaluaKNNsymp.size(); i++) {
			if (evaluaKNNsymp.get(i)<=3.2) {
				if(originalsympST.get(i).length() > 15 ) {
					UsageModelPanel.showSentence(originalsympST.get(i)+".");
				}
			}
		}
		UsageModelPanel.showSentence("\n");
	}
	
	
	//Cosine
	protected static void getSTCosine() {
		//System.out.println("getSTCosine");
		UsageModelPanel.showSentence("Diagnosis :");
		for (int i = 0; i < evaluaCosinediag.size(); i++) {
			int d=0;
			for (int j = 0; j <  evaluaCosinediag.get(1).size(); j++) {
				//System.out.println(evaluaCosine.get(i).get(j));
				if(evaluaCosinediag.get(i).get(j) >= 0.45) {
					d=1;
				}
			}
			if(d==1) {
				//System.out.println(originalDoc.get(i));
				if(originaldiagST.get(i).length() > 15 ) {
					UsageModelPanel.showSentence(originaldiagST.get(i)+".");
				}
			}
		}
		UsageModelPanel.showSentence("\n");
		UsageModelPanel.showSentence("Reflection :");
		for (int i = 0; i < evaluaCosinereflec.size(); i++) {
			int d=0;
			for (int j = 0; j <  evaluaCosinereflec.get(1).size(); j++) {
				//System.out.println(evaluaCosine.get(i).get(j));
				if(evaluaCosinereflec.get(i).get(j) >= 0.45) {
					d=1;
				}
			}
			if(d==1) {
				//System.out.println(originalDoc.get(i));
				if(originalreflecST.get(i).length() > 15 ) {
					UsageModelPanel.showSentence(originalreflecST.get(i)+".");
				}
			}
		}
		UsageModelPanel.showSentence("\n");
		UsageModelPanel.showSentence("Symptom :");
		for (int i = 0; i < evaluaCosinesymp.size(); i++) {
			int d=0;
			for (int j = 0; j <  evaluaCosinesymp.get(1).size(); j++) {
				//System.out.println(evaluaCosine.get(i).get(j));
				if(evaluaCosinesymp.get(i).get(j) >= 0.45) {
					d=1;
				}
			}
			if(d==1) {
				//System.out.println(originalDoc.get(i));
				if(originalsympST.get(i).length() >  15) {
					UsageModelPanel.showSentence(originalsympST.get(i)+".");
				}
			}
		}
		UsageModelPanel.showSentence("\n");
	}
	
	//NVB
	protected static void getSTNVB() {
		//System.out.println("NVB");
		UsageModelPanel.showSentence("Diagnosis :");
		try {
			for (int i = 0; i < evaluaNVBdiag.size(); i++) {
				String str =  evaluaNVBdiag.get(i).toString();
				String []spl =  str.split("\\.");
				if (Integer.parseInt(spl[0]) > 6) {
					//System.out.println(originalDoc.get(i));
					if(originaldiagST.get(i).length() > 17 ) {
						UsageModelPanel.showSentence(" "+originaldiagST.get(i)+".");
					}
				}
			}
		
			UsageModelPanel.showSentence("\n");
			UsageModelPanel.showSentence("Reflection :");
			for (int i = 0; i < evaluaNVBreflec.size(); i++) {
				String str =  evaluaNVBreflec.get(i).toString();
				String []spl =  str.split("\\.");
				if (Integer.parseInt(spl[0]) > 6) {
					//System.out.println(originalDoc.get(i));
					if(originalreflecST.get(i).length() > 17 ) {
						UsageModelPanel.showSentence(" "+originalreflecST.get(i)+".");
					}
				}
			}
			
			UsageModelPanel.showSentence("\n");
			UsageModelPanel.showSentence("Symptom :");
	
			for (int i = 0; i < evaluaNVBsymp.size(); i++) {
				String str =  evaluaNVBsymp.get(i).toString();
				String []spl =  str.split("\\.");
				if (Integer.parseInt(spl[0]) > 6) {
					//System.out.println(originalDoc.get(i));
					if(originalsympST.get(i).length() > 17 ) {
						UsageModelPanel.showSentence(" "+originalsympST.get(i)+".");
					}
				}
			}
		
		} catch (IndexOutOfBoundsException e) {
			// TODO: handle exception
		}
		UsageModelPanel.showSentence("\n");
	}
	
	
	//BM25+
	protected static void getSTBM25() {
		UsageModelPanel.showSentence("Diagnosis :");
		DecimalFormat df2 = new DecimalFormat("#.##");
		for (int i = 0; i < evaluaBM25diag.size(); i++) {
			Double sum = 0.0d;
			int d=0;
			for (int k = 0; k < evaluaBM25diag.get(0).size(); k++) {
				sum += sum + evaluaBM25diag.get(i).get(k);
				if(evaluaBM25diag.get(i).get(k)>=32.5d) {
					d=1;
				}
				//System.out.println(evaluaBM25diag.get(i).get(k));
			}
			//System.out.println();
			if(d==1) {
				//System.out.println(originalDoc.get(i));
				if(originaldiagST.get(i).length() > 15 ) {
					UsageModelPanel.showSentence(originaldiagST.get(i)+".");
				}
			}
		}
		UsageModelPanel.showSentence("\n");
		UsageModelPanel.showSentence("Reflection :");
		
		for (int i = 0; i < evaluaBM25reflec.size(); i++) {
			Double sum = 0.0d;
			int d=0;
			for (int k = 0; k < evaluaBM25reflec.get(0).size(); k++) {
				sum += sum + evaluaBM25reflec.get(i).get(k);
				if(evaluaBM25reflec.get(i).get(k)>=32.5d) {
					d=1;
				}
			}
			if(d==1) {
				//System.out.println(originalDoc.get(i));
				if(originalreflecST.get(i).length() >  15 ) {
					UsageModelPanel.showSentence(" "+originalreflecST.get(i)+".");
				}
			}
		}
		UsageModelPanel.showSentence("\n");
		UsageModelPanel.showSentence("Symptom :");
		
		for (int i = 0; i < evaluaBM25symp.size(); i++) {
			Double sum = 0.0d;
			int d=0;
			for (int k = 0; k < evaluaBM25symp.get(0).size(); k++) {
				sum += sum + evaluaBM25symp.get(i).get(k);
				if(evaluaBM25symp.get(i).get(k)>=32.5d) {
					d=1;
				}
			}
			if(d==1) {
				//System.out.println(originalDoc.get(i));
				if(originalsympST.get(i).length() >  15 ) {
					UsageModelPanel.showSentence(originalsympST.get(i)+".");
				}
			}
		}
		
	}
	
	private static void loadMultiData(String path) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder(); 
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
        for(int i = 0; i < listOfFiles.length; i++){
            String filename = listOfFiles[i].getName();
            if(filename.endsWith(".xml")||filename.endsWith(".XML")) {
//                System.out.println(filename);
                Document doc = db.parse(path+"/"+filename);  
            	doc.getDocumentElement().normalize(); 
            	NodeList nodeList = doc.getElementsByTagName("Doc");
            	ReadXMLMT(nodeList);
            }
        }
	}
	
	private static  void splitSentence() {
		// TODO Auto-generated method stub
		
		/// symptom
		int i = 0;
		for (String Doc : sympDoc) {
			for(String sentence : Doc.split("[:\\.]")) {
				//System.out.println(sentence);
				originalsympST.add(sentence);
				ArrayList<String> oneDoc = new ArrayList<String>();
				for (String word : sentence.split("[\\-\\[,.() ; % : / \t]")) {
					word = word.toLowerCase();
					if(word.length()>1 && ! mathMethod.isNumeric(word)) {
						oneDoc.add(word);
						if(!wordListsymp.contains(word)) {
							wordListsymp.add(word);
						}
					}
				}
				oneDoc.removeAll(stopword);
				sympST.put(i, oneDoc);
				i+=1;
			}
		}
		
		//diagnosis
		i = 0;
		for (String Doc : diagDoc) {
			
			for(String sentence : Doc.split("[:\\.]")) {
				//System.out.println(sentence);
				originaldiagST.add(sentence);
				ArrayList<String> oneDoc = new ArrayList<String>();
				for (String word : sentence.split("[\\-\\[,.() ; % : / \t]")) {
					word = word.toLowerCase();
					if(word.length()>1 && ! mathMethod.isNumeric(word)) {
						oneDoc.add(word);
						if(!wordListdiag.contains(word)) {
							wordListdiag.add(word);
						}
					}
				}
				oneDoc.removeAll(stopword);
				diagST.put(i, oneDoc);
				i+=1;
			}
		}
		
		//reflection
		i = 0;
		for (String Doc : reflecDoc) {
			for(String sentence : Doc.split("[:\\.]")) {
				//System.out.println(sentence);
				originalreflecST.add(sentence);
				ArrayList<String> oneDoc = new ArrayList<String>();
				for (String word : sentence.split("[\\-\\[,.() ; % : / \t]")) {
					word = word.toLowerCase();
					if(word.length()>1 && ! mathMethod.isNumeric(word)) {
						oneDoc.add(word);
						if(!wordListreflec.contains(word)) {
							wordListreflec.add(word);
						}
					}
				}
				oneDoc.removeAll(stopword);
				reflecST.put(i, oneDoc);
				i+=1;
			}
		}
	}
	
	
	private static void loadTrainsetMuti(String path, HashMap<Integer, 
			ArrayList<String>> Type, 
			ArrayList<String> wordList
			) throws IOException{
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
					Type.put(kk, oneDoc);
					kk +=1;
                }
    			br.close();
            }
        }
	}
	
	public static void ReadXMLMT(NodeList nodeList) {
    	for (int itr = 0; itr < nodeList.getLength(); itr++)   
    	{  
	    	Node node = nodeList.item(itr);
	    	if (node.getNodeType() == Node.ELEMENT_NODE)   
	    	{  
	    		Element eElement = (Element) node; 
	    		if(eElement.getElementsByTagName("Abstract").item(0).getTextContent().length() > 1) {
	    			
	    			if(eElement.getElementsByTagName("Class").item(0).getTextContent().equalsIgnoreCase("symptom")) {
	    				sympDoc.add(eElement.getElementsByTagName("Abstract").item(0).getTextContent());
	    			}else if(eElement.getElementsByTagName("Class").item(0).getTextContent().equalsIgnoreCase("reflection")) {
	    				reflecDoc.add(eElement.getElementsByTagName("Abstract").item(0).getTextContent());
	    			}else if(eElement.getElementsByTagName("Class").item(0).getTextContent().equalsIgnoreCase("diagnosis")) {
	    				diagDoc.add(eElement.getElementsByTagName("Abstract").item(0).getTextContent());
	    			}
	    			nameDoc.add(eElement.getElementsByTagName("Doc_ID").item(0).getTextContent());
	    			UsageModelPanel.showDoc("https://pubmed.ncbi.nlm.nih.gov/"+eElement.getElementsByTagName("Doc_ID").item(0).getTextContent());
	    		}
	    	}
    	}
	}
}
