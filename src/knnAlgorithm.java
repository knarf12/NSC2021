
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class knnAlgorithm {
	
	/*
	 * กำหนดขนาดของ k
	 * คำนวณระยะห่าง (Distance)
	 * จัดเรียงลำดับของระยะห่าง และเลือกพิจารณาชุดข้อมูลที่ใกล้จุดที่ต้องการพิจารณาตามจำนวน k ที่กำหนดไว้
	 * พิจารณาข้อมูลจำนวน k ชุด และสังเกตว่ากลุ่ม (Class) ไหนที่ใกล้จุดที่พิจารณาเป็นจำนวนมากที่สุด
	 * กำหนด Class ให้กับจุดที่พิจารณา
	 * 
	 * d = sqr((x2-x1)^2 + (y2-y1)^2) Distance
	 * 
	 * 
	 */
	
	private static int K = 0;
//	private static HashMap<Integer, ArrayList<Integer>> VtDocTr = new HashMap<Integer, ArrayList<Integer>>();
	private static mathMethod math = new mathMethod();
	static DecimalFormat df2 = new DecimalFormat("#.##");
	public knnAlgorithm() {	
		
	}

	
	private static Double distance(int A , int B) {
		if ((A-B) == 0) {
			return 0.0d;
		}else {
			Double num = Math.pow((A-B),2);
			return (num);
		}
	}
	
	@SuppressWarnings("unused")
	static void checkKNN(ArrayList<Integer> VtDocTest, HashMap<Integer, ArrayList<Integer>> VtDocTr) {
		Double[] distanceDoc = new Double[VtDocTr.size()];
		Double[] distanceMin = new Double[VtDocTr.size()];
		Double[] distanceDocOri = new Double[VtDocTr.size()];
		//System.out.println(VtDocTest);
		//System.out.println(VtDocTr.size());
		for (int j=0; j< VtDocTr.size(); j++) {
			Double sum = 0.0d;
			for (int i=0; i < VtDocTest.size(); i++) {
				sum =sum + distance(VtDocTest.get(i),VtDocTr.get(j).get(i));
			}
			distanceDocOri[j]=Math.sqrt(sum);
			distanceDoc[j]=Math.sqrt(sum);
		}
		distanceMin = bubbleSort(distanceDoc);
		
	}
	
	static String checkKNNDoc(ArrayList<Integer> VtDocTest, HashMap<Integer, ArrayList<Integer>> VtDocTr) {
		Double[] distanceDoc = new Double[VtDocTr.size()];
		Double[] distanceDocOri = new Double[VtDocTr.size()];
		Double[] distanceMin = new Double[VtDocTr.size()];
		
		//System.out.println(VtDocTest);
		//System.out.println(VtDocTr.size());
		for (int j=0; j< VtDocTr.size(); j++) {
			Double sum = 0.0d;
			for (int i=0; i < VtDocTest.size(); i++) {
				sum =sum + distance(VtDocTest.get(i),VtDocTr.get(j).get(i));
			}
			//System.out.println(Math.sqrt(sum));
			distanceDoc[j]=Math.sqrt(sum);
			distanceDocOri[j]=Math.sqrt(sum);
		}//System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++");

		distanceMin = bubbleSort(distanceDoc);
		
//		for (int i=0; i< distanceMin.length; i++) {
//			System.out.println(df2.format(distanceMin[i])+"\t"+df2.format(distanceDocOri[i]));
//		}System.out.println();
		
		//System.out.println(distanceDocOri.length);
		
		int s=0,d=0,r=0;
		for (int i=0; i< 9; i++) {
			for (int j = 0; j < distanceDocOri.length; j++) {
				if(distanceMin[i].equals(distanceDocOri[j])  && j < 10) {
					d+=1;
				}else if(distanceMin[i].equals(distanceDocOri[j])  && j < 20) {
					r+=1;
				}else if(distanceMin[i].equals(distanceDocOri[j])  && j < 30){
					s+=1;
				}
			}
		}
		
		if(s>=d && s>=r) {
			return "symptom" ;
		}else if(d>=s && d>=r) {
			return "diagnosis" ;
		}else{
			return "reflection" ;
		}
	}
	
	static Double[] bubbleSort(Double[] arr) 
	{ 
	    int n = arr.length; 
	    for (int i = 0; i < n-1; i++) {
	    	 for (int j = 0; j < n-i-1; j++) {
	    		 if (arr[j] > arr[j+1]) 
		            { 
		                // swap arr[j+1] and arr[j] 
		            	Double temp = arr[j]; 
		                arr[j] = arr[j+1]; 
		                arr[j+1] = temp; 
		            } 
		      } 
	    }
	    return arr;
	} 


	
}
