
public class OrPred extends Pred {
	
	Pred[] preds;
	public OrPred(Pred[] p) {
		// TODO Auto-generated constructor stub
		preds =p;
	}
	
	@Override
	public boolean eval(int[] row) {
		// TODO Auto-generated method stub
		for (Pred p : preds) {
			if(p.eval(row)) return true;
		}
		return false;
	}

}
