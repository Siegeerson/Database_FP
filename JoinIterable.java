import java.util.ArrayList;
import java.util.Iterator;

public class JoinIterable implements IterableWithTable {

	IterableWithTable leftIter;
	IterableWithTable rightIter;
	Table t1;
	Table t2;
	String condl;
	String condr;

	public JoinIterable(IterableWithTable l, IterableWithTable r, String cond1, String cond2) {
		leftIter = l;
		rightIter = r;
		t1 = l.getTable();
		t2 = r.getTable();
		condl = cond1;
		condr = cond2;
	}

	@Override
	public Iterator<ArrayList<int[]>> iterator() {
		return new JoinIterator(leftIter, rightIter, condl, condr, t1, t2);
	}

	public Table makeConTable(String outputName, int hit) {
		Table newT = new Table(outputName, t1.colNums + t2.colNums, hit);
		int i = 0;
//		puts correct col nums in hash 
		for (String k : t1.colNames.keySet()) {
			newT.colNames.put(k, i);
			i++;
		}
		for (String k : t2.colNames.keySet()) {
			newT.colNames.put(k, i);
			i++;
		}
		return newT;
	}

	@Override
	public Table getTable() {

		return makeConTable(t1.name + t2.name, t1.rowNum * t2.rowNum);// TODO:currently max rows possible, change to be
																		// better metaData later
	}

}