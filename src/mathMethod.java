import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class mathMethod {
	public mathMethod() {
		// TODO Auto-generated constructor stub
	}
	
	//top 5 rating
	public static void maxSimDoc(int column,  HashMap<Integer, ArrayList<Double>> evalua) {
		double[] max = new double[evalua.size()];
		for (int i = 1; i <= evalua.size(); i++) {
			max[i-1] = evalua.get(i).get(column);
		}
		max = bubbleSort(max);
		int num = 0;
		for (int i = 0; i < max.length; i++) {
			for (int j = 1; j <= max.length; j++) {
				if (evalua.get(j).get(column) == max[i]) {
					//System.out.println("Similarity Doc "+(column+1)+" is Doc "+ j + " = "+ max[i]);
					num +=1;
				}
			}
			if(num==5)break;
		}
	}
	
	//sort
	public static double[] bubbleSort(double[] max) {  
        int n = max.length;  
        double temp = 0; 
        
         for(int i=0; i < n; i++){  
                 for(int j=1; j < (n-i); j++){  
                          if(max[j-1] < max[j]){  
                                 //swap elements  
                                 temp = max[j-1];  
                                 max[j-1] = max[j];  
                                 max[j] = temp; 
                         }    
                 }
         }
         return max;  
    }
	
	//Vector
	public static ArrayList<Integer> getVector(List<String> doc, ArrayList<String> wordList) {
			ArrayList<Integer> vc = new ArrayList<Integer>();
			for (String word : wordList) {
				vc.add(vector(doc, word));
//				System.out.print(vector(doc, word) + "\t");
			}//System.out.println();
		return vc;
	}
	
	public static Integer vector(List<String> doc, String term) {
	    int result = 0;
	    for (String word : doc) {
	    	if (term.equalsIgnoreCase(word))result++;
	    }
	    return result;
	}
	
	//Cosine similarity
	public static double CosineSim(List<Integer> doc1, List<Integer> doc2) {
		double sum=0d;
		double d1 = Sum(doc1);
		double d2 = Sum(doc2);
		double dot = 0d;
		
		for (int i = 0; i < doc1.size(); i++) {
			dot = dot + doc1.get(i)*doc2.get(i);
		}
		sum =(dot/(Math.sqrt(d1) * Math.sqrt(d2))) ;
		return sum;
	}

	//math sum
	public static double Sum(List<Integer> doc) {
		double sum=0;
		for (Integer integer : doc) {
			sum=sum+(Math.pow(integer,2));
		}
		return sum;
	}
	
	//check number
	public static boolean isNumeric(String strNum) {
	    if (strNum == null) {return true;}
	    try {
	        double d = Double.parseDouble(strNum);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
}
