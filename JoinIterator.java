import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class JoinIterator implements Iterator<int[][]> {

	IterableWithTable leftIt;
	IterableWithTable rightIt;
	Iterator<int[][]> leftItor;
	Iterator<int[][]> rightItor;
	int[][] currentLeft;
	int[][] currentRight;
	boolean allNUll;
	int rowsIt;
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
		allNUll = true;
		rowsIt = 0;
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
		boolean hasNext = leftItor.hasNext();
		if (hasNext == false) {
			System.err.println(toString() + "__FINISHED__" + allNUll + "_" + rowsIt);
		}
		return hasNext;
	}

	/**
	 * Preforms BLN join for one block from the right branch for each next call
	 */
	@Override
	public int[][] next() {
		int[][] result = new int[1024 / colN][];
//		System.out.println(tl.toString() + "__" + tr.toString());

		if (currentLeft == null) {
			currentLeft = leftItor.next();
			lCurr = 0;
		}
		if (currentRight == null) {
			currentRight = rightItor.next();
			rCurr = 0;
		}

		return doJoin(result);

	}

	@Override
	public String toString() {
		return leftItor.toString() + "=" + lCond + "^" + rightItor.toString() + "=" + rCond;
	}

	public int[][] doJoin(int[][] result) {
		int resIndex = 0;
		while (resIndex < result.length) {
			if (lCurr == currentLeft.length) {
				if (!rightItor.hasNext()) {
					if (leftItor.hasNext()) {
						System.err.println("RESET: " + rightItor.toString());
						rightItor = rightIt.iterator();
						currentLeft = leftItor.next();
						lCurr = 0;
					} else
						return result;
				}
				currentRight = rightItor.next();
				rCurr = 0;
			}

			if (lCurr == currentLeft.length&&currentRight.length-1==rCurr) {
				lCurr = 0;
			}
			while (lCurr < currentLeft.length && resIndex < result.length) {
				if (rCurr == currentRight.length) {
					rCurr = 0;
				}
				int[] left = currentLeft[lCurr];
				while (rCurr < currentRight.length && resIndex < result.length) {

					if (resIndex >= result.length)
						return result;
					int[] right = currentRight[rCurr];
//					if(right==null) break;
					if (left != null && right != null) {
						if (left[lCond] == right[rCond]) {
							rowsIt++;
							allNUll = false;
//						int[] tempAr = Arrays.copyOf(left, left.length + right.length);// check if this is efficient
//						System.arraycopy(right, 0, tempAr, left.length, right.length);
//						Thank you Joachim Sauer on stackoverflow
							int[] tempAr = new int[tl.colNums + tr.colNums];
							int k = 0;
							for (int b : left) {
								tempAr[k] = b;
								k++;
							}
							for (int js : right) {
								tempAr[k] = js;
								k++;
							}
							result[resIndex] = tempAr;
							resIndex++;
						}
					}
					rCurr++;
				}
				if(rCurr==currentRight.length) System.out.println("STOP");
				lCurr++;
			}

		}

		return result;

	}

}
