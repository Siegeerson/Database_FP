import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JoinIterableTest {
	long start;
	@BeforeEach
	void setTime() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		start = System.nanoTime();
	}

	@AfterEach
	void stateTime() {
		System.out.println(System.nanoTime() - start);
	}
	@Test
	void test() throws IOException {
		Loader a = new Loader();
		String predR = "A3";
		String predL = "D0";
		Table rT = a.LoadFile("data/m/A.csv");
		Table lT = a.LoadFile("data/m/D.csv");
		System.out.println(lT.toString()+rT.toString());
		System.out.println("LOADS DONE_"+(System.nanoTime()-start));
		start = System.nanoTime();
		IterableWithTable l = new Tloader(lT);
		IterableWithTable r = new Tloader(rT);
		IterableWithTable join = new JoinIterable(l, r, predL, predR);
		ArrayList<int[]> result = new ArrayList<>();
		Iterator<ArrayList<int[]>> joinIter = join.iterator();
		while (joinIter.hasNext()) {
//			System.out.println("JOINS");
			result.addAll(joinIter.next());
		}
		System.out.println(result.size()+"_"+result.get(0).length);
		
	}

}
