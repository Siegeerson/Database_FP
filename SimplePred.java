
public class SimplePred extends Pred {
	String pred;
	int colNum;
	int compVal;

	public SimplePred(String p, int cn, int v) {
		pred = p;
		compVal = v;
		colNum = cn;
	}

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
