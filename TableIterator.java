import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.rmi.UnexpectedException;
import java.util.Iterator;


public class TableIterator implements Iterator<Integer[][]> {

	FileInputStream fis;
	ByteBuffer bb;
	ByteBuffer bb2;
	FileChannel fc;
	Table table;
	int rowsRead;
	public TableIterator(Table t) throws IOException {
		table =t;
		fis = new FileInputStream(new File(table.fileName));
		fc = fis.getChannel();
		rowsRead =0;
		bb = ByteBuffer.allocate(4*1024);
		bb2 = ByteBuffer.allocate(4*1024);
		fc.read(bb);
	}
	/**
	 * determines if all the rows have been read
	 */
	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return rowsRead!= table.rowNum;
	}

	/**
	 *returns a matrix of new values
	 */
	@Override
	public Integer[][] next() {
		if ((bb.remaining()>(4*table.colNums)-1)||resetBuffers()!=-1) {
			Integer[][] intAr = new Integer[bb.remaining()/(8*table.colNums)][table.colNums];
			System.out.println(bb.remaining()+"_"+(4*table.colNums));
			for (int i = 0; i < intAr.length; i++) {
				for (int j = 0; j < intAr[0].length; j++) {
					intAr[i][j]=bb.getInt();
				}
			}
			return intAr;
		}
		try {
			throw new UnexpectedException("UNEXPECTED END OF READ");//Stops program early, no dumb null pointers
		} catch (UnexpectedException e) {
			e.printStackTrace();
		}
		return null;//Should never reach this
	}
	/**
	 * @throws IOException
	 * Needed to prevent weird file reading errors when reseting table read
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
			int a =fc.read(bb);
			bb.flip();//TODO:Best place for this?
			return a;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
		
	}

}
