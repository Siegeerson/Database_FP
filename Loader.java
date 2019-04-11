import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

public class Loader {
	public Map<String, Table> loadAll(String input) throws IOException {
		Map<String, Table> result = new HashMap<>();
		String[] toLoad = input.split(",");
		for (String inS : toLoad) {
			Table inST = LoadFile(inS);
			result.put(inST.name, inST);
		}

		return result;
	}

//	TODO:Change to external sort on each different column
//	TODO:Gather more metaData
//	TODO:This method might be able to be improved
	public Table LoadFile(String path) throws IOException {
		File input = new File(path);
		String tableName =  getName(path);
		FileReader fr = new FileReader(input);
		CharBuffer cb1 = CharBuffer.allocate(4 * 1024);
		CharBuffer cb2 = CharBuffer.allocate(4 * 1024);
		DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(tableName)));
		int colNum = 0;
		int rowNum = 0;
		int colTrack =0;
		boolean colNumF = false;
		while (fr.read(cb1) != -1) {
			cb1.flip();
			int numS = 0;
			for (int i = 0; i < cb1.length(); i++) {
				char selChar = cb1.charAt(i);
				if (selChar == ',' || selChar == '\n') {
//					Integer tempI= Integer.parseInt(cb1.subSequence(numS, i).toString(),10);
					dos.writeInt((Integer.parseInt(cb1.subSequence(numS, i).toString(), 10))); // TODO:Best Way?
					numS = i + 1;
//					Find number of columns
					colTrack++;
					if (selChar == '\n') {
						rowNum++;
						if (!colNumF) {
							colNumF = true;
							colNum=colTrack;
						}
					}
				}
			}
//			take everything left over from cb1 into cb2
			cb1.position(numS);
			cb2.clear();
			cb2.put(cb1);
			CharBuffer temp = cb1;
			cb1 = cb2;
			cb2 = temp;
		}
		dos.close();
		fr.close();
		Table output = new Table(tableName, colNum,rowNum);
		output.fName = tableName;
		for (int i = 0; i < output.colNums; i++) {
			output.colNames.put(output.name+i, i);
		}
		return output;
	}

	public void testRead(Table table) throws IOException {
		File targetF = new File(table.fName);
		FileInputStream fis = new FileInputStream(targetF);
		FileChannel fc = fis.getChannel();
		ByteBuffer bb = ByteBuffer.allocate(4 * 1024);
		ByteBuffer bb2 = ByteBuffer.allocate(4 * 1024);
		int i = 0;
		while (fc.read(bb) != -1) {
			bb.flip();
			while (bb.remaining()>3) {
				System.out.printf("%-10d",bb.get());
				i++;
				if (i % table.colNums == 0) {
					System.out.println();
				}
			}
			bb2.clear();
			bb2.put(bb);
			ByteBuffer tempB = bb;
			bb = bb2;
			bb2 = tempB;
			System.out.println(i/table.colNums);
		}
		fc.close();
		fis.close();
	}
	public String getName(String f) {
		String[] fullP = f.split("/");
		String name = fullP[2].charAt(0)+fullP[1];
		return name;
		
	}
}
