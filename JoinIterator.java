import java.util.ArrayList;
import java.util.Iterator;

public class JoinIterator implements Iterator<ArrayList<int[]>>{

	IterableWithTable leftIt;
	IterableWithTable rightIt;
	Iterator<ArrayList<int[]>> leftItor;
	Iterator<ArrayList<int[]>> rightItor;
	
	int lCond;
	int rCond;
	public JoinIterator(IterableWithTable lI,IterableWithTable rI,String lCon,String rCon,Table t1, Table t2) {
		leftIt = lI;
		rightIt = rI;
		lCond = t1.colNames.get(lCon);
		rCond = t2.colNames.get(rCon);
		leftItor = leftIt.iterator();
		rightItor = rightIt.iterator();
	}
	@Override
	public boolean hasNext() {
		return leftItor.hasNext();
	}

	@Override
	public ArrayList<int[]> next() {
		ArrayList<int[]> result = new ArrayList<>();
//		TODO:always call has next before next
		if (!rightItor.hasNext()) {
			rightItor= rightIt.iterator();
		}
		ArrayList<int[]> 
	}

	
}
