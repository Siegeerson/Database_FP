import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class MainTest {


	@Test
	void testQuery() {
		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String q =
				"SELECT SUM(D.c4), SUM(F.c2), SUM(D.c3)\r\n" + 
				"FROM A, C, D, F\r\n" + 
				"WHERE A.c2 = C.c0 AND A.c3 = D.c0 AND D.c1 = F.c0\r\n" + 
				"AND D.c2 < 28957;";
		String load = "data/xxs/A.csv,data/xxs/D.csv,data/xxs/C.csv,data/xxs/F.csv";
		Table res = Main.executeQuery(load, q);
		System.out.println(res);
		
//		try {
//			l.testRead(res);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

}
