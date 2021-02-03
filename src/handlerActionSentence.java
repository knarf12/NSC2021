import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class handlerActionSentence implements ActionListener{
	static String docchoice = "";
	static String algorchoice = "";
	
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equalsIgnoreCase("Single Document")) {
			docchoice = "singleDoc";
			UsageModelPanel.clearSentence();
			UsageModelPanel.clearDoc();
			UsageModelPanel.setSearch();
			
		}else if(e.getActionCommand().equalsIgnoreCase("Multi Documents")) {
			docchoice = "mutiDoc";
			System.out.println(docchoice);
			UsageModelPanel.clearSentence();
			UsageModelPanel.clearDoc();
			UsageModelPanel.setSearch();
		}
		
		// TODO Auto-generated method stub
		if(e.getActionCommand().equalsIgnoreCase("BM25+")) {
			algorchoice = "bm25";
			UsageModelPanel.clearSentence();
			if(docchoice.equalsIgnoreCase("singleDoc") && UsageModelPanel.CheckprocessSG ) {
				//System.out.println("BM25+");
				
				ClassifierSentence.getSTBM25();
			}else if (docchoice.equalsIgnoreCase("mutiDoc") && UsageModelPanel.CheckprocessML) {
				ClassifierMulti.getSTBM25();
			}
		}else if (e.getActionCommand().equalsIgnoreCase("Cosine Similarity")) {
			algorchoice = "cosine";
			UsageModelPanel.clearSentence();
			if(docchoice.equalsIgnoreCase("singleDoc") && UsageModelPanel.CheckprocessSG ) {
				ClassifierSentence.getSTCosine();
			}else if (docchoice.equalsIgnoreCase("mutiDoc") && UsageModelPanel.CheckprocessML) {
				ClassifierMulti.getSTCosine();
			}
			
		}else if(e.getActionCommand().equalsIgnoreCase("KNN")) {
			algorchoice = "knn";
			UsageModelPanel.clearSentence();
			if(docchoice.equalsIgnoreCase("singleDoc") && UsageModelPanel.CheckprocessSG ) {
				ClassifierSentence.getSTKNN();
			}else if (docchoice.equalsIgnoreCase("mutiDoc") && UsageModelPanel.CheckprocessML) {
				ClassifierMulti.getSTKNN();
			}
			
		}else if(e.getActionCommand().equalsIgnoreCase("Naive Bayes")) {
			algorchoice = "nv";
			UsageModelPanel.clearSentence();
			if(docchoice.equalsIgnoreCase("singleDoc") && UsageModelPanel.CheckprocessSG ) {
				ClassifierSentence.getSTNVB();
			}else if (docchoice.equalsIgnoreCase("mutiDoc") && UsageModelPanel.CheckprocessML) {
				ClassifierMulti.getSTNVB();
			}
			
		}
	}
}
