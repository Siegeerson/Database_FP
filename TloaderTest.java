import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

class TloaderTest {
	
	@Test
	void testIterator() throws IOException {
		Loader l = new Loader();
		Table t = l.LoadFile("data/xs/a.csv");
		IterableWithTable tlTest = new Tloader(t);
		Iterator<ArrayList<int[]>> rowIter = tlTest.iterator();
		ArrayList<int[]> intA = rowIter.next();
		ArrayList<int[]> intA2 = tlTest.iterator().next();
		System.out.println(tlTest.getTable().colNames.keySet());
		System.out.println(Arrays.toString(intA.get(0))+"\n"+Arrays.toString(intA2.get(0)));
		
	}

}
