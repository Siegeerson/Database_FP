import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.omg.CORBA.LongLongSeqHelper;

public class JoinEngine {
//	TODO:How to designate names for each row
	public Table blnJoin(Table t1, Table t2, String tar1, String tar2) throws IOException {
		String outputName = t1.fileName + t2.fileName;
		DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(outputName)));
		File targetF = new File(t1.fileName);
		FileInputStream fis = new FileInputStream(targetF);
		FileChannel fc = fis.getChannel();
		File targetF2 = new File(t2.fileName);
		FileInputStream fis2 = new FileInputStream(targetF2);
		FileChannel fc2 = fis2.getChannel();
//		Allocate two buffers for each table
		int colTarg1 = t1.colNames.get(tar1);
		int colTarg2 = t2.colNames.get(tar2);
		
		ByteBuffer bb = ByteBuffer.allocate(4*1024);//TODO:get help on size allocation
		ByteBuffer bb2 = ByteBuffer.allocate(4*1024);
		ByteBuffer b2b = ByteBuffer.allocate(4*1024);//TODO:get help on size allocation
		ByteBuffer b2b2 = ByteBuffer.allocate(4*1024);
//		System.out.println("START LOOPING");
		while(fc.read(bb)!=-1) {
			bb.flip();
			List<int[]> bufferedRows = new ArrayList<>(); //make a list of all arrays put in the buffer
			while(bb.remaining()>4*t1.colNums) {
				int[] tempAr = new int[t1.colNums];
				for (int i = 0; i < tempAr.length; i++) {
					tempAr[i]=bb.getInt();
				}
				bufferedRows.add(tempAr);
			}
			while(fc2.read(b2b)!=-1) {
//				System.out.println("START INNER");
				b2b.flip();
				while(b2b.remaining()>4*t2.colNums) {
					int[] tempAr = new int[t2.colNums];
					for (int i = 0; i < tempAr.length; i++) {
						tempAr[i]=b2b.getInt();
					}
					for (int[] iA: bufferedRows) {
						if(iA[colTarg1]==tempAr[colTarg2]) {
//							System.out.println("START WRITE");
//							Write both rows			
							for (int i : iA) {
								dos.writeInt(i);
							}
							for (int i : tempAr) {
								dos.writeInt(i);
							}
						}
					}
				}
//				Reset inner buffer
//				System.out.println("RESET_IN");
				b2b2.clear();
				b2b2.put(b2b);
				ByteBuffer tempB = b2b;
				b2b = b2b2;
				b2b2 = tempB;
			}
//			Reset outer buffer
//			System.out.println("RESET_OUT");
			bb2.clear();
			bb2.put(bb);
			ByteBuffer tempB = bb;
			bb = bb2;
			bb2 = tempB;
//			reset read from inner file
			fc2.close();
			fis2.close();
			fis2 = new FileInputStream(targetF2);
			fc2 = fis2.getChannel();
		}
		fc2.close();
		fis2.close();
		fc.close();
		fis.close();
		dos.close();
		return makeConTable(outputName, t1, t2);
	}
	
	public Table makeConTable(String outputName,Table t1,Table t2) {
		Table newT = new Table(outputName, t1.colNums+t2.colNums);
		int i = 0;
//		puts correct col nums in hash 
		for (String k : t1.colNames.keySet()) {
			newT.colNames.put(k, i);
			i++;
		}
		for (String k : t2.colNames.keySet()) {
			newT.colNames.put(k, i);
			i++;
		}
		return newT;
	}
	
	
	
}
