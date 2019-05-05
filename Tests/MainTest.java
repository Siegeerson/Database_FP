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
<<<<<<< HEAD
				"SELECT SUM(B.c2), SUM(A.c24), SUM(B.c1)\r\n" + 
				"FROM A, B, C, D\r\n" + 
				"WHERE A.c3 = D.c0 AND C.c2 = D.c2 AND A.c1 = B.c0\r\n" + 
				"AND C.c3 > 4950;";
		String load = "data/s/C.csv,data/s/D.csv,data/s/A.csv,data/s/B.csv";
=======
				"SELECT SUM(E.c1), SUM(E.c3), SUM(F.c3)\r\n" + 
				"FROM A, D, E, F\r\n" + 
				"WHERE D.c1 = F.c0 AND F.c1 = E.c1 AND A.c3 = D.c0\r\n" + 
				"AND E.c0 < -3 AND F.c3 < 0;";
		String load = "../DBMS_Project/data/xs/C.csv,data/xs/D.csv,data/xs/E.csv,data/xs/A.csv,data/xs/F.csv";
>>>>>>> 7e965cf7327ad885ddd5cc308398654e98a23361
		int[] result = Main.executeQuery(load,q);
		System.out.println(Arrays.toString(result));
	}

}
