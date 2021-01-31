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
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class midProcess {
	private static ArrayList<String> wordList = new ArrayList<String>();
	private static List<String> stopword;
	public static ArrayList<String> nameDoc = new ArrayList<String>();
	//public static ArrayList<String> abtDoc = new ArrayList<String>();
	private static ArrayList<String> DocsTest = new ArrayList<String>();
	private static HashMap<Integer, ArrayList<String>> DocTr = new HashMap<Integer, ArrayList<String>>();
	private static HashMap<Integer, ArrayList<String>> Doclist = new HashMap<Integer, ArrayList<String>>();
	private static HashMap<Integer, ArrayList<Integer>> VtDoclist = new HashMap<Integer, ArrayList<Integer>>();
	private static HashMap<Integer, ArrayList<Integer>> VtDocTr = new HashMap<Integer, ArrayList<Integer>>();
	static HashMap<Integer, ArrayList<Double>> evaluaCosine = new HashMap<Integer, ArrayList<Double>>();
	private static HashMap<Integer, ArrayList<Double>> evaluaSSCosine = new HashMap<Integer, ArrayList<Double>>();
	static HashMap<Integer, ArrayList<Double>> evaluaBM25 = new HashMap<Integer, ArrayList<Double>>();
	static ArrayList<String> evaluaKNN = new ArrayList<String>();
	static ArrayList<String> evaluaNVB = new ArrayList<String>();
	//private static HashMap<Integer, ArrayList<Double>> evaluaSSBM25 = new HashMap<Integer, ArrayList<Double>>();
	private static DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
	private static File folder;
	private static File[] listOfFiles;
	private static mathMethod mathMethod = new mathMethod();
	private static bm25F bm25f = new bm25F();
	private static knnAlgorithm knn = new knnAlgorithm();
	private static Model md = new Model();
	static int kk=0;
	
	
	@SuppressWarnings("static-access")
	public midProcess(String path) throws ParserConfigurationException, SAXException, IOException {
		//knn.loadTrainmodel();
		stopword = Files.readAllLines(Paths.get("./dic/stopwordAndSpc_eng.txt"));
		loadData(path); // true data load
		
		
		//train
		loadTrainset("./data/data-Reflec"); // data for train in program
		loadTrainset("./data/data-Diag"); // data for train in program
		loadTrainset("./data/data-Sympt"); // data for train in program
//		loadTrainset(); 
		
		splitWord();
		DataProcess();
		
	}
	
	@SuppressWarnings("unused")
	//data Test set
	private static void loadData(String path) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilder db = dbf.newDocumentBuilder(); 
		folder = new File(path);
    	listOfFiles = folder.listFiles();
        for(int i = 0; i < listOfFiles.length; i++){
            String filename = listOfFiles[i].getName();
            if(filename.endsWith(".xml")||filename.endsWith(".XML")) {
//                System.out.println(filename);
                Document doc = db.parse(path+"/"+filename);  
            	doc.getDocumentElement().normalize(); 
            	NodeList nodeList = doc.getElementsByTagName("Doc");
            	DocsTest.addAll(ReadXML(nodeList));
            }
        }
	}
	
	@SuppressWarnings("static-access")
	private static void loadTrainset(String path) throws IOException{
		folder = new File(path);
        listOfFiles = folder.listFiles();
        BufferedReader br ;
        for(int i = 0; i < listOfFiles.length; i++){
            String filename = listOfFiles[i].getName();
            
            if(filename.endsWith(".txt")||filename.endsWith(".TXT")) {
            	
                br = new BufferedReader(new FileReader(path+"/"+filename));
                String line = br.readLine();
                //System.out.println(i);
                //System.out.println(line);
                ArrayList<String> oneDoc = new ArrayList<String>();
    				//ArrayList<String> oneLine = new ArrayList<String>();
    			for (String word : line.split("[,.() ; % : / \t -]")) {
    					word = word.toLowerCase();
    					if(word.length()>1 && ! mathMethod.isNumeric(word)) {
    						oneDoc.add(word);
    						if(!wordList.contains(word)) {
    							wordList.add(word);
    						}
    					}
    			}
    			oneDoc.removeAll(stopword);
    			DocTr.put(kk, oneDoc);
    			br.close();
            }
            kk +=1;
        }
	}
	
	@SuppressWarnings("static-access")
	public static void DataProcess() throws SAXException, IOException, ParserConfigurationException {
		//System.out.println(nameDoc);
		for (int j = 1; j <= Doclist.size(); j++) {
			VtDoclist.put(j, mathMethod.getVector(Doclist.get(j), wordList));
		}
		for (int j = 0; j < DocTr.size(); j++) {
			VtDocTr.put(j, mathMethod.getVector(DocTr.get(j), wordList));
			//System.out.println(getVector(DocTr.get(j)));
		}
		
		for (int j = 1; j <= VtDoclist.size(); j++) {
			ArrayList<Double> similar = new ArrayList<Double>();
			int s=0 ,r=0, d =0;
			for (int j2 = 0; j2 < VtDocTr.size(); j2++) {
				similar.add(mathMethod.CosineSim(VtDocTr.get(j2), VtDoclist.get(j)));
//					System.out.println(j2);
				if(j2<10 && mathMethod.CosineSim(VtDocTr.get(j2), VtDoclist.get(j)) < 0.1) {
					d++;
				}else if(j2<20 && mathMethod.CosineSim(VtDocTr.get(j2), VtDoclist.get(j)) < 0.1) {
					r++;
				}else if(j2<30 && mathMethod.CosineSim(VtDocTr.get(j2), VtDoclist.get(j)) < 0.1) {
					s++;
				}
			}
			evaluaCosine.put(j, similar);
			if(d>=5) {
				writeXML("./ClassifierDocFile/Cosine/",nameDoc.get(j-1),DocsTest.get(j-1),"diagnosis");
			}else if(r>=5) {
				writeXML("./ClassifierDocFile/Cosine/",nameDoc.get(j-1),DocsTest.get(j-1),"reflection");
			}else if(s>=5){
				writeXML("./ClassifierDocFile/Cosine/",nameDoc.get(j-1),DocsTest.get(j-1),"symptom");
			}
		}
		
//		for (int j = 0; j < VtDocTr.size(); j++) {
//			ArrayList<Double> similar = new ArrayList<Double>();
//			for (int j2 = 0; j2 < VtDocTr.size(); j2++) {
//				similar.add(mathMethod.CosineSim(VtDocTr.get(j2), VtDocTr.get(j)));
////					System.out.println(j2);
//			}
//			evaluaSSCosine.put(j, similar);
//		}
		
		//bm25+
		ArrayList<Double> weigtingIDF = new ArrayList<Double>();
		weigtingIDF.addAll(bm25F.getAll(VtDoclist));
		bm25F.DocAVG(VtDoclist);
		//System.out.println(weigtingIDF);
		for (int j = 1; j <= VtDoclist.size(); j++) {
			ArrayList<Double> similar = new ArrayList<Double>();
			for (int j2 = 0; j2 < VtDocTr.size(); j2++) {
				similar.add(bm25F.BM25Plus(VtDocTr.get(j2), VtDoclist.get(j), weigtingIDF));
				//System.out.println(j2);
			}
			evaluaBM25.put(j, similar);
			
		}

		//KNN && NVB
		for (int i = 1; i <= VtDoclist.size(); i++) {
			evaluaKNN.add(knnAlgorithm.checkKNNDoc(VtDoclist.get(i), VtDocTr));
			writeXML("./ClassifierDocFile/KNN/",nameDoc.get(i-1),DocsTest.get(i-1),knnAlgorithm.checkKNNDoc(VtDoclist.get(i), VtDocTr));
			evaluaNVB.add(NaiveBayes.NVprocess(VtDoclist.get(i), VtDocTr));
			writeXML("./ClassifierDocFile/NVB/",nameDoc.get(i-1),DocsTest.get(i-1),NaiveBayes.NVprocess(VtDoclist.get(i), VtDocTr));
		}
	}
	
	private static void splitWord() {
		// TODO Auto-generated method stub
		int i = 1;
		for(String doc : DocsTest) {
			ArrayList<String> wordAll = new ArrayList<String>();
			for (String word : doc.split("[,.() ; % : / \t -]")) {
				word = word.toLowerCase();
				if(word.length()>1 && !mathMethod.isNumeric(word)) {
					wordAll.add(word);
					if(!wordList.contains(word)) {
						wordList.add(word);
					}
				}
			}
			wordAll.removeAll(stopword);
			Doclist.put(i, wordAll);
			i++;
		}
		wordList.removeAll(stopword);
	}
	
	protected static void writeXML(String path, String ID, String abs,String cls) {
		// TODO Auto-generated method stub
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            //add elements to Document
            Element rootElement =doc.createElement("Data");
            //append root element to document
            doc.appendChild(rootElement);

            rootElement.appendChild(getReviewsPositive(doc, ID, abs,cls));
            //for output to file, console
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            //for pretty print
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);

            //write to console or file
            StreamResult console = new StreamResult(System.out);
            StreamResult file = new StreamResult(new File(path+""+ID+".xml"));

            //write data
            transformer.transform(source, console);
            transformer.transform(source, file);
            //System.out.println("DONE");

        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	
	private static Node getReviewsPositive(org.w3c.dom.Document dom,String ID, String abs,String cls) {
		org.w3c.dom.Element reviews = dom.createElement("Doc");
		
	    reviews.appendChild(getEmployeeElements(dom, reviews, "Doc_ID", ID));
	    reviews.appendChild(getEmployeeElements(dom, reviews, "Class", cls));
	    reviews.appendChild(getEmployeeElements(dom, reviews, "Abstract", abs));

	    return reviews;
	}

	private static Node getEmployeeElements(org.w3c.dom.Document doc, org.w3c.dom.Element element, String name, String value) {
		org.w3c.dom.Element node = doc.createElement(name);
	    node.appendChild(doc.createTextNode(value));
	    return node;
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
	    			nameDoc.add(eElement.getElementsByTagName("Doc_ID").item(0).getTextContent());
	    			TrainModelPanel.addTextAreaWC(eElement.getElementsByTagName("Doc_ID").item(0).getTextContent());
	    			Data.add(eElement.getElementsByTagName("Abstract").item(0).getTextContent());  
	    		}
	    	}
    	}
		return Data;
	}
}
