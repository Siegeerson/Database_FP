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
			temp = testL.LoadFile("data/m/B.csv");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Table tempP = null;
		try {
			SimplePred sim = new SimplePred(">", 0, 4);
			SimplePred sim2 = new SimplePred("<", 0, 10);
			SimplePred sim3 = new SimplePred(">",0,20);
			SimplePred sim4 = new SimplePred("<", 0, 26);
			AndPred and1 = new AndPred(sim, sim2);
			AndPred and2 = new AndPred(sim4, sim3);
			OrPred or1 = new OrPred(and1,and2);
			tempP = testLE.simplePred(temp,or1);
			
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
