import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class IOUtils {
	public static ArrayList<ArrayList<GridCell>> readFromFile(String fileName) {
		ArrayList<ArrayList<GridCell>> grid = new ArrayList<>();
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		try {
			fileReader = new FileReader(fileName);
			bufferedReader = new BufferedReader(fileReader);
			int xCoord = 0; 
			int yCoord = 0;
			String line = bufferedReader.readLine();
			
			while (line != null) {
				ArrayList<GridCell> gridLine = new ArrayList<>();
				char[] linechars = line.toCharArray();
				for (char c : linechars) {
					if (c == '*') {
						gridLine.add(new EmptyCell(xCoord, yCoord));
					}
					else if (c == 's') {
						gridLine.add(new SwampCell(xCoord, yCoord));
					}
					else if (c == 'w') {
						gridLine.add(new WallCell(xCoord, yCoord));
					}
					++yCoord;
				}
				grid.add(gridLine);
				++xCoord;
				yCoord = 0;
				line = bufferedReader.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return grid;
	}
}
