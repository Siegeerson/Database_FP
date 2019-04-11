import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.Iterator;

public class TableIterator implements Iterator<ArrayList<int[]>> {

	FileInputStream fis;
	ByteBuffer bb;
	FileChannel fc;
	Table table;
	int rowsRead;

	public TableIterator(Table t) throws IOException {
		table = t;
		File tempF = new File(table.fName);
		fis = new FileInputStream(tempF);
		fc = fis.getChannel();
		rowsRead = 0;
		bb = fc.map(MapMode.READ_ONLY, 0, tempF.length());
//		fc.read(bb);
//		bb.flip();//DON'T FORGET THIS
	}

	/**
	 * determines if all the rows have been read
	 */
	@Override
	public boolean hasNext() {
		return rowsRead != table.rowNum;
	}

	/**
	 * returns a matrix of new values
	 */
	@Override
	public ArrayList<int[]> next() {
		if (bb.hasRemaining()) {
			ArrayList<int[]> intAr = new ArrayList<>();// TODO:pick initial capacity,either method or constant
//			System.out.println(bb.remaining()+"_"+(4*table.colNums));
			for (int i = 0; i < (4 * 1024) / table.colNums; i++) {
				if (bb.hasRemaining()) {
					int[] tempAr = new int[table.colNums];
					for (int j = 0; j < table.colNums; j++) {
						tempAr[j] = bb.getInt();
					}
					rowsRead++;
					intAr.add(tempAr);
				}else {
					break;//bad practice i know
				}
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

//	private int resetBuffers() {
//		bb2.clear();
//		bb2.put(bb);
//		ByteBuffer tempB = bb;
//		bb = bb2;
//		bb2 = tempB;
//		try {
//			int a = fc.read(bb);
//			bb.flip();// TODO:Best place for this?
//			return a;
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return -1;
//
//	}

}
