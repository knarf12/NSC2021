import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class handlerAction implements ActionListener{
	public String choice = "" ; 
	private String weight= "";
	public static String algorithm= "";
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
				getKNN("All");
			}else if (algorithm.equalsIgnoreCase("NV")) {
				getNVB("All");
			}else if (algorithm.equalsIgnoreCase("Deep")) {
				//getDocSymptom();
			}
			
		}else if(e.getActionCommand().equalsIgnoreCase("Symptoms")) {
			//System.out.println("Symptoms");
			TrainModelPanel.clearTextArea();
			
			if(algorithm.equalsIgnoreCase("BM25+")) {
				if (midProcess.evaluaBM25.size()<= 50) {
					getDocDiagnosis(midProcess.evaluaBM25, 85.0d);
				}else if (midProcess.evaluaBM25.size()<= 220) {
					getDocDiagnosis(midProcess.evaluaBM25, 140.0d);
				}else {
					getDocDiagnosis(midProcess.evaluaBM25, 160.0d);
				}
				
			}else if (algorithm.equalsIgnoreCase("Cosine")) {
				//System.out.println("Cosine");
				getDocSymptom(midProcess.evaluaCosine,0.1);
			}else if (algorithm.equalsIgnoreCase("KNN")) {
				//getDocSymptom();
				getKNN("Symptom");
			}else if (algorithm.equalsIgnoreCase("NV")) {
				//getDocSymptom();
				getNVB("Symptom");
			}else if (algorithm.equalsIgnoreCase("Deep")) {
				//getDocSymptom();
			}
			
		}else if(e.getActionCommand().equalsIgnoreCase("Diagnosis")) {
			//System.out.println("Diagnosis");
			TrainModelPanel.clearTextArea();
			
			if(algorithm.equalsIgnoreCase("BM25+")) {
				if (midProcess.evaluaBM25.size()<= 50) {
					getDocDiagnosis(midProcess.evaluaBM25, 85.0d);
				}else if (midProcess.evaluaBM25.size()<= 220) {
					getDocDiagnosis(midProcess.evaluaBM25, 140.0d);
				}else {
					getDocDiagnosis(midProcess.evaluaBM25, 160.0d);
				}
				
				
			}else if (algorithm.equalsIgnoreCase("Cosine")) {
				//System.out.println("Cosine");
				getDocDiagnosis(midProcess.evaluaCosine, 0.1);
			}else if (algorithm.equalsIgnoreCase("KNN")) {
				//getDocDiagnosis();
				getKNN("Reflec");
			}else if (algorithm.equalsIgnoreCase("NV")) {
				getNVB("Reflec");
				//getDocDiagnosis();
			}else if (algorithm.equalsIgnoreCase("Deep")) {
				//getDocDiagnosis();
			}
			
		}else if(e.getActionCommand().equalsIgnoreCase("Reflection")) {
			TrainModelPanel.clearTextArea();
			if(algorithm.equalsIgnoreCase("BM25+")) {
				if (midProcess.evaluaBM25.size()<= 50) {
					getDocDiagnosis(midProcess.evaluaBM25, 85.0d);
				}else if (midProcess.evaluaBM25.size()<= 220) {
					getDocDiagnosis(midProcess.evaluaBM25, 140.0d);
				}else {
					getDocDiagnosis(midProcess.evaluaBM25, 160.0d);
				}
				
			}else if (algorithm.equalsIgnoreCase("Cosine")) {
//				System.out.println("Cosine");
				getDocReflection(midProcess.evaluaCosine, 0.1);
			}else if (algorithm.equalsIgnoreCase("KNN")) {
				getKNN("Diag");
				//getDocReflection();
			}else if (algorithm.equalsIgnoreCase("NV")) {
				//getDocReflection();
				getNVB("Diag");
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
			for (int j = 10; j < evalua.get(i).size(); j++) {
				if (evalua.get(i).get(j) >= rate && j<20) {
					k++;
				}
			}
			if(k>=5) {
				s+=1;
				TrainModelPanel.addTextAreaWC2(midProcess.nameDoc.get(i-1));
				if(rate <= 1 ) {
					midProcess.writeXML("./ClassifierDocFile/Cosine/",midProcess.nameDoc.get(i-1),midProcess.DocsTest.get(i-1),"reflection");
				}else{
					midProcess.writeXML("./ClassifierDocFile/BM25/",midProcess.nameDoc.get(i-1),midProcess.DocsTest.get(i-1),"reflection");
				}
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
				if (j<10 && evalua.get(i).get(j) >= rate) {
					k++;
				}
			}
			if(k>=5) {
				s+=1;
				TrainModelPanel.addTextAreaWC2(midProcess.nameDoc.get(i-1));
				if(rate <= 1 ) {
					midProcess.writeXML("./ClassifierDocFile/Cosine/",midProcess.nameDoc.get(i-1),midProcess.DocsTest.get(i-1),"diagnosis");
				}else{
					midProcess.writeXML("./ClassifierDocFile/BM25/",midProcess.nameDoc.get(i-1),midProcess.DocsTest.get(i-1),"diagnosis");
				}
			}
		}
		TrainModelPanel.setText(""+s);

	}
	
	private void getDocSymptom(HashMap<Integer, ArrayList<Double>> evalua, Double rate) {
		// TODO Auto-generated method stub
		int s=0;
		for (int i = 1; i <= evalua.size(); i++) {
			int k = 0;
			for (int j = 20; j < evalua.get(i).size(); j++) {
				
				if (evalua.get(i).get(j) >= rate ) {
					k++;
				}
			}
			if(k>=5) {
				s+=1;
				TrainModelPanel.addTextAreaWC2(midProcess.nameDoc.get(i-1));
				if(rate <= 1 ) {
					midProcess.writeXML("./ClassifierDocFile/Cosine/",midProcess.nameDoc.get(i-1),midProcess.DocsTest.get(i-1),"symptom");
				}else{
					midProcess.writeXML("./ClassifierDocFile/BM25/",midProcess.nameDoc.get(i-1),midProcess.DocsTest.get(i-1),"symptom");
				}
			}
		}
		TrainModelPanel.setText(""+s);
	}
	
	private void getDocAll(HashMap<Integer, ArrayList<Double>> evalua,String name) {
		Double r1 =0.0d ;
		if(algorithm.equalsIgnoreCase("BM25+")) {
			if (midProcess.evaluaBM25.size()<= 50) {
				r1 = 85.0d;
				
			}else if (midProcess.evaluaBM25.size()<= 220) {
				r1 = 140.0d;
			}else {
				r1 = 160.0d; 
			}
			
		}else if (algorithm.equalsIgnoreCase("Cosine")) {
			r1=0.1;
		}
		if(r1>0) {
			int s=0;
			for (int i = 1; i <= evalua.size(); i++) {
				int k = 0;
				int k2 =0;
				int k3 =0;
				for (int j = 0; j < evalua.get(i).size(); j++) {
					
					if (evalua.get(i).get(j) >= r1 && j<10) {
						k++;
					}else if (evalua.get(i).get(j) >= r1 && j<20) {
						k2++;
					}else if(evalua.get(i).get(j) >= r1 ){
						k3++;
					}
				}
				if(k>=5 || k2>=5 || k3 >=5) {
					s+=1;
					TrainModelPanel.addTextAreaWC2(midProcess.nameDoc.get(i-1));
				}
			}
			TrainModelPanel.setText(""+s);
		}
	}
	
	private void getKNN(String name) {
		int s=0;
		for (int i = 0; i < midProcess.evaluaKNN.size(); i++) {
			if(name.equalsIgnoreCase("Symptom") && midProcess.evaluaKNN.get(i).equalsIgnoreCase("symptom")) {
				s+=1;
				TrainModelPanel.addTextAreaWC2(midProcess.nameDoc.get(i));
			}else if(name.equalsIgnoreCase("Diag")&& midProcess.evaluaKNN.get(i).equalsIgnoreCase("diagnosis")) {
				s+=1;
				TrainModelPanel.addTextAreaWC2(midProcess.nameDoc.get(i));
			}else if(name.equalsIgnoreCase("Reflec")&& midProcess.evaluaKNN.get(i).equalsIgnoreCase("reflection")) {
				s+=1;
				TrainModelPanel.addTextAreaWC2(midProcess.nameDoc.get(i));
			}
			if(name.equalsIgnoreCase("All")) {
				s+=1;
				TrainModelPanel.addTextAreaWC2(midProcess.nameDoc.get(i));
			}
		}
		TrainModelPanel.setText(""+s);
	}
	
	private void getNVB(String name) {
		int s=0;
		for (int i = 0; i < midProcess.evaluaKNN.size(); i++) {
			if(name.equalsIgnoreCase("Symptom") && midProcess.evaluaNVB.get(i).equalsIgnoreCase("symptom")) {
				s+=1;
				TrainModelPanel.addTextAreaWC2(midProcess.nameDoc.get(i));
			}else if(name.equalsIgnoreCase("Diag")&& midProcess.evaluaNVB.get(i).equalsIgnoreCase("diagnosis")) {
				s+=1;
				TrainModelPanel.addTextAreaWC2(midProcess.nameDoc.get(i));
			}else if(name.equalsIgnoreCase("Reflec")&& midProcess.evaluaNVB.get(i).equalsIgnoreCase("reflection")) {
				s+=1;
				TrainModelPanel.addTextAreaWC2(midProcess.nameDoc.get(i));
			}
			if(name.equalsIgnoreCase("All")) {
				s+=1;
				TrainModelPanel.addTextAreaWC2(midProcess.nameDoc.get(i));
			}
		}
		TrainModelPanel.setText(""+s);
	}
}
