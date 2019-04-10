import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Table {
	String fileName;
	String name;
	int colNums;
	int rowNum;
//	int[] colMax;
//	int[] colMin;
//	int[] colUni;
	Map<String,Integer> colNames;
//	TODO:add metaData gathering
	public Table(String nameF, int colN,int rowN) {
		colNums = colN;
		fileName = nameF;
		name = nameF.substring(0,1);
		rowNum = rowN;
		colNames = new LinkedHashMap<String, Integer>(colN*2);
		//inital size is set this way to reduce hash resizing
		//uses a LinkedHashMap to retain key ordering for re-entry when joining
	}
	
	@Override
	public String toString() {
		return fileName +", "+colNums+", "+rowNum;
	}
}
