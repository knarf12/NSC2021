import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class handlerAction implements ActionListener{
	public String choice = "" ; 
	private String weight= "";
	private String algorithm= "";
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand().equalsIgnoreCase("Pubmed")) {
			choice="Pubmed";
			//System.out.println("Web");
		}else if(e.getActionCommand().equalsIgnoreCase("Local Device")) {
			choice = "local";
			//System.out.println("Local");
		}
		
		//Doc selection
		if(e.getActionCommand().equalsIgnoreCase("All")) {
			//System.out.println("All");
			TrainModelPanel.clearTextArea();
			if(algorithm.equalsIgnoreCase("BM25+")) {
				getDocAll(midProcess.evaluaBM25,"BM25+");
			}else if (algorithm.equalsIgnoreCase("Cosine")) {
				//System.out.println("Cosine");
				getDocAll(midProcess.evaluaCosine,"Cosine");
			}else if (algorithm.equalsIgnoreCase("KNN")) {
				//getDocSymptom();
				getDocAll(midProcess.evaluaBM25,"KNN");
			}else if (algorithm.equalsIgnoreCase("NV")) {
				//getDocSymptom();
			}else if (algorithm.equalsIgnoreCase("Deep")) {
				//getDocSymptom();
			}
			
		}else if(e.getActionCommand().equalsIgnoreCase("Symptoms")) {
			//System.out.println("Symptoms");
			TrainModelPanel.clearTextArea();
			
			if(algorithm.equalsIgnoreCase("BM25+")) {
				getDocSymptom(midProcess.evaluaBM25, 0.8);
			}else if (algorithm.equalsIgnoreCase("Cosine")) {
				//System.out.println("Cosine");
				getDocSymptom(midProcess.evaluaCosine,0.1);
			}else if (algorithm.equalsIgnoreCase("KNN")) {
				//getDocSymptom();
				getKNN(midProcess.evaluaBM25,"Symptom");
			}else if (algorithm.equalsIgnoreCase("NV")) {
				//getDocSymptom();
			}else if (algorithm.equalsIgnoreCase("Deep")) {
				//getDocSymptom();
				
			}
			
		}else if(e.getActionCommand().equalsIgnoreCase("Diagnosis")) {
			//System.out.println("Diagnosis");
			TrainModelPanel.clearTextArea();
			
			if(algorithm.equalsIgnoreCase("BM25+")) {
				getDocDiagnosis(midProcess.evaluaBM25, 0.8);
			}else if (algorithm.equalsIgnoreCase("Cosine")) {
				//System.out.println("Cosine");
				getDocDiagnosis(midProcess.evaluaCosine, 0.1);
			}else if (algorithm.equalsIgnoreCase("KNN")) {
				//getDocDiagnosis();
				getKNN(midProcess.evaluaBM25,"Reflec");
			}else if (algorithm.equalsIgnoreCase("NV")) {
				
				//getDocDiagnosis();
			}else if (algorithm.equalsIgnoreCase("Deep")) {
				
				//getDocDiagnosis();
			}
			
		}else if(e.getActionCommand().equalsIgnoreCase("Reflection")) {
			TrainModelPanel.clearTextArea();
			if(algorithm.equalsIgnoreCase("BM25+")) {
				getDocReflection(midProcess.evaluaBM25, 0.8);
				
				
			}else if (algorithm.equalsIgnoreCase("Cosine")) {
//				System.out.println("Cosine");
				getDocReflection(midProcess.evaluaCosine, 0.1);
				
				
			}else if (algorithm.equalsIgnoreCase("KNN")) {
				getKNN(midProcess.evaluaBM25,"Diag");
				//getDocReflection();
				
				
			}else if (algorithm.equalsIgnoreCase("NV")) {
				//getDocReflection();
				
				
			}else if (algorithm.equalsIgnoreCase("Deep")) {
				//getDocReflection();
				
				
			}
		}
		
		//technique select
		if(e.getActionCommand().equalsIgnoreCase("Deep Learning")) {
			algorithm = "Deep";
			TrainModelPanel.bgWeight2.clearSelection();
			
			
		}else if(e.getActionCommand().equalsIgnoreCase("Unsupervised Learning")) {
			weight = "UTW";
			TrainModelPanel.setFalse("UTW");
			
			
		}else if(e.getActionCommand().equalsIgnoreCase("Supervised Learning")) {
			weight = "STW";
			TrainModelPanel.setFalse("STW");
			
			
		}
		
		//algorithm select
		if(e.getActionCommand().equalsIgnoreCase("BM25+ Similarity") && weight.equalsIgnoreCase("UTW")) {
			algorithm = "BM25+";
			
		}else if (e.getActionCommand().equalsIgnoreCase("Cosine Similarity")&& weight.equalsIgnoreCase("UTW")) {
			algorithm = "Cosine";
			
		}else if(e.getActionCommand().equalsIgnoreCase("KNN")&& weight.equalsIgnoreCase("STW")) {
			algorithm = "KNN";
			
		}else if(e.getActionCommand().equalsIgnoreCase("Naive Bayes")&& weight.equalsIgnoreCase("STW")) {
			algorithm = "NV" ;
			
		}
	}
	
	
	private void getDocReflection(HashMap<Integer, ArrayList<Double>> evalua, Double rate) {
		// TODO Auto-generated method stub
		int s=0;
		for (int i = 1; i <= evalua.size(); i++) {
			int k = 0;
			for (int j = 4; j < evalua.get(i).size(); j++) {
				if (evalua.get(i).get(j) >= rate && j<13) {
					k++;
				}
			}
			if(k>=5) {
				s+=1;
				TrainModelPanel.addTextAreaWC2(midProcess.nameDoc.get(i-1));
			}
		}
		TrainModelPanel.setText(""+s);

	}

	private void getDocDiagnosis(HashMap<Integer, ArrayList<Double>> evalua, Double rate) {
		// TODO Auto-generated method stub
		int s=0;
		for (int i = 1; i <= evalua.size(); i++) {
			int k = 0;
			for (int j = 0; j < evalua.get(1).size(); j++) {
				//System.out.println(j+" "+ midProcess.evaluaBM25.get(i).get(j));
				if (j<4 && evalua.get(i).get(j) >= rate) {
					k++;
				}
			}
			if(k>2) {
				s+=1;
				TrainModelPanel.addTextAreaWC2(midProcess.nameDoc.get(i-1));
			}
		}
		TrainModelPanel.setText(""+s);

	}
	
	private void getDocSymptom(HashMap<Integer, ArrayList<Double>> evalua, Double rate) {
		// TODO Auto-generated method stub
		int s=0;
		for (int i = 1; i <= evalua.size(); i++) {
			int k = 0;
			for (int j = 13; j < evalua.get(i).size(); j++) {
				
				if (evalua.get(i).get(j) >= rate ) {
					k++;
				}
			}
			if(k>2) {
				s+=1;
				TrainModelPanel.addTextAreaWC2(midProcess.nameDoc.get(i-1));
			}
		}
		TrainModelPanel.setText(""+s);
	}
	
	private void getDocAll(HashMap<Integer, ArrayList<Double>> evalua,String name) {
		Double r1 =0.0d ;
		Double r2 =0.0d;
		Double r3 =0.0d;
		if(algorithm.equalsIgnoreCase("BM25+")) {
			r1 = 0.8; r2 = 0.8; r3=0.8;
		}else if (algorithm.equalsIgnoreCase("Cosine")) {
			r1=0.1; r2=0.1; r3=0.1;
		}else if (algorithm.equalsIgnoreCase("KNN")) {
			r1 = 0.65; r2 = 0.65; r3=0.8;
		}else if (algorithm.equalsIgnoreCase("NV")) {
			
		}else if (algorithm.equalsIgnoreCase("Deep")) {
			
		}
		
		if(r1>0) {
			int s=0;
			for (int i = 1; i <= evalua.size(); i++) {
				int k = 0;
				int k2 =0;
				int k3 =0;
				for (int j = 0; j < evalua.get(i).size(); j++) {
					
					if (evalua.get(i).get(j) >= r1 && j<4) {
						k++;
					}else if (evalua.get(i).get(j) >= r2 && j<13) {
						k2++;
					}else if(evalua.get(i).get(j) >= r3 ){
						k3++;
					}
				}
				if(k>2 || k2>=5 || k3 >2) {
					s+=1;
					TrainModelPanel.addTextAreaWC2(midProcess.nameDoc.get(i-1));
				}
			}
			TrainModelPanel.setText(""+s);
		}
	}
	
	private void getKNN(HashMap<Integer, ArrayList<Double>> evalua, String name) {
		Double rate = 0.8;
		int s=0;
		for (int i = 1; i <= evalua.size(); i++) {
			
		}
		TrainModelPanel.setText(""+s);
	}
}
