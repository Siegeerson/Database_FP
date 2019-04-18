import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Table implements Comparable<Table>{
	String name;
	List<String> names;
	String fName;
	int colNums;
	int rowNum;
//	to find unique do max - min
//	int[] colMax;
//	int[] colMin;
//	int[] colUni;
	Map<String,Integer> colNames;
//	TODO:add metaData gathering
	public Table(String nameF, int colN,int rowN) {
		colNums = colN;
		name = nameF.substring(0,1);
		fName = "NOT STORED ON DISK";
		rowNum = rowN;
		colNames = new LinkedHashMap<String, Integer>(colN*2);
		names = new ArrayList<String>();
		//inital size is set this way to reduce hash resizing
		//uses a LinkedHashMap to retain key ordering for re-entry when joining
	}
	
	@Override
	public String toString() {
		return name +"_"+colNums+"_"+rowNum;
	}

	@Override
	public int compareTo(Table o) {
		if (o.rowNum>rowNum) {
			return -1;
		}else if (o.rowNum==rowNum) {
			return 0;
		}else {
			return 1;
		}
	}
}
