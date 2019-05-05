import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class PredicateIterator implements Iterator<int[][]> {
	Table t;
	Iterator<int[][]> inputIt;
	Pred p;
	int[][] currIN;
	int curDex;
	public PredicateIterator(IterableWithTable inputIter, Pred predicate) {
		inputIt = inputIter.iterator();
		t = inputIter.getTable();
		p = predicate;
		curDex =0;
		currIN = inputIt.next();
	}

	@Override
	public boolean hasNext() {

		return inputIt.hasNext();
	}

	@Override
	public int[][] next() {
		int[][] result = new int[1024 * 4 / t.colNums][];
		int resIndex = 0;
		while (resIndex < result.length) {
			if(curDex==currIN.length&&inputIt.hasNext()) {
				currIN=inputIt.next();
				curDex=0;
			}else if (curDex==currIN.length) {
				return result;
			} 
			while (curDex<currIN.length&&resIndex<result.length) {
				int[] inRow = currIN[curDex];
				if(inRow!=null&&p.eval(inRow,t.colNames)) {
					result[resIndex] = inRow;
					resIndex++;
				}
				curDex++;
			}
			
		}
		return result;
	}

}
