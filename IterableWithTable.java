import java.util.ArrayList;

/**
 * @author Benjamin Siege
 *	This class is important as there are numerous methods that might need to know data about the table below them
 */
public interface IterableWithTable extends Iterable<int[][]> {

	public Table getTable();
}
