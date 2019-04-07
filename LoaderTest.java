import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoaderTest {
	long start;
	
	@BeforeEach
	void setTime() {
		start=System.nanoTime();
	}
	@AfterEach
	void stateTime() {
		System.out.println(System.nanoTime()-start);
	}
	@Test
	void testLoadFile() {
		
		Loader test = new Loader();
		Table temp = null;
		try {
			temp = test.LoadFile("data/m/C.csv");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		try {
//			test.testRead(temp);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	@Test
	void testHashLoad() {
		Loader test = new Loader();
		try {
			Map<String, Table> res = test.loadAll("data/m/C.csv,data/m/F.csv");
			for (Table t : res.values()) {
				System.out.println(t);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
