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
	private static HashMap<Integer, ArrayList<String>> VtDocTr = new HashMap<Integer, ArrayList<String>>();
	private static mathMethod math = new mathMethod();

	public knnAlgorithm() {	
		
	}
	
	protected static void loadTrainmodel(){
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(
					"data/model/Model.txt"));
			String line = reader.readLine();
			int i = 1;
			while (line != null) {
				ArrayList<String> wordAll = new  ArrayList<String>();
				String name = "";
				for(String word : line.split(" ")) {
						wordAll.add(word); 
				}
				
				VtDocTr.put(i, wordAll);
				i++;
				line = reader.readLine();
			}
			
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static Double distance(int A , int B) {
		if ((A-B) == 0) {
			return 0.0d;
		}else {
			Double num = Math.pow((A-B),2);
			return Math.sqrt(num);
		}
	}
	
	
	@SuppressWarnings("unused")
	private static void checkK(ArrayList<Integer> VtDocTest) {
		double[] distanceDoc = new double[VtDocTr.size()];
		double[] distanceMax = new double[VtDocTr.size()];
		for (int j=1; j<= VtDocTr.size(); j++) {
			Double sum = 0.0d;
			//System.out.println(VtDocTr.get(j));
			for (int i=0; i < VtDocTest.size(); i++) {
				if(i<VtDocTr.get(j).size()-1) {
					int parseInt = Integer.parseInt(VtDocTr.get(j).get(i));
					sum += sum + distance(VtDocTest.get(i),parseInt);
				}
			}
			distanceDoc[j-1]=(sum);
		}
		
		distanceMax = math.bubbleSort(distanceDoc);
		
		for (int i=0; i< distanceMax.length; i++) {
			System.out.println(distanceMax[i]);
		}
	}
	
}
