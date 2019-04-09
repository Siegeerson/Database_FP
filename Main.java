import java.io.IOException;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Benjamin Siege This Class will be the main method that accepts input
 *         and parses into usable Strings
 */
public class Main {

//	TODO:Use a stack for joins, push all tables in with thier corresponding join conditions in different table

	public static Map<String, Table> loadTables(String input) {
		Loader load = new Loader();// TODO: make these static?
		String[] t2L = input.split(",");
		Map<String, Table> output = new HashMap<>(35);
		// TODO:test to see if there is optimal starting size, known max of 26
		for (String tPath : t2L) {
			try {
				Table tempT = load.LoadFile(tPath);
				output.put(tempT.fileName, tempT);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return output;
	}

	public static void putTablesJoins(Map<String, Table> loadedT, Deque<Table> tables, Deque<String> pred,
			String input) {
		String nString = input.substring(6);
		String[] joins = nString.split(" AND ");
		String colN1;
		String colN2;
		for (String joinOp : joins) {
			colN1 = joinOp.substring(0, 1);
			colN2 = joinOp.substring(7, 8);
			tables.add(loadedT.get(colN1));
			tables.add(loadedT.get(colN2));
			pred.add(colN1 + joinOp.charAt(3));
			pred.add(colN2 + joinOp.charAt(10));

		}

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
			sumVals[i] = sums[i].charAt(4) + "" + sums[i].charAt(7);

		}
		return sumVals;
	}

	public static Table doPredicates(String simpPreds, Table toFilter) {
		LogicEngine le = new LogicEngine();
		String[] eq = simpPreds.replace(";", "").split("AND");
		if (eq.length == 1) {
			String predicate = eq[0].trim();
			String operation = predicate.substring(5, 6);
			String colN = predicate.substring(0, 1) + predicate.substring(3, 4);
			int value = Integer.parseInt(predicate.substring(7));
			try {
				return le.simplePred(toFilter, new SimplePred(operation, colN, value));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

//		TODO:deal with multiple preds
		return null;
	}

}
