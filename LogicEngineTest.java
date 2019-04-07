import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LogicEngineTest {

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
	void testSimplePred() {
		Loader testL = new Loader();
		LogicEngine testLE = new LogicEngine();
		Table temp = null;
		try {
			temp = testL.LoadFile("data/m/A.csv");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Table tempP = null;
		try {
			SimplePred sim = new SimplePred("=", 0, 0);
			tempP = testLE.simplePred(temp,sim);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		try {
//			testL.testRead(tempP);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}

}
