import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.Iterator;

public class TableIterator implements Iterator<int[][]> {

	FileInputStream fis;
	MappedByteBuffer bb;
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
		bb.load();
		
//		fc.read(bb);
//		bb.flip();//DON'T FORGET THIS
	}

	/**
	 * determines if all the rows have been read
	 */
	@Override
	public boolean hasNext() {
<<<<<<< HEAD
		System.out.println((table.rowNum-rowsRead)/(double)table.rowNum+"_"+table.name);
		return rowsRead != table.rowNum;
=======
//		System.err.println((rowsRead)/(double)table.rowNum+"_"+table.names);
		if (rowsRead != table.rowNum)
			return true;
		else {
//			System.err.println("END OF TABLE:"+table.name+"_"+rowsRead);
			return false;
		}
>>>>>>> 7e965cf7327ad885ddd5cc308398654e98a23361
	}

	/**
	 * returns a matrix of new values
	 */
	@Override
	public int[][] next() {
//		System.err.println("READING TABLE:"+table.name);
		if (bb.hasRemaining()) {
<<<<<<< HEAD
			bb.load();
			ArrayList<int[]> intAr = new ArrayList<>();// TODO:pick initial capacity,either method or constant
//			System.out.println(bb.remaining()+"_"+(4*table.colNums));
			for (int i = 0; i < 4*1024; i++) {
				if (bb.hasRemaining()) {
					int[] tempAr = new int[table.colNums];
					for (int j = 0; j < table.colNums; j++) {
						tempAr[j] = bb.getInt();
					}
					rowsRead++;
					intAr.add(tempAr);
				}else {
					break;//bad practice i know
=======
			int[][] result = new int[1024 * 80/table.colNums][];// size is equal to 1024 *4 /column numbers
//			System.out.println(bb.remaining()+"_"+(4*table.colNums));
			for (int i = 0; i < result.length; i++) {
				if (rowsRead == table.rowNum)
					break;
				int[] row = new int[table.colNums];
				for (int j = 0; j < row.length; j++) {
					row[j] = bb.getInt();
>>>>>>> 7e965cf7327ad885ddd5cc308398654e98a23361
				}
				rowsRead++;
				result[i] = row;// add to block
			}
//			System.out.println(rowsRead);
			return result;
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

	@Override
	public String toString() {
		return table.toString();
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
