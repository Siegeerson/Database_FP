
public class OrPred extends Pred {
	
	Pred pred1;
	Pred pred2;
	public OrPred(Pred p,Pred p2) {
		// TODO Auto-generated constructor stub
		pred1 =p;
		pred2 =p2;
	}
	
	@Override
	public boolean eval(int[] row) {
		// TODO Auto-generated method stub
		return pred1.eval(row)||pred2.eval(row);
	}

}