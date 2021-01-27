import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.w3c.dom.Node;

public class WebClawer {
	static org.w3c.dom.Document dom;
	static boolean done = false;

	
	public WebClawer(String term, int RetMax) throws IOException, InterruptedException, ParserConfigurationException, TransformerException {
		HashMap<String, String> list_result1 = new HashMap<String, String>();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		dBuilder = dbFactory.newDocumentBuilder();
		dom = dBuilder.newDocument();
		org.w3c.dom.Element rootElement = dom.createElement("Data");
		dom.appendChild(rootElement);
		
	 	//String RetMax = "50"; //input count doc
//	        String term1 = "HCoV-19"; //input keyword 
//			key
//	        HCoV-19
//	        hCoV-19
//	        covid-19
//	        coronavirus
//	        SARS-CoV-2
//	        Wuhan coronavirus
//	        Chinese virus
        
        if (term.contains(" ")) {
        	term = term.replace(" ", "%20");
		}
			
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed&term="+term+"&usehistory=y&RetMax="+RetMax+"&fbclid=IwAR2SeQ9cnYiK0ObeQc7jZCaQSadTiO7NE_KcHi9EvOyfFuYYuUm9IvukoPg"))
				.build();
		
		HttpResponse<String> response = client.send(request,
		        HttpResponse.BodyHandlers.ofString());
		
		
		String resultHtml = response.body().toString();
		
		Document doc = Jsoup.parse(resultHtml);
		
		Elements link = doc.getElementsByTag("IdList");
		  
		    StringTokenizer search_id = new StringTokenizer(link.text());
		    //System.out.println(search_id.countTokens());
		    
	    while (search_id.hasMoreTokens()) { 
	        list_result1 = Pull_with_id(search_id.nextToken());
	        if (!list_result1.get("Abstract").isEmpty()) {
	        	rootElement.appendChild(getReviewsPositive(dom,list_result1));
	        }
		} 
//		for output to file, console
	    TransformerFactory transformerFactory = TransformerFactory.newInstance();
	    Transformer transformer = transformerFactory.newTransformer();
	    //for pretty print
	    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	    DOMSource source = new DOMSource(dom);
	
	    //write to console or file
	    //StreamResult console = new StreamResult(System.out);
	    StreamResult file1 = new StreamResult(new File("./data/dlData/dataDL.xml"));
	
	    //write data
	    //transformer.transform(source, console);
	    transformer.transform(source, file1);
	    done = true;
	    System.out.println("DONE");
		
	}
	
	public static HashMap<String,String> Pull_with_id(String id_data) throws IOException, InterruptedException, TransformerException {
		
		HashMap<String, String> list_result = new HashMap<String, String>();
			
			HttpClient client1 = HttpClient.newHttpClient();
			String linkpa = "https://pubmed.ncbi.nlm.nih.gov/"+id_data+"/";
			HttpRequest request1 = HttpRequest.newBuilder()
	                .uri(URI.create(linkpa))
	                .build();
//			 
			 HttpResponse<String> response1 = client1.send(request1,
		     HttpResponse.BodyHandlers.ofString());
		        

			String resultHtml1 = response1.body().toString();
			
			Document doc1 = Jsoup.parse(resultHtml1);
			
			Elements title = doc1.getElementsByClass("heading-title");
			Elements abstract1 = doc1.getElementsByClass("abstract-content selected");
			
			//	        	 list_result.put("Link", linkpa);
			 list_result.put("ID", id_data);
			 list_result.put("Title", title.text());
			 list_result.put("Abstract", abstract1.text());
			 
			 return list_result;

	}
	private static Node getReviewsPositive(org.w3c.dom.Document dom,HashMap<String, String> list_result1 ) {
		org.w3c.dom.Element reviews = dom.createElement("Doc");
		
	    reviews.appendChild(getEmployeeElements(dom, reviews, "Doc_ID", list_result1.get("ID")));
	    reviews.appendChild(getEmployeeElements(dom, reviews, "Title", list_result1.get("Title")));
	    reviews.appendChild(getEmployeeElements(dom, reviews, "Abstract", list_result1.get("Abstract")));

	    return reviews;
	}

	private static Node getEmployeeElements(org.w3c.dom.Document doc, org.w3c.dom.Element element, String name, String value) {
		org.w3c.dom.Element node = doc.createElement(name);
	    node.appendChild(doc.createTextNode(value));
	    return node;
	}

}
