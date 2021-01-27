import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

public class Model {
private static PrintWriter writer;
	
	public Model() {}
	
	@SuppressWarnings("unused")
	protected static void saveModel(HashMap<Integer, ArrayList<Integer>> VtDocTr) {
		for (int i=1; i< VtDocTr.size(); i++) {
			for (int j = 0; j < VtDocTr.get(1).size(); j++) {
				writer.write(VtDocTr.get(i).get(j)+" ");
			}
			if(i <= 5) {
				writer.write("Diagnosis\n");
			}else {
				writer.write("Reflection\n");
			}
			
		}
	}
	
	protected static void createFile() {
		// TODO Auto-generated method stub
		try {
			writer = new PrintWriter("data/model/Model.txt", "UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected static void closeWrite() {
		// TODO Auto-generated method stub
		writer.close();
	}
}
