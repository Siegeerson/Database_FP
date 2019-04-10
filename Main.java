import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

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

	/**
	 * @param loadedT
	 * @param tables
	 * @param pred
	 * @param input   Takes in a place to put tables to be joined and their
	 *                predicates
	 */
	public static void putTablesJoins(Map<String, Table> loadedT, Deque<Table> tables, Deque<String> pred,
			String input) {
		String nString = input.substring(6);
		String[] joins = nString.split(" AND ");
		String colN1;
		String colN2;
		Set<String> pushedT = new HashSet<>();
		for (String joinOp : joins) {
			colN1 = joinOp.substring(0, 1);
			boolean unordered = false;
			if (pushedT.add(colN1)) {
				tables.add(loadedT.get(colN1));
			}
			colN2 = joinOp.substring(7, 8);
			if (pushedT.add(colN2)) {
				tables.add(loadedT.get(colN2));
			}else {
				unordered = true;//weird ordering edge case
			}
			if (unordered) {
				pred.add(colN2 + joinOp.charAt(10));
				pred.add(colN1 + joinOp.charAt(3));
			} else {
				pred.add(colN1 + joinOp.charAt(3));
				pred.add(colN2 + joinOp.charAt(10));
			}

		}
		System.out.println(pred.size() + "_" + tables.size());

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
		System.out.println(Arrays.toString(eq));
		if (eq.length == 2 &&eq[0].equals("")) {
//			TODO:Change getting of info to account for shorter or longer column numbers
			String predicate = eq[1].trim();
			String operation = predicate.substring(6,7);
			String colN = predicate.substring(0, 1) + predicate.substring(3, 4);
			int value = Integer.parseInt(predicate.substring(7).trim());
			try {
				return le.simplePred(toFilter, new SimplePred(operation, colN, value));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

//		TODO:deal with multiple preds
		return null;
	}

	/**
	 * @param input
	 * @param query -> test query
	 */
	public static Table executeQuery(String input, String query) {
		Map<String, Table> lTables = loadTables(input);
		Scanner scan = new Scanner(query);
		String[] sums = getSums(scan.nextLine());
		scan.nextLine();
		Deque<Table> table = new ArrayDeque<Table>();
		Deque<String> preds = new ArrayDeque<String>();
		putTablesJoins(lTables, table, preds, scan.nextLine());
		Table joinRe = doJoins(table, preds);
		Table simPreds = doPredicates(scan.nextLine(), joinRe);
		return simPreds;

	}

	public static Table doJoins(Deque<Table> tables, Deque<String> preds) {
		JoinEngine je = new JoinEngine();
		while (!preds.isEmpty()) {
			try {
				System.out.println(tables.size());
				tables.push(je.blnJoin(tables.pop(), tables.pop(), preds.pop(), preds.pop()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return tables.pop();
	}

}
