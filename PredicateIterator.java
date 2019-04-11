import java.util.ArrayList;
import java.util.Iterator;

public class PredicateIterator implements Iterator<ArrayList<int[]>> {
	Table t;
	Iterator<ArrayList<int[]>> inputIt;
	Pred p;
	public PredicateIterator(IterableWithTable inputIter,Pred predicate) {
		inputIt = inputIter.iterator();
		t = inputIter.getTable();
		p = predicate;
	}
	@Override
	public boolean hasNext() {
		
		return inputIt.hasNext();
	}

	@Override
	public ArrayList<int[]> next() {
		ArrayList<int[]> result = new ArrayList<>();
		ArrayList<int[]> input = inputIt.next();
		for (int[] inputRow : input) {
			if (p.eval(inputRow, t.colNames)) {
				result.add(inputRow);
			}
		}
		return result;
	}

}
