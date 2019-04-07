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
	public Map<String,Table> loadAll(String input) throws IOException{
		Map<String,Table> result = new HashMap<>();
		String[] toLoad = input.split(",");
		for (String inS : toLoad) {
			Table inST = LoadFile(inS);
			result.put(inST.fileName, inST);
		}
		
		return result;
	}
	
	
	public Table LoadFile(String path) throws IOException {
		File input = new File(path);
		String outputName = input.getName().substring(0, 1);
		FileReader fr = new FileReader(input);
		CharBuffer cb1 = CharBuffer.allocate(4 * 1024);
		CharBuffer cb2 = CharBuffer.allocate(4 * 1024);
		DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(outputName)));
		int colNum = 0;
		boolean colNumF = false;
		while(fr.read(cb1)!=-1) {
			cb1.flip();
			int numS = 0;
			for (int i = 0; i < cb1.length(); i++) {
				char selChar = cb1.charAt(i);
				if (selChar== ',' || selChar =='\n' ) {
//					Integer tempI= Integer.parseInt(cb1.subSequence(numS, i).toString(),10);
					dos.writeInt((Integer.parseInt(cb1.subSequence(numS, i).toString(),10))); //TODO:Best Way?
					numS = i+1;
//					Find number of columns
					if (!colNumF) {
						colNum++;
						if (selChar=='\n') {
							colNumF=true;
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
		return new Table(outputName, colNum);
	}
	
	public void testRead(Table table) throws IOException {
		File targetF = new File(table.fileName);
		FileInputStream fis = new FileInputStream(targetF);
		FileChannel fc = fis.getChannel();
		ByteBuffer bb = ByteBuffer.allocate(4*1024);
		ByteBuffer bb2 = ByteBuffer.allocate(4 * 1024);
		int i =0;
		while(fc.read(bb)!=-1) {
			bb.flip();
			while(bb.hasRemaining()) {
				System.out.print(bb.getInt()+", ");
				i++;
				if(i%table.colNums==0) {
					System.out.println();
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
	}
}