import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Sum {
	Table t;
	String[] toSum;
	Iterator<int[][]> inputIter;

	public Sum(IterableWithTable inputI, String[] sums) {
		toSum = sums;
		System.err.println("NEW SUM:"+inputI.toString()+" OVER:"+toString());
		inputIter = inputI.iterator();
		t = inputI.getTable();
	}

	public int[] doSum() {
		int[] result = new int[toSum.length];
		while (inputIter.hasNext()) {
			int[][] arrayList = inputIter.next();
			for (int[] inRow : arrayList) {
				if (inRow != null) {
					for (int i = 0; i < result.length; i++) {
						result[i] += inRow[t.colNames.get(toSum[i])];// get the actual row # from map
					}
				}
			}
		}
		return result;
	}
	@Override
	public String toString() {
		return Arrays.toString(toSum);
	}

}
