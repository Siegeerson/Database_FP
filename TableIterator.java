import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.Iterator;

public class TableIterator implements Iterator<ArrayList<int[]>> {

	FileInputStream fis;
	ByteBuffer bb;
	ByteBuffer bb2;
	FileChannel fc;
	Table table;
	int rowsRead;

	public TableIterator(Table t) throws IOException {
		table = t;
		fis = new FileInputStream(new File(table.fName));
		fc = fis.getChannel();
		rowsRead = 0;
		bb = ByteBuffer.allocate(4 * 1024);
		bb2 = ByteBuffer.allocate(4 * 1024);
		fc.read(bb);
		bb.flip();//DON'T FORGET THIS
	}

	/**
	 * determines if all the rows have been read
	 */
	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return rowsRead != table.rowNum;
	}

	/**
	 * returns a matrix of new values
	 */
	@Override
	public ArrayList<int[]> next() {
		if ((bb.remaining() > (4 * table.colNums) - 1) || resetBuffers() != -1) {
			ArrayList<int[]> intAr = new ArrayList<>();// TODO:pick initial capacity,either method or constant
//			System.out.println(bb.remaining()+"_"+(4*table.colNums));
			while (bb.remaining() > (4 * table.colNums) - 1) {
				int[] tempAr = new int[table.colNums];
				for (int j = 0; j < table.colNums; j++) {
					tempAr[j] = bb.getInt();
				}
				rowsRead++;
				intAr.add(tempAr);
				
			}
			return intAr;
		}
		try {
			throw new UnexpectedException("UNEXPECTED END OF READ");// Stops program early, no dumb null pointers
		} catch (UnexpectedException e) {
			e.printStackTrace();
		}
		return null;// Should never reach this
	}

	/**
	 * @throws IOException Needed to prevent weird file reading errors when reseting
	 *                     table read
	 */
	public void close() throws IOException {
		fc.close();
		fis.close();
	}

	private int resetBuffers() {
		bb2.clear();
		bb2.put(bb);
		ByteBuffer tempB = bb;
		bb = bb2;
		bb2 = tempB;
		try {
			int a = fc.read(bb);
			bb.flip();// TODO:Best place for this?
			return a;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;

	}

}
