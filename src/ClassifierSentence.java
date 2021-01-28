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
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;  
import org.w3c.dom.Element; 

public class ClassifierSentence {
	private static List<String> stopword;
	private static HashMap<Integer, ArrayList<String>> singleDoc = new HashMap<Integer, ArrayList<String>>();
	private static ArrayList<String> wordList = new ArrayList<String>();
	private static HashMap<Integer, ArrayList<String>> sigleType = new HashMap<Integer, ArrayList<String>>();
	private static HashMap<Integer, ArrayList<Integer>> VtDoclist = new HashMap<Integer, ArrayList<Integer>>();
	private static HashMap<Integer, ArrayList<Integer>> VtDocTr = new HashMap<Integer, ArrayList<Integer>>();
	private static HashMap<Integer, ArrayList<Double>> evaluaCosine = new HashMap<Integer, ArrayList<Double>>();
	private static HashMap<Integer, ArrayList<Double>> evaluaBM25 = new HashMap<Integer, ArrayList<Double>>();
	
	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
		stopword = Files.readAllLines(Paths.get("./dic/stopwordAndSpc_eng.txt"));
		loadSingleFile("./data/dataSS/3.xml");
		loadTrainset("./data/data-Diag");
		
		//vector
		for (int i = 0; i < singleDoc.size(); i++) {
			VtDoclist.put(i,mathMethod.getVector(singleDoc.get(i), wordList));
			//System.out.println(singleDoc.get(i));
			//System.out.println(mathMethod.getVector(singleDoc.get(i), wordList));
		}
		
		for (int i = 0; i < sigleType.size(); i++) {
			//if (i== 26 || i==21) System.out.println(mathMethod.getVector(sigleType.get(i), wordList));
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
		
		//bm25+
		bm25F.DocAVGST(VtDoclist);
		for (int j = 0; j < VtDoclist.size(); j++) {
			ArrayList<Double> similar = new ArrayList<Double>();
			for (int j2 = 0; j2 < VtDocTr.size(); j2++) {
				similar.add(bm25F.BM25PlusST(VtDocTr.get(j2), VtDoclist.get(j), bm25F.getAllST(VtDoclist)));
//					System.out.println(j2);
			}
			evaluaBM25.put(j, similar);
		}
		//System.out.println(bm25F.getAllST(VtDoclist));
//		System.out.println(evaluaCosine);
//		System.out.println(evaluaBM25);
		DecimalFormat df2 = new DecimalFormat("#.##");
		System.out.println(evaluaBM25.size());
		for (int i = 0; i < evaluaBM25.size(); i++) {
			Double sum = 0.0d;
			System.out.println("////////////////////////////////////////////////");
			for (int k = 0; k < evaluaBM25.get(0).size(); k++) {
				
				sum += sum + evaluaBM25.get(i).get(k);
				if(evaluaBM25.get(i).get(k)>15.0d) {
					System.out.println(k+" "+df2.format(+evaluaCosine.get(i).get(k))+" \t: "+df2.format(evaluaBM25.get(i).get(k)));
				}
			}
//			System.out.println(sum);
//			System.out.println(sum/evaluaBM25.get(0).size());
		}
		
	}
	public ClassifierSentence() {
		
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
		
		int i = 0;
		for(String sentence : Doc.split("[:\\.]")) {
			//System.out.println(sentence);
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
			data.removeAll(stopword);
			singleDoc.put(i, oneDoc);
			i+=1;
		}
		//System.out.println(singleDoc);
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
                //System.out.println(i);
                //System.out.println(line);
                for(String sentence : line.split("[:\\.]")) {
                	ArrayList<String> oneDoc = new ArrayList<String>();
    				//ArrayList<String> oneLine = new ArrayList<String>();
                	//System.out.println(sentence);
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
					if(kk == 21 || kk== 26)System.out.println(oneDoc);
					//System.out.println(kk);
					sigleType.put(kk, oneDoc);
					kk +=1;
                }
    			br.close();
            }
        }
        
//       System.out.println(sigleType);
	}
	
	
	void loadMutiFile() {
		// TODO Auto-generated method stub
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
	    		}
	    	}
    	}
		return Data;
	}
}
