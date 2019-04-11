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
				"SELECT SUM(A.c0), SUM(E.c4), SUM(C.c1), SUM(C.c2)\r\n" + 
				"FROM A, C, D, E\r\n" + 
				"WHERE C.c1 = E.c0 AND C.c2 = D.c2 AND A.c3 = D.c0\r\n" + 
				"AND D.c2 < 13445983;";
		String load = "data/s/C.csv,data/s/D.csv,data/s/E.csv,data/s/A.csv";
		int[] result = Main.executeQuery(load,q);
		System.out.println(Arrays.toString(result));
	}

}
