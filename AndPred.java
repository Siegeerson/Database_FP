
public class AndPred extends Pred {
	Pred and1;
	Pred and2;
	
	public AndPred(Pred and1,Pred and2) {
		this.and1 = and1;
		this.and2 = and2;
	}
	@Override
	public boolean eval(int[] row) {
		return (and1.eval(row)&&and2.eval(row));
	}

}
