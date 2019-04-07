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

	public Table simplePred(Table table,SimplePred[] preds) throws IOException {
		String outputName = table.fileName+System.currentTimeMillis();
		DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(outputName)));
		File targetF = new File(table.fileName);
		FileInputStream fis = new FileInputStream(targetF);
		FileChannel fc = fis.getChannel();
		ByteBuffer bb = ByteBuffer.allocate(4*1024);
		ByteBuffer bb2 = ByteBuffer.allocate(4 * 1024);
		int hit = 0;
		while(fc.read(bb)!=-1) {
			bb.flip();
			while(bb.remaining()>=table.colNums*4) {
				int[] row = new int[table.colNums];
				for (int j = 0; j < row.length; j++) {
					row[j] = bb.getInt();
				}
				if(evalOrPreds(preds, row)) {
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
		return new Table(outputName, table.colNums);
	}
	
//	TODO:change to evaluate OR's and AND's the same like the calculator tree
	public boolean evalOrPreds(SimplePred[] preds,int[] row) {
		for (SimplePred sp : preds) {
			if(sp.eval(row)) return true;
		}
		return false;
	}
	

}
