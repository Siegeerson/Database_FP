import java.util.ArrayList;
import java.util.Iterator;

public class Sum {
	Table t;
	String[] toSum;
	Iterator<ArrayList<int[]>> inputIter;
	public Sum(IterableWithTable inputI,String[] sums) {
		inputIter = inputI.iterator();
		t = inputI.getTable();
		toSum = sums;
	}
	
	public int[] doSum() {
		int[] result = new int[toSum.length];
		while (inputIter.hasNext()) {
			ArrayList<int[]> arrayList = inputIter.next();
			for (int[] inRow : arrayList) {
				for (int i = 0; i < result.length; i++) {
					result[i] += inRow[t.colNames.get(toSum[i])];//get the actual row # from map
				}
			}
		}
		return result;
	}
	


}
