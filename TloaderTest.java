import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

class TloaderTest {

	@Test
	void test() throws IOException {
		Loader l = new Loader();
		Table t = l.LoadFile("data/M/A.csv");
		IterableWithTable it = new Tloader(t);
		long start = System.currentTimeMillis();
		Iterator<int[][]> iter = it.iterator();
		while (iter.hasNext()) {
			iter.next();
		}
		System.err.println(System.currentTimeMillis()-start);
	}

}
