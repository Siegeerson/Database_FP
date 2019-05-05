import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TloaderTest {
	long start;

	@BeforeEach
	void setTime() throws InterruptedException {
		Thread.sleep(5000);
		start = System.nanoTime();
	}

	@AfterEach
	void stateTime() {
		System.out.println(System.nanoTime() - start);
	}

	@Test
	void testIterator() throws IOException {
		Loader l = new Loader();
		Table t = l.LoadFile("data/m/a.csv");
		IterableWithTable tlTest = new Tloader(t);
		TableIterator rowIter = (TableIterator) tlTest.iterator();

		int i =0;
		while (rowIter.hasNext()) {
			rowIter.next();
			i++;
		}
		System.out.println(i+"_"+rowIter.rowsRead);
		
	}

}
