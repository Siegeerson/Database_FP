import java.util.Map;

public class AndPred extends Pred {
	Pred and1;
	Pred and2;
	
	public AndPred(Pred and1,Pred and2) {
		this.and1 = and1;
		this.and2 = and2;
	}
	@Override
	public boolean eval(int[] row,Map<String,Integer> rowN) {
		return (and1.eval(row,rowN)&&and2.eval(row,rowN));
	}

}
