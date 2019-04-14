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
				"SELECT SUM(E.c1), SUM(E.c3), SUM(F.c3)\r\n" + 
				"FROM A, D, E, F\r\n" + 
				"WHERE D.c1 = F.c0 AND F.c1 = E.c1 AND A.c3 = D.c0\r\n" + 
				"AND E.c0 < -3 AND F.c3 < 0;";
		String load = "data/xs/C.csv,data/xs/D.csv,data/xs/E.csv,data/xs/A.csv,data/xs/F.csv";
		int[] result = Main.executeQuery(load,q);
		System.out.println(Arrays.toString(result));
	}

}
