import java.util.ArrayList;

public interface IterableWithTable extends Iterable<ArrayList<int[]>> {

	public Table getTable();
}
