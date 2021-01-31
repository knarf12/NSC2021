import java.util.ArrayList;
import java.util.HashMap;

public class NaiveBayes {
	/*
	 * 
	 * P(C|X) = P(X|C)*P(C)/P(X)
	 * 
	 * C = Class = { symptom, diagnosis, reflection }
	 * X = Attribute
	 * P = Probability 
	 */
	
	public NaiveBayes() {
		
	}
	
	protected static String NVprocess(ArrayList<Integer> vrDoc, HashMap<Integer, ArrayList<Integer>> VtDocTr) {
		Double PS= 0.333d; //Prob Symptom 10/30
		Double PD= 0.333d; //Prob Diagnosis 10/30
		Double PR= 0.333d; //Prob Reflection 10/30
		
		for (int i = 0; i < vrDoc.size(); i++) {
			Double []arr= new Double[3];
			if (vrDoc.get(i)>0) {
				arr = collectWord(VtDocTr,i);
				PS = PS * arr[0];
				PD = PD * arr[1];
				PR = PR * arr[2];
			}else {
				arr = collectWord2(VtDocTr,i);
				PS = PS * arr[0];
				PD = PD * arr[1];
				PR = PR * arr[2];
			}
		}
		
		if(PS>=PD && PS>=PR) {
			return "symptom" ;
		}else if(PD>=PS && PD>=PR) {
			return "diagnosis" ;
		}else{
			return "reflection" ;
		}
		
	}
	
	private static Double[] collectWord(HashMap<Integer, ArrayList<Integer>> VtDocTr, int n) {
		// TODO Auto-generated method stub
		Double []arr= new Double[3];
		for (int i = 0; i < VtDocTr.size(); i++) {
			if(VtDocTr.get(i).get(n)> 0 && i < 10) {
				 arr[0] += 1;
			}else if(VtDocTr.get(i).get(n)> 0 && i < 20) {
				 arr[1] += 1;
			}else if(VtDocTr.get(i).get(n)> 0 && i < 30) {
				 arr[2] += 1;
			}
		}
		return arr;
	}
	
	private static Double[] collectWord2(HashMap<Integer, ArrayList<Integer>> VtDocTr, int n) {
		// TODO Auto-generated method stub
		Double []arr= new Double[3];
		for (int i = 0; i < VtDocTr.size(); i++) {
			if(VtDocTr.get(i).get(n)== 0 && i < 10) {
				 arr[0] += 1;
			}else if(VtDocTr.get(i).get(n)== 0 && i < 20) {
				 arr[1] += 1;
			}else if(VtDocTr.get(i).get(n)== 0 && i < 30) {
				 arr[2] += 1;
			}
		}
		return arr;
	}
	
	
}
