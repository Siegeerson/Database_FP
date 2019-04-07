
public class OrPred extends Pred {
	
	Pred pred1;
	Pred pred2;
	public OrPred(Pred p,Pred p2) {
		pred1 =p;
		pred2 =p2;
	}
	
	@Override
	public boolean eval(int[] row) {
		return pred1.eval(row)||pred2.eval(row);
	}

}
