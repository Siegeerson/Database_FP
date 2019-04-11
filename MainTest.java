import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MainTest {
	long start;
	@BeforeEach
	void setTime() throws InterruptedException {
//		Thread.sleep(5000);
		start = System.nanoTime();
	}

	@AfterEach
	void stateTime() {
		System.out.println(System.nanoTime() - start);
	}
	@Test
	void test() {
		String q =
				"SELECT SUM(B.c0), SUM(B.c1), SUM(D.c2), SUM(D.c3)\r\n" + 
				"FROM A, B, C, D\r\n" + 
				"WHERE A.c1 = B.c0 AND A.c3 = D.c0 AND C.c2 = D.c2\r\n" + 
				"AND B.c2 = -1257;";
		String load = "data/xs/A.csv,data/xs/B.csv,data/xs/C.csv,data/xs/D.csv";
		int[] result = Main.executeQuery(load,q);
		System.out.println(Arrays.toString(result));
	}

}
