import java.util.*;
/**
 * 
 * @author Simona Andreea Badoiu
 *
 */
public class ContentDir {
	//continutul directorului se retine intr-un map
	Map<String,AFisier> c = new TreeMap<String,AFisier>();
	
	@Override
	public String toString() {
		String str="";
		for (Map.Entry<String, AFisier> entry : c.entrySet())
			str += entry.getValue().name + "\n";
		if (str != "")
		return str.substring(0, str.length()-1);
		return "";
	}
}
