import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
		stopword = Files.readAllLines(Paths.get("./dic/stopwordAndSpc_eng.txt"));
		loadSingleFile("./data/dataSS/1.xml");
		loadTrainset("./data/data-Diag");
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
		//System.out.println("Root element: " + doc.getDocumentElement().getNodeName());  
		NodeList nodeList = doc.getElementsByTagName("Doc");  
		data.addAll(ReadXML(nodeList));
		System.out.println(data.get(0));
		String Doc = data.get(0);
		//String[] dspl = Doc.split(".");
		int i = 0;
		//System.out.println(data);
		for(String sentence : Doc.split("[:\\.]")) {
			data.clear();
//			System.out.println(sentence);
			for (String word : sentence.split("[,.() ; % : / \t -]")) {
				word = word.toLowerCase();
				if(word.length()>1 && ! mathMethod.isNumeric(word)) {
					data.add(word);
					if(!wordList.contains(word)) {
						wordList.add(word);
					}
				}
			}
			data.removeAll(stopword);
			singleDoc.put(i, data);
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
                }
    			br.close();
            }
            kk +=1;
        }
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
