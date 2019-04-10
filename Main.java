import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * @author Benjamin Siege This Class will be the main method that accepts input
 *         and parses into usable Strings
 */
public class Main {

//	TODO:Use a stack for joins, push all tables in with thier corresponding join conditions in different table

	public static void main(String[] args) {
		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String q =
				"SELECT SUM(D.c2), SUM(A.c29), SUM(C.c1)\r\n" + 
				"FROM A, B, C, D\r\n" + 
				"WHERE A.c2 = C.c0 AND A.c1 = B.c0 AND A.c3 = D.c0\r\n" + 
				"AND A.c29 = 5376;";
		String load = "data/xs/A.csv,data/xs/D.csv,data/xs/C.csv,data/xs/B.csv";
		Table res = Main.executeQuery(load, q);
		System.out.println(res);
	}
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
			String[] ops = joinOp.split(" = ");
			colN1 = ops[0].substring(0,1);
			boolean unordered = false;
			if (pushedT.add(colN1)) {
				tables.add(loadedT.get(colN1));
			}
			colN2 = ops[1].substring(0,1);
			if (pushedT.add(colN2)) {
				tables.add(loadedT.get(colN2));
			}else {
				unordered = true;//weird ordering edge case
			}
			if (unordered) {
				pred.add(colN2 + ops[1].trim().substring(3));
				pred.add(colN1 + ops[0].trim().substring(3));
			} else {
				pred.add(colN1 + ops[0].trim().substring(3));
				pred.add(colN2 + ops[1].trim().substring(3));
			}

		}
		System.out.println(pred.size() + "_" + tables.size());

	}

	/**
	 * @param input: the first line of each sql query
	 * @return
	 */
//	TODO:fix sum gathering
	public static String[] getSums(String input) {
		String a = input.substring(6);
		String[] sums = a.split(",");
		String[] sumVals = new String[sums.length];
		for (int i = 0; i < sumVals.length; i++) {
			sumVals[i] = sums[i].charAt(5)+sums[i].split("\\)")[0].substring(8) ;

		}
		return sumVals;
	}

	public static Table doPredicates(String simpPreds, Table toFilter) {
		LogicEngine le = new LogicEngine();
		String[] eq = simpPreds.substring(4).replace(";", "").split("AND");
		System.out.println(Arrays.toString(eq));
		Deque<Pred> preds = new ArrayDeque<>();
		String p;
		String cn;
		int val;
		Scanner scan;
		for (String pred : eq) {
			scan = new Scanner(pred);
			cn = scan.next();
			cn = cn.substring(0,1)+cn.substring(3);
			p = scan.next();
			val=scan.nextInt();
			preds.add(new SimplePred(p, cn, val));
			scan.close();
		}
		while(preds.size()>1) {
			preds.add(new AndPred(preds.pop(), preds.pop()));
		}
		try {
			return le.simplePred(toFilter, preds.pop());
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		System.out.println(preds);
		Table joinRe = doJoins(table, preds);
		System.out.println(table);
		Table simPreds = doPredicates(scan.nextLine(), joinRe);
		LogicEngine e= new LogicEngine();
		try {
			e.outputSums(simPreds, sums);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		scan.close();
		return simPreds;

	}

	public static Table doJoins(Deque<Table> tables, Deque<String> preds) {
		JoinEngine je = new JoinEngine();
		while (!preds.isEmpty()&&tables.size()>1) {
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
