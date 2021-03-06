import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;

import javafx.scene.control.Tab;
import sun.misc.Queue;

public class Main {
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		String toLoad = scan.nextLine();		
		Map<String, Table> loaded = loadTables(toLoad);
//		System.err.println(loaded.keySet().toString());
		long startT = System.currentTimeMillis();
		int nums =  Integer.parseInt(scan.nextLine());
		for (int i = 0; i < nums; i++) {
			int[] res = executeQuery(loaded, scan);
			for (int j = 0; j < res.length; j++) {
				System.err.print(res[j] + ",");
			}
			System.err.println();
			if (i < nums - 1)
				scan.nextLine();
		}
		scan.close();
		System.err.println(System.currentTimeMillis() - startT);

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

	/**
	 * @param joinable
	 * @param joinConds
	 * @param loaded
	 * @param input     Construct by reference abstractions needed to optimize join
	 *                  orderings
	 */
	public static void processJoins(Map<String, String> joinConds, Map<String, Table> loaded, String input) {
		String nString = input.substring(6);
		String[] joins = nString.split(" AND ");
		String colN1;
		String colN2;
		for (String joinOp : joins) {
			String[] ops = joinOp.split(" = ");
			colN1 = ops[0].substring(0, 1);
			colN2 = ops[1].substring(0, 1);
//			put join conditions both ways
			joinConds.put(colN1 + colN2, colN1 + ops[0].trim().substring(3));
			joinConds.put(colN2 + colN1, colN2 + ops[1].trim().substring(3));
		}

	}

	public static Map<String, SimplePred> doPredicates(String simpPreds) {
		String[] eq = simpPreds.substring(4).replace(";", "").split("AND");
//		System.out.println(Arrays.toString(eq));
		String p;
		Map<String, SimplePred> predicates = new HashMap<String, SimplePred>();
		String cn;
		String name;
		int val;
		Scanner scan;
		for (String pred : eq) {
			scan = new Scanner(pred);
			cn = scan.next();
			name = cn.substring(0, 1);
			cn = name + cn.substring(3);
			p = scan.next();
			val = scan.nextInt();
			predicates.put(name, new SimplePred(p, cn, val));
			scan.close();
		}
		return predicates;
	}

	public static IterableWithTable putPreds(Map<String, SimplePred> preds, IterableWithTable itT) {
		if (preds.containsKey(itT.getTable().name)) {
			return new PredicateIterable(itT, preds.remove(itT.getTable().name));
		}
		return itT;
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
	 * @param canJoin
	 * @param joinConds
	 * @return Recursive method to construct join tree First shot at optimizing
	 *         assumes a correlation between num of rows and unique values this way
	 *         joins start big * small and then go where possible also put biggest
	 *         at bottom to ensure smallest amount of repeated reads this will
	 *         change once if/when i impliment merge joins
	 */
	public static IterableWithTable constructJoinTable(PriorityQueue<Table> jTables, Map<String, String> joinConds,
			Map<String, SimplePred> preds) {
		Table maxT = null;
		int maxRs = 0;
		for (Table t : jTables) {
//			find biggest table to use as first leaf
			if (t.rowNum > maxRs) {
				maxRs = t.rowNum;
				maxT = t;
			}
		}
		jTables.remove(maxT);
		System.err.print(maxT.name);

		return constJoinIterable(jTables, joinConds, putPreds(preds, (IterableWithTable) new Tloader(maxT)), preds);
	}

	/**
	 * @param canJoin
	 * @param joinConds
	 * @param baseT
	 * @return uses abstractions to do next join in tree based on the given table
	 */
	public static IterableWithTable constJoinIterable(PriorityQueue<Table> jTables, Map<String, String> joinConds,
			IterableWithTable baseT, Map<String, SimplePred> preds) {
		if (jTables.isEmpty())
			return baseT;// recursive end
		List<String> lNList = baseT.getTable().names;
		List<Table> notJoinable = new ArrayList<Table>();
		boolean joinP = false;
//		find a possible join
		Table rT = null;
		String jName = "";// the "table" that the right table is joining to
		while (!joinP) {
			rT = jTables.poll();
			Iterator<String> it = lNList.iterator();
			while (it.hasNext() && !joinP) {
				jName = it.next();
				String predName = rT.name + jName;
				if (joinConds.get(predName) != null) {
					joinP = true;

				}
			}
			if (!joinP)
				notJoinable.add(rT);
		}
		jTables.addAll(notJoinable);// might be inefficient
		System.err.print("X" + rT.name);
		String cond1 = joinConds.get(jName + rT.name);
		String cond2 = joinConds.get(rT.name + jName);
		IterableWithTable newIt = new JoinIterable(baseT, putPreds(preds, new Tloader(rT)), cond1, cond2);
		return constJoinIterable(jTables, joinConds, newIt, preds);

	}

	public static int[] executeQuery(String input, String query) {
		Map<String, Table> lTables = loadTables(input);
		return executeQuery(lTables, new Scanner(query));
	}

	/**
//	 * @param canJoin
	 * @param loaded  puts loaded tables into the can join map
	 */
	public static PriorityQueue<Table> fillQ(Map<String, Table> loaded, String input) {
		PriorityQueue<Table> pq = new PriorityQueue<>();
		String tStr = input.substring(4);
		String[] tables = tStr.split(",");
		System.err.println(Arrays.toString(tables));
		for (String string : tables) {
			System.err.println(string);
			pq.add(loaded.get(string.trim()));
		}
		return pq;
	}

	public static int[] executeQuery(Map<String, Table> lTables, Scanner scan) {
		Sum result = constructSum(lTables, scan);
		System.err.println("START");
		long start = System.nanoTime();
		int[] res = result.doSum();// do summation
		System.err.println(System.nanoTime() - start);
		System.err.println(Arrays.toString(res));
		return res;
	}

	/**
	 * @param lTables
	 * @param scan
	 * @return putting the construction of the sum in a separate method allows the
	 *         numerous data structures used in the process to be removed via
	 *         garbage collection rather than taking up space mid execution
	 */
	public static Sum constructSum(Map<String, Table> lTables, Scanner scan) {
		String[] sums = getSums(scan.nextLine());// compute what is to be summed
		String test = scan.nextLine();
		PriorityQueue<Table> pqT = fillQ(lTables, test);
//		canJoin = fillCanJoin(canJoin, lTables);
		Map<String, String> joinConds = new HashMap<String, String>();
		String joins = scan.nextLine();
		Map<String, SimplePred> predicateTree = doPredicates(scan.nextLine());
		processJoins(joinConds, lTables, joins);
		IterableWithTable topJoin = constructJoinTable(pqT, joinConds, predicateTree);
		Sum result = new Sum(topJoin, sums);// summation on top
		return result;
	}

}