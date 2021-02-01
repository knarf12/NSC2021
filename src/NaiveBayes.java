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
	
	protected static Double NVBST(ArrayList<Integer> vrDoc, HashMap<Integer, ArrayList<Integer>> VtDocTr) {
		// TODO Auto-generated method stub
		Double PY = 0.1d;
		for (int i = 0; i < vrDoc.size(); i++) {
					if (vrDoc.get(i)>0) {
						PY = PY * (collectWordST(VtDocTr,i)/VtDocTr.size());
						
					}else {
						PY = PY * (collectWord2ST(VtDocTr,i)/VtDocTr.size());
						
					}
		}
		// มากกว่า 5.5 
		//System.out.println(PY);
		return PY;
	}
	
	protected static String NVprocess(ArrayList<Integer> vrDoc, HashMap<Integer, ArrayList<Integer>> VtDocTr) {
		Double PS= 0.333d; //Prob Symptom 10/30
		Double PD= 0.333d; //Prob Diagnosis 10/30
		Double PR= 0.333d; //Prob Reflection 10/30
		
		for (int i = 0; i < vrDoc.size(); i++) {
			
			if (vrDoc.get(i)>0) {
				Double []arr = collectWord(VtDocTr,i);
				PS = PS * (arr[0]/10);
				PD = PD * (arr[1]/10);
				PR = PR * (arr[2]/10);
			}else {
				Double []arr = collectWord2(VtDocTr,i);
				PS = PS * (arr[0]/10);
				PD = PD * (arr[1]/10);
				PR = PR * (arr[2]/10);
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
	
	private static Double collectWordST(HashMap<Integer, ArrayList<Integer>> VtDocTr, int n) {
		// TODO Auto-generated method stub
		Double arr= 0.0d;
		//System.out.println(VtDocTr.size());
		for (int i = 0; i < VtDocTr.size(); i++) {
			if(VtDocTr.get(i).get(n) > 0 ) {
				 arr += 1;
			}
		}
		if(arr == 0.0d ) {
			arr = 0.1d;
		}
		
		return arr;
	}
	
	private static Double collectWord2ST(HashMap<Integer, ArrayList<Integer>> VtDocTr, int n) {
		// TODO Auto-generated method stub
		Double arr= 0.0d;
		//System.out.println(VtDocTr.size());
		for (int i = 0; i < VtDocTr.size(); i++) {
			if(VtDocTr.get(i).get(n) == 0 ) {
				 arr += 1;
			}
		}
		
		if(arr == 0.0d ) {
			arr = 0.1d;
		}
		
		return arr;
	}
	
	
	private static Double[] collectWord(HashMap<Integer, ArrayList<Integer>> VtDocTr, int n) {
		// TODO Auto-generated method stub
		Double []arr= new Double[3];
		arr[0] = 0.0d ;
		arr[1] = 0.0d ;
		arr[2] = 0.0d ;
		for (int i = 0; i < VtDocTr.size(); i++) {
			if(VtDocTr.get(i).get(n) > 0 && i < 10) {
				 arr[0] += 1;
			}else if(VtDocTr.get(i).get(n)> 0 && i < 20) {
				 arr[1] += 1;
			}else if(VtDocTr.get(i).get(n)> 0 && i < 30) {
				 arr[2] += 1;
			}
		}
		if(arr[0] == 0.0d ) {
			arr[0] = 0.1d;
		}
		if(arr[1] == 0.0d){
			arr[1] = 0.1d;
		}
		if(arr[2] == 0.0d){
			arr[2]=0.1d;
		}
		return arr;
	}
	
	private static Double[] collectWord2(HashMap<Integer, ArrayList<Integer>> VtDocTr, int n) {
		// TODO Auto-generated method stub
		Double []arr= new Double[3];
		arr[0] = 0.0d ;
		arr[1] = 0.0d ;
		arr[2] = 0.0d ;
		for (int i = 0; i < VtDocTr.size(); i++) {
			if(VtDocTr.get(i).get(n)== 0 && i < 10) {
				 arr[0] += 1;
			}else if(VtDocTr.get(i).get(n)== 0 && i < 20) {
				 arr[1] += 1;
			}else if(VtDocTr.get(i).get(n)== 0 && i < 30) {
				 arr[2] += 1;
			}
		}
		
		if(arr[0] == 0.0d ) {
			arr[0] = 0.1d;
		}
		if(arr[1] == 0.0d){
			arr[1] = 0.1d;
		}
		if(arr[2] == 0.0d){
			arr[2]=0.1d;
		}
		
		return arr;
	}
	
	
}
