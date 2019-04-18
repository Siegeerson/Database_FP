import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class PredicateIterator implements Iterator<int[][]> {
	Table t;
	Iterator<int[][]> inputIt;
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
	public int[][] next() {
		int[][] result = new int[1024*4/t.colNums][];
		int resIndex = 0;
		while(inputIt.hasNext()&&resIndex<result.length) {
			int[][] input = inputIt.next();
			for (int[] inputRow : input) {
				System.err.println(Arrays.toString(inputRow));
				if (inputRow!=null&&p.eval(inputRow, t.colNames)) {
					result[resIndex] = inputRow;
					resIndex++;				
				}
			}
		}
		return result;
	}

}
