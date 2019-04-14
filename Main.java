import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Main {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		Map<String, Table> loaded = loadTables(scan.nextLine());
		int nums = scan.nextInt();
		for (int i = 0; i < nums; i++) {
			int[] res = executeQuery(loaded, scan);
			for (int j = 0; j < res.length; j++) {
				System.out.print(res[j] + ",");
			}
			System.out.println();
			scan.nextLine();
		}

	}

	/**
	 * load in tables
	 * 
	 * @param input
	 * @return
	 */
	public static Map<String, Table> loadTables(String input) {
		Loader load = new Loader();// TODO: make these static?
		String[] t2L = input.split(",");
		Map<String, Table> output = new HashMap<>(35);
		// TODO:test to see if there is optimal starting size, known max of 26
		for (String tPath : t2L) {
			try {
				Table tempT = load.LoadFile(tPath);
				output.put(tempT.name, tempT);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return output;
	}

	public static void putTablesJoins(Map<String, IterableWithTable> loadedT, ArrayDeque<IterableWithTable> tables,
			ArrayDeque<String> pred, String input) {
		String nString = input.substring(6);
		String[] joins = nString.split(" AND ");
		String colN1;
		String colN2;
		int maxSize = 0;
		Set<String> pushedT = new HashSet<>();
		for (String joinOp : joins) {
			String[] ops = joinOp.split(" = ");
			colN1 = ops[0].substring(0, 1);
			boolean unordered = false;
			if (pushedT.add(colN1)) {
				tables.add(loadedT.get(colN1));
			}
			colN2 = ops[1].substring(0, 1);
			if (pushedT.add(colN2)) {
				tables.add(loadedT.get(colN2));
			} else {
				unordered = true;// weird ordering edge case
			}
			if (unordered) {
				pred.add(colN2 + ops[1].trim().substring(3));
				pred.add(colN1 + ops[0].trim().substring(3));
			} else {
				pred.add(colN1 + ops[0].trim().substring(3));
				pred.add(colN2 + ops[1].trim().substring(3));
			}

		}

//		System.out.println(pred.size() + "_" + tables.size());

	}

	public static void bigToSmall() {

	}

	public static void doPredicates(String simpPreds, Map<String, IterableWithTable> baseTables) {
		String[] eq = simpPreds.substring(4).replace(";", "").split("AND");
//		System.out.println(Arrays.toString(eq));
		String p;
		String cn;
		int val;
		Scanner scan;
		for (String pred : eq) {
			scan = new Scanner(pred);
			cn = scan.next();
			String tn = cn.substring(0, 1);
			cn = tn + cn.substring(3);
			p = scan.next();
			val = scan.nextInt();
			PredicateIterable tempPredI = new PredicateIterable(baseTables.get(tn), new SimplePred(p, cn, val));
			baseTables.put(tn, tempPredI);
			scan.close();
		}

	}

	public static String[] getSums(String input) {
		String a = input.substring(6);
		String[] sums = a.split(",");
		String[] sumVals = new String[sums.length];
		for (int i = 0; i < sumVals.length; i++) {
			sumVals[i] = sums[i].charAt(5) + sums[i].split("\\)")[0].substring(8);

		}
		return sumVals;
	}

	/**
	 * @param tables
	 * @param preds
	 * 
	 *               Basic l-Deep tree constructor, no use of metaData
	 * @return
	 */
	public static IterableWithTable constructJoinIterable(Deque<IterableWithTable> tables, Deque<String> preds) {
		IterableWithTable topIterable = tables.pop();
		while (!tables.isEmpty()) {
//			Construct left deep tree
			topIterable = new JoinIterable(topIterable, tables.pop(), preds.pop(), preds.pop());
		}
		return topIterable;
	}

	public static int[] executeQuery(String input, String query) {
		Map<String, Table> lTables = loadTables(input);
		return executeQuery(lTables, new Scanner(query));
	}

	/**
	 * @param inputs
	 * @return Makes a map from a table name to the "input stream" this map can then
	 *         be modified by pushing it down and replacing it with a predicate
	 *         iterator
	 */
	public static Map<String, IterableWithTable> putInIterators(Map<String, Table> inputs) {
		Map<String, IterableWithTable> a = new HashMap<String, IterableWithTable>();
		for (String name : inputs.keySet()) {
			a.put(name, new Tloader(inputs.get(name)));
		}
		return a;
	}

	/**
	 * @param lTables
	 * @param scan
	 * @return Goes from load -> simple predicates (>,<,=) which are fast to joins
	 *         to sums
	 */
	public static int[] executeQuery(Map<String, Table> lTables, Scanner scan) {
		Map<String, IterableWithTable> loads = putInIterators(lTables);
		String[] sums = getSums(scan.nextLine());
		scan.nextLine();
		ArrayDeque<IterableWithTable> table = new ArrayDeque<IterableWithTable>();
		ArrayDeque<String> preds = new ArrayDeque<String>();
		String joins = scan.nextLine();
		String predicates = scan.nextLine();

		doPredicates(predicates, loads);// change by reference
		putTablesJoins(loads, table, preds, joins);
		IterableWithTable topJoin = constructJoinIterable(table, preds);
//		IterableWithTable predIter = new PredicateIterable(topJoin, predicateTree); no longer needed
		Sum result = new Sum(topJoin, sums);
		System.out.println("START");
		long start = System.nanoTime();
		int[] res = result.doSum();
		System.out.println(System.nanoTime() - start);
		scan.close();
		return res;
	}

}