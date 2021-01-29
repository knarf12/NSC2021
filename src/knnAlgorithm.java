import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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

	public knnAlgorithm() {	
		
	}
	
//	protected static void loadTrainmodel(){
//		BufferedReader reader;
//		try {
//			reader = new BufferedReader(new FileReader(
//					"data/model/Model.txt"));
//			String line = reader.readLine();
//			int i = 1;
//			while (line != null) {
//				ArrayList<String> wordAll = new  ArrayList<String>();
//				String name = "";
//				for(String word : line.split(" ")) {
//						wordAll.add(word); 
//				}
//				
//				VtDocTr.put(i, wordAll);
//				i++;
//				line = reader.readLine();
//			}
//			
//			reader.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
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
		Double[] distanceMax = new Double[VtDocTr.size()];
		//System.out.println(VtDocTest);
		//System.out.println(VtDocTr.size());
		for (int j=0; j< VtDocTr.size(); j++) {
			Double sum = 0.0d;
			for (int i=0; i < VtDocTest.size(); i++) {
				sum =sum + distance(VtDocTest.get(i),VtDocTr.get(j).get(i));
			}
			distanceDoc[j]=Math.sqrt(sum);
		}
		distanceMax = bubbleSort(distanceDoc);
		
		for (int i=0; i< distanceMax.length; i++) {
			System.out.println(distanceMax[i]+"\t"+distanceDoc[i]);
		}System.out.println();
	}
	
	static void checkKNNDoc(ArrayList<Integer> VtDocTest, HashMap<Integer, ArrayList<Integer>> VtDocTr) {
		Double[] distanceDoc = new Double[VtDocTr.size()];
		Double[] distanceMax = new Double[VtDocTr.size()];
		//System.out.println(VtDocTest);
		//System.out.println(VtDocTr.size());
		for (int j=0; j< VtDocTr.size(); j++) {
			Double sum = 0.0d;
			for (int i=0; i < VtDocTest.size(); i++) {
				sum =sum + distance(VtDocTest.get(i),VtDocTr.get(j).get(i));
			}
			System.out.println(Math.sqrt(sum));
			distanceDoc[j]=Math.sqrt(sum);
		}//System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++");
		distanceMax = bubbleSort(distanceDoc);
		
//		for (int i=0; i< distanceMax.length; i++) {
//			System.out.println(distanceMax[i]+"\t"+distanceDoc[i]);
//		}System.out.println();
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
