import java.util.ArrayList;
import java.util.HashMap;

public class bm25F {
	private final static Double k_1 = 1.5d;
	private final static Double b = 0.5d;
	private final static Double delta = 1.0d;
	private static Double doc_avd = 0.0d;
	
	public bm25F() {
		// TODO Auto-generated constructor stub
		
	}
	
	public static Double BM25Plus(ArrayList<Integer> VtDocTr,ArrayList<Integer> VtDocTest, ArrayList<Double> weigtingIDF) {
		// TODO Auto-generated constructor stub
		int index = 0;
		Double result = 0.0d;
		//System.out.println(doc_avd);
		for (int i = 0; i < VtDocTest.size(); i++) {
			if(VtDocTr.get(i)>0 ) {
				result += weigtingIDF.get(index) * (((VtDocTest.get(i)*(k_1+1)/(VtDocTest.get(i)+k_1*(1-b+b*(wordCount(VtDocTest)/doc_avd))))+delta));
				index +=1;
			}
		}
		//System.out.println(result);
		return result/(130);
	}
	
	public static ArrayList<Double> getAll(HashMap<Integer, ArrayList<Integer>> Doc) {
		
		Double docCount =(double)Doc.size();
		//System.out.println(Doc.get(1).size());
		ArrayList<Double> weigtingIDF = new ArrayList<Double>();
		for (int j = 0; j < Doc.get(1).size(); j++) {
			int count = 0;
			for (int i = 1; i <= Doc.size(); i++) {
				if(Doc.get(i).get(j)>0) {
					count +=1;
				}
			}
			//System.out.print(count);
			weigtingIDF.add(IDF((double)count,docCount));
			
		}
		//System.out.println();
		//System.out.println(docCount);
		return weigtingIDF;
	}

	private static Double IDF(Double word,Double docCount) {
		if(word == 0) {
			return 0.0d;
		}else {
			Double num = (docCount/word);
//			System.out.print(word +"\t");
//			System.out.println(Math.log10(num));
			return Math.log10(num);
		}
	}
	
	private static Integer wordCount(ArrayList<Integer> Doc) {
		int countDoc = 0;
		for (Integer num : Doc) {
			countDoc += num;
		}
		return countDoc;
	}
	
	public static void DocAVG(HashMap<Integer, ArrayList<Integer>> VtDocTest) {
		Double sum= 0.0d;
		for (int i = 1; i <= VtDocTest.size(); i++) {
			for (int x : VtDocTest.get(i)) {
				if(x>0) {
					sum+=x;
				}
			}
		}
		doc_avd = sum/VtDocTest.size();
		//System.out.println(doc_avd);
	} 
}
