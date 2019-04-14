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
				"SELECT SUM(B.c2), SUM(A.c24), SUM(B.c1)\r\n" + 
				"FROM A, B, C, D\r\n" + 
				"WHERE A.c3 = D.c0 AND C.c2 = D.c2 AND A.c1 = B.c0\r\n" + 
				"AND C.c3 > 4950;";
		String load = "data/s/C.csv,data/s/D.csv,data/s/A.csv,data/s/B.csv";
		int[] result = Main.executeQuery(load,q);
		System.out.println(Arrays.toString(result));
	}

}
