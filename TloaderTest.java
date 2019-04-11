import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

class TloaderTest {
	
	@Test
	void testIterator() throws IOException {
		Loader l = new Loader();
		Table t = l.LoadFile("data/xs/a.csv");
		Tloader tlTest = new Tloader(t);
		Iterator<Integer[][]> rowIter = tlTest.iterator();
		Integer[][] intA = rowIter.next();
		Integer[][] intA2 = tlTest.iterator().next();
		System.out.println(Arrays.toString(intA[0])+"\n"+Arrays.toString(intA2[0]));
		
	}

}
