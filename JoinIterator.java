import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class JoinIterator implements Iterator<ArrayList<int[]>> {

	IterableWithTable leftIt;
	IterableWithTable rightIt;
	Iterator<ArrayList<int[]>> leftItor;
	Iterator<ArrayList<int[]>> rightItor;
	ArrayList<int[]> currentLeft;
	Table tl;
	Table tr;
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
		tl = t1;
		tr = t2;
	}

	@Override
	public boolean hasNext() {
		if(leftItor.hasNext()==false) {
			System.err.println(toString()+"__FINISHED");
		}
		return leftItor.hasNext();
	}

	/**
	 * Preforms BLN join for one block from the right branch for each next call
	 */
	@Override
	public ArrayList<int[]> next() {
		ArrayList<int[]> result = new ArrayList<>();
//		System.out.println(tl.toString() + "__" + tr.toString());

		if (currentLeft == null) {
			currentLeft = leftItor.next();
		}
//		TODO:always call has next before next
		if (!rightItor.hasNext()) {
			rightItor = rightIt.iterator();
			currentLeft = leftItor.next();
		}
		
		return doJoin(result);

	}
	@Override
	public String toString() {
		return leftItor.toString()+"^"+rightItor.toString();
	}
	public ArrayList<int[]> doJoin(ArrayList<int[]> result){
		ArrayList<int[]> rightRow = rightItor.next();
//		System.out.println(rightRow.size());
		Iterator<int[]> leftIt = currentLeft.iterator();
		while(leftIt.hasNext()) {
			int[] left = leftIt.next();
			Iterator<int[]> rightIt = rightRow.iterator();
			while(rightIt.hasNext()) {
				int[] right = rightIt.next();
				if (left[lCond] == right[rCond]) {
					int[] tempAr = Arrays.copyOf(left, left.length + right.length);// check if this is efficient
					System.arraycopy(right, 0, tempAr, left.length, right.length);
//					// Thank you Joachim Sauer on stackoverflow
					result.add(tempAr);
//					int[] tempAr = new int[tl.colNums+tr.colNums];
//					int i = 0;
//					for (int j : left) {
//						tempAr[i]=j;
//						i++;
//					}
//					for (int js : right) {
//						tempAr[i]=js;
//						i++;
//					}
//					result.add(tempAr);
				}
			}
		}
		return result;
	}
}
