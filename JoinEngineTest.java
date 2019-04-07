import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.scene.control.Tab;

class JoinEngineTest {

	long start;

	@BeforeEach
	void setTime() {
		start = System.nanoTime();
	}

	@AfterEach
	void stateTime() {
		System.out.println(System.nanoTime() - start);
	}
	@Test
	void testBlnJoin() throws IOException {
		Loader testL = new Loader();
		Table t1 = testL.LoadFile("data/xxxs/B.csv");
		Table t2 = testL.LoadFile("data/xxxs/B.csv");
//		System.out.println("START JOIN");
		JoinEngine je = new JoinEngine();
		Table res = je.blnJoin(t1, t2, "B0", "B0");
		System.out.println(res.colNames.keySet());
		testL.testRead(res);
	}

}
