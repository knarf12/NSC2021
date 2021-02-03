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
				if (ClassifierSentence.type.equalsIgnoreCase("diagnosis")) {
					UsageModelPanel.showSentence("SYMPTOM : -\n");
					UsageModelPanel.showSentence("TREATMENT MODALITY : ");
					ClassifierSentence.getSTBM25();
					UsageModelPanel.showSentence("\nREFLECTION/INFLECTION : -");
				}else if (ClassifierSentence.type.equalsIgnoreCase("reflection")) {
					UsageModelPanel.showSentence("SYMPTOM : - \n");
					UsageModelPanel.showSentence("\nTREATMENT MODALITY : -\n");
					UsageModelPanel.showSentence("REFLECTION/INFLECTION : ");
					ClassifierSentence.getSTBM25();
				}else if (ClassifierSentence.type.equalsIgnoreCase("symptom")) {
					UsageModelPanel.showSentence("SYMPTOM : - ");
					ClassifierSentence.getSTBM25();
					UsageModelPanel.showSentence("\nTREATMENT MODALITY : -\n");
					UsageModelPanel.showSentence("\nREFLECTION/INFLECTION : -");
				}
				
			}else if (docchoice.equalsIgnoreCase("mutiDoc") && UsageModelPanel.CheckprocessML) {
				ClassifierMulti.getSTBM25();
			}
		}else if (e.getActionCommand().equalsIgnoreCase("Cosine Similarity")) {
			algorchoice = "cosine";
			UsageModelPanel.clearSentence();
			if(docchoice.equalsIgnoreCase("singleDoc") && UsageModelPanel.CheckprocessSG ) {
				
				if (ClassifierSentence.type.equalsIgnoreCase("diagnosis")) {
					UsageModelPanel.showSentence("SYMPTOM : -\n");
					UsageModelPanel.showSentence("TREATMENT MODALITY : ");
					ClassifierSentence.getSTCosine();
					UsageModelPanel.showSentence("\nREFLECTION/INFLECTION : -");
				}else if (ClassifierSentence.type.equalsIgnoreCase("reflection")) {
					UsageModelPanel.showSentence("SYMPTOM : - \n");
					UsageModelPanel.showSentence("\nTREATMENT MODALITY : -\n");
					UsageModelPanel.showSentence("REFLECTION/INFLECTION : ");
					ClassifierSentence.getSTCosine();
				}else if (ClassifierSentence.type.equalsIgnoreCase("symptom")) {
					UsageModelPanel.showSentence("SYMPTOM : - ");
					ClassifierSentence.getSTCosine();
					UsageModelPanel.showSentence("\nTREATMENT MODALITY : -\n");
					UsageModelPanel.showSentence("\nREFLECTION/INFLECTION : - \n");
				}
				
			}else if (docchoice.equalsIgnoreCase("mutiDoc") && UsageModelPanel.CheckprocessML) {
				ClassifierMulti.getSTCosine();
			}
			
		}else if(e.getActionCommand().equalsIgnoreCase("KNN")) {
			algorchoice = "knn";
			UsageModelPanel.clearSentence();
			if(docchoice.equalsIgnoreCase("singleDoc") && UsageModelPanel.CheckprocessSG ) {
				if (ClassifierSentence.type.equalsIgnoreCase("diagnosis")) {
					UsageModelPanel.showSentence("SYMPTOM : -\n");
					UsageModelPanel.showSentence("TREATMENT MODALITY : ");
					ClassifierSentence.getSTKNN();
					UsageModelPanel.showSentence("\nREFLECTION/INFLECTION : -");
				}else if (ClassifierSentence.type.equalsIgnoreCase("reflection")) {
					UsageModelPanel.showSentence("SYMPTOM : - \n");
					UsageModelPanel.showSentence("\nTREATMENT MODALITY : -\n");
					UsageModelPanel.showSentence("REFLECTION/INFLECTION : ");
					ClassifierSentence.getSTKNN();
				}else if (ClassifierSentence.type.equalsIgnoreCase("symptom")) {
					UsageModelPanel.showSentence("SYMPTOM : - ");
					ClassifierSentence.getSTKNN();
					UsageModelPanel.showSentence("\nTREATMENT MODALITY : -\n");
					UsageModelPanel.showSentence("\nREFLECTION/INFLECTION : -");
				}
				
			}else if (docchoice.equalsIgnoreCase("mutiDoc") && UsageModelPanel.CheckprocessML) {
				ClassifierMulti.getSTKNN();
			}
			
		}else if(e.getActionCommand().equalsIgnoreCase("Naive Bayes")) {
			algorchoice = "nv";
			UsageModelPanel.clearSentence();
			if(docchoice.equalsIgnoreCase("singleDoc") && UsageModelPanel.CheckprocessSG ) {
				if (ClassifierSentence.type.equalsIgnoreCase("diagnosis")) {
					UsageModelPanel.showSentence("SYMPTOM : -\n");
					UsageModelPanel.showSentence("TREATMENT MODALITY : ");
					ClassifierSentence.getSTNVB();
					UsageModelPanel.showSentence("\nREFLECTION/INFLECTION : -");
				}else if (ClassifierSentence.type.equalsIgnoreCase("reflection")) {
					UsageModelPanel.showSentence("SYMPTOM : - \n");
					UsageModelPanel.showSentence("TREATMENT MODALITY : -\n");
					UsageModelPanel.showSentence("REFLECTION/INFLECTION : ");
					ClassifierSentence.getSTNVB();
				}else if (ClassifierSentence.type.equalsIgnoreCase("symptom")) {
					UsageModelPanel.showSentence("SYMPTOM : - ");
					ClassifierSentence.getSTNVB();
					UsageModelPanel.showSentence("TREATMENT MODALITY : -\n");
					UsageModelPanel.showSentence("\nREFLECTION/INFLECTION : -");
				}
				
			}else if (docchoice.equalsIgnoreCase("mutiDoc") && UsageModelPanel.CheckprocessML) {
				System.out.println("NVB");
				ClassifierMulti.getSTNVB();
			}
			
		}
	}
}
