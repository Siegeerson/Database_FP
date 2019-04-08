import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Benjamin Siege
 * This Class will be the main method that accepts input and parses into usable Strings
 */
public class Main {


	public static Map<String, Table> loadTables(String input){
		Loader load = new Loader();//TODO: make these static?
		String[] t2L = input.split(",");
		Map<String, Table> output = new HashMap<>(35);
		//TODO:test to see if there is optimal starting size, known max of 26
		for(String tPath:t2L) {
			try {
				Table tempT = load.LoadFile(tPath);
				output.put(tempT.fileName, tempT);
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
		return output;
	}
	
	/**
	 * @param input: the first line of each sql query
	 * @return
	 */
	public static String[] getSums(String input) {
		String a = input.substring(6);
		String[] sums = a.split(",");
		String[] sumVals = new String[sums.length];
		for (int i = 0; i < sumVals.length; i++) {
			sumVals[i] = sums[i].charAt(4)+""+sums[i].charAt(7);
			
		}
		return sumVals;
	}

}
