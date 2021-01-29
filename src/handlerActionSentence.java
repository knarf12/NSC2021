import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class handlerActionSentence implements ActionListener{
	static String docchoice = "";
	static String algorchoice = "";
	
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equalsIgnoreCase("Single Document")) {
			docchoice = "singleDoc";
			UsageModelPanel.setSearch();
			
		}else if(e.getActionCommand().equalsIgnoreCase("Multi Document")) {
			docchoice = "mutiDoc";
			UsageModelPanel.setSearch();
		}
		
		// TODO Auto-generated method stub
		if(e.getActionCommand().equalsIgnoreCase("BM25+ Similarity")) {
			algorchoice = "bm25";
		}else if (e.getActionCommand().equalsIgnoreCase("Cosine Similarity")) {
			algorchoice = "cosine";
		}else if(e.getActionCommand().equalsIgnoreCase("KNN")) {
			algorchoice = "knn";
		}else if(e.getActionCommand().equalsIgnoreCase("Naive Bayes")) {
			algorchoice = "nv";
		}else if(e.getActionCommand().equalsIgnoreCase("Deep Learning")) {
			algorchoice = "deep";
		}
	}
}
