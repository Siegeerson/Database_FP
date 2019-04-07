
public class Table {
	String fileName;
	int colNums;
//	int[] colMax;
//	int[] colMin;
//	int[] colUni;
	public Table(String name, int colN) {
		colNums = colN;
		fileName = name;
	}
	
	@Override
	public String toString() {
		return fileName +", "+colNums;
	}
}
