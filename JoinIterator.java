import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class JoinIterator implements Iterator<int[][]> {

	IterableWithTable leftIt;
	IterableWithTable rightIt;
	Iterator<int[][]> leftItor;
	Iterator<int[][]> rightItor;
	int[][] currentLeft;
	Table tl;
	Table tr;
	int lCurr;// index in left block
	int rCurr;// index in left block
	int lCond;// column to join on
	int rCond;// column to join on
	int colN;

	public JoinIterator(IterableWithTable lI, IterableWithTable rI, String lCon, String rCon, Table t1, Table t2) {
		leftIt = lI;
		rightIt = rI;
		lCond = t1.colNames.get(lCon);
		rCond = t2.colNames.get(rCon);
		leftItor = leftIt.iterator();
		rightItor = rightIt.iterator();
		colN = t1.colNums + t2.colNums;
		tl = t1;
		tr = t2;
		lCurr = 0;
		rCurr = 0;
	}

	@Override
	public boolean hasNext() {
		if (leftItor.hasNext() == false) {
			System.err.println(toString() + "__FINISHED");
		}
		return leftItor.hasNext();
	}

	/**
	 * Preforms BLN join for one block from the right branch for each next call
	 */
	@Override
	public int[][] next() {
		int[][] result = new int[1024 * 4 / colN][];
//		System.out.println(tl.toString() + "__" + tr.toString());

		if (currentLeft == null) {
			currentLeft = leftItor.next();
			lCurr = 0;
		}

		return doJoin(result);

	}

	@Override
	public String toString() {
		return leftItor.toString() + "^" + rightItor.toString();
	}

	public int[][] doJoin(int[][] result) {
		int resIndex = 0;
		while (resIndex < result.length) {
			int[][] rightCur = rightItor.next();
			if (!rightItor.hasNext()) {
				if (leftItor.hasNext()) {
					rightItor = rightIt.iterator();
					currentLeft = leftItor.next();
					lCurr = 0;
					System.err.println("RESET:"+tr.name);
				}
				else return result;
			}
			for (int i = lCurr; i < currentLeft.length; i++) {
				int[] left = currentLeft[i];
//				if (resIndex >= result.length)
//					return result;
				lCurr = i;
				for (int j = rCurr; j < rightCur.length; j++) {
//					if (resIndex >= result.length)
//						return result;
					rCurr = j;
					int[] right = rightCur[j];
					if (left != null && right != null && left[lCond] == right[rCond]) {
						int[] tempAr = Arrays.copyOf(left, left.length + right.length);// check if this is efficient
						System.arraycopy(right, 0, tempAr, left.length, right.length);
//						Thank you Joachim Sauer on stackoverflow
						result[resIndex] = tempAr;
						resIndex++;
					}
				}
			}
			
		}
		
		return result;

	}

}
