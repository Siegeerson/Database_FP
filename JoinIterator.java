import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class JoinIterator implements Iterator<ArrayList<int[]>> {

	IterableWithTable leftIt;
	IterableWithTable rightIt;
	Iterator<ArrayList<int[]>> leftItor;
	Iterator<ArrayList<int[]>> rightItor;
	ArrayList<int[]> currentLeft;
	int lCond;
	int rCond;
	int colN;

	public JoinIterator(IterableWithTable lI, IterableWithTable rI, String lCon, String rCon, Table t1, Table t2) {
		leftIt = lI;
		rightIt = rI;
		lCond = t1.colNames.get(lCon);
		rCond = t2.colNames.get(rCon);
		leftItor = leftIt.iterator();
		rightItor = rightIt.iterator();
		colN = t1.colNums + t2.colNums;
	}

	@Override
	public boolean hasNext() {
		return leftItor.hasNext();
	}

	/**
	 *Preforms BLN join for one block from the right branch for each next call
	 */
	@Override
	public ArrayList<int[]> next() {
		ArrayList<int[]> result = new ArrayList<>();
		if (currentLeft == null) {
			currentLeft = leftItor.next();
		}
//		TODO:always call has next before next
		if (!rightItor.hasNext()) {
			rightItor = rightIt.iterator();
			currentLeft = leftItor.next();
		}
		ArrayList<int[]> rightRow = rightItor.next();
//		System.out.println(rightRow.size());
		for (int[] left : currentLeft) {
			for (int[] right : rightRow) {
				if (left[lCond] == right[rCond]) {
					int[] tempAr = Arrays.copyOf(left, left.length + right.length);// check if this is efficient
					System.arraycopy(right, 0, tempAr, left.length, right.length);
					// Thank you Joachim Sauer on stackoverflow
					result.add(tempAr);
				}
			}
		}
		return result;

	}

}