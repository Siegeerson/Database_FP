
public class SimplePred extends Pred {
	String pred;
	int colNum;
	int compVal;

	public SimplePred(String p, int cn, int v) {
		pred = p;
		compVal = v;
		colNum = cn;
	}
//	Assumes that simple predicates will performed first
//	TODO: ensure that either this assumption is not invalidated or change to use map
	public boolean eval(int[] row) {
		int val1 = row[colNum];
		switch (pred) {
		case ">":
			return val1 > compVal;
		case "<":
			return val1 < compVal;
		case "=":
			return val1 == compVal;
		}
		return false;
	}

}
