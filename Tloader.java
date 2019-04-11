import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

public class Tloader implements Iterable<Integer[][]> {

	Table t;
	TableIterator it;// need to maintain a reference to current iterator so it can be closed

	public Tloader(Table targetT) {
		t = targetT;
		it = null;
	}

	/**
	 * iterator for buffered rows
	 */
	@Override
	public Iterator<Integer[][]> iterator() {
		if (it!=null) {
			try {
				it.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			it = new TableIterator(t);//Lots of exceptions here
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return it;
	}

}
