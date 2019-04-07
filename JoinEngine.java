
public class JoinEngine {

	public Table blnJoin(Table t1, Table t2, int t1Col, int t2Col) {
		String name = t1.fileName+"."+t1Col+"_"+t2.fileName+"."+t2Col;
		
		
		
		
		
		
		return new Table(name, t1.colNums+t2.colNums);
	}
}
