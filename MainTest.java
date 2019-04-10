import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class MainTest {

	@Test
	void testPutTablesJoins() {
		Map<String, Table> loadTables = new HashMap<String, Table>();
		loadTables.put("A", new Table("A", 0));
		loadTables.put("C", new Table("B", 0));
		loadTables.put("D", new Table("C", 0));
		Deque<String> preds = new ArrayDeque<>();
		Deque<Table> tabs = new ArrayDeque<Table>();
		String input = "WHERE A.c2 = C.c0 AND A.c3 = D.c0 AND C.c2 = D.c2";
		Main.putTablesJoins(loadTables, tabs, preds, input);
		System.out.println(input);

	}

	@Test
	void testQuery() {
		String q = "SELECT SUM(D.c2), SUM(A.c29), SUM(C.c1)\r\n" + 
				"FROM A, B, C, D\r\n" + 
				"WHERE A.c2 = C.c0 AND A.c1 = B.c0 AND A.c3 = D.c0\r\n" + 
				"AND A.c29 > 1;\r\n";
		String load = "data/xs/A.csv,data/xs/B.csv,data/xs/C.csv,data/xs/D.csv";
		Table res = Main.executeQuery(load, q);
		System.out.println(res);
		Loader l = new Loader();
		try {
			l.testRead(res);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
