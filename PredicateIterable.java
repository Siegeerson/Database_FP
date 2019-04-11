import java.util.ArrayList;
import java.util.Iterator;

public class PredicateIterable implements IterableWithTable {

//	As currently constructed is to be run after all joins
//	Because of BLN predicates would have to be re-done each time right table is reset
	IterableWithTable iter;
	Pred p;
	public PredicateIterable(IterableWithTable it,Pred tablePred) {
		p = tablePred;
		iter = it;
	}
	@Override
	public Iterator<ArrayList<int[]>> iterator() {
		return new PredicateIterator(iter, p);
	}

	@Override
	public Table getTable() {

		return iter.getTable();
	}

}
