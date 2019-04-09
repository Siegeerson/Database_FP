import java.util.Map;

public class SimplePred extends Pred {
	String pred;
	String colNum;
	int compVal;

	public SimplePred(String p, String cn, int v) {
		pred = p;
		compVal = v;
		colNum = cn;
	}
	public boolean eval(int[] row, Map<String,Integer> rowN) {
		int val1 = row[rowN.get(colNum)];
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
