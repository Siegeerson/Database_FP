import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

public class LogicEngine {
	static int TABLESMADE = 0;
	
	
	
	

	/**
	 * @param table
	 * @param pred  - head of tree
	 * @return - result of simple predicates
	 * @throws IOException
	 */
	public Table simplePred(Table table, Pred pred) throws IOException {
		String outputName = table.fileName + "_" + TABLESMADE;
		TABLESMADE++;
		DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(outputName)));
		File targetF = new File(table.fileName);
		FileInputStream fis = new FileInputStream(targetF);
		FileChannel fc = fis.getChannel();
		ByteBuffer bb = ByteBuffer.allocate(4 * 1024);
		ByteBuffer bb2 = ByteBuffer.allocate(4 * 1024);
		int hit = 0;
		while (fc.read(bb) != -1) {
			bb.flip();
			while (bb.remaining() >= table.colNums * 4) {
				int[] row = new int[table.colNums];
				for (int j = 0; j < row.length; j++) {
					row[j] = bb.getInt();
				}
				if (pred.eval(row)) {
					hit++;
					System.out.println(Arrays.toString(row));
					for (int j = 0; j < row.length; j++) {
						dos.writeInt(row[j]);
					}
				}
			}
			bb2.clear();
			bb2.put(bb);
			ByteBuffer tempB = bb;
			bb = bb2;
			bb2 = tempB;
		}
		fc.close();
		fis.close();
		dos.close();
		Table output = new Table(outputName, table.colNums);
		output.colNames = table.colNames; //since no changes are made to col structure just reuse old map
		return output;
	}

}
