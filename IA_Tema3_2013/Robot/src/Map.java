import java.util.ArrayList;

public class Map {
	public ArrayList<ArrayList<GridCell>> 	map;
	
	public Map() {
		map = IOUtils.readFromFile(Constants.FILE_NAME);
	}
	
	public GridCell getCell(int xCoord, int yCoord) {
		return map.get(xCoord).get(yCoord);
	}
	
	public int getMapWidth() {
		return map.get(0).size();
	}
	
	public int getMapHeight() {
		return map.size();
	}
	
	public GridCell nextCell(GridCell currentCell) {
		// if the smell sensor feels swamp smell then the robot won't move to the next cell
		// because he doesn't know which of the next cells is a swamp
		if (smells(currentCell)) {
			return null;
		}
		
		GridCell next = null;
		
		// try to go north
		next = goNorth(currentCell);
		if (next != null) {
			return next;
		}
		
		//try to go south
		next = goSouth(currentCell);
		if (next != null) {
			return next;
		}
		
		// try to go east
		next = goEast(currentCell);
		if (next != null) {
			return next;
		}
		
		//try to go west
		next = goWest(currentCell);
		if (next != null) {
			return next;
		}
		
		return null;
	}
	
	public int availableCells(GridCell currentCell) {
		// if the smell sensor feels swamp smell then the robot won't move to the next cell
		// because he doesn't know which of the next cells is a swamp
		int totalAvailable = 0;
		if (smells(currentCell)) {
			return totalAvailable;
		}
		
		GridCell next = null;
		
		// try to go north
		next = goNorth(currentCell);
		if (next != null) {
			totalAvailable++;
		}
		
		//try to go south
		next = goSouth(currentCell);
		if (next != null) {
			totalAvailable++;
		}
		
		// try to go east
		next = goEast(currentCell);
		if (next != null) {
			totalAvailable++;
		}
		
		//try to go west
		next = goWest(currentCell);
		if (next != null) {
			totalAvailable++;
		}
		
		return totalAvailable;
	}
	
	public GridCell goNorth(GridCell currentCell) {
		GridCell northCell = getNorthCell(currentCell);
		
		if (northCell == null)
			return null;
		
		if (northCell.canBeVisited()) {
			return northCell;
		}
		
		return null;
	}
	
	public GridCell goSouth(GridCell currentCell) {
		GridCell southCell = getSouthCell(currentCell);
		
		if (southCell == null)
			return null;

		if (southCell.canBeVisited()) {
			return southCell;
		}
		return null;
	}
	
	public GridCell goEast(GridCell currentCell) {
		GridCell eastCell = getEastCell(currentCell);
		
		if (eastCell == null)
			return null;

		if (eastCell.canBeVisited()) {
			return eastCell;
		}
		return null;
	}
	
	public GridCell goWest(GridCell currentCell) {
		GridCell westCell = getWestCell(currentCell);

		if (westCell == null)
			return null;
		
		if (westCell.canBeVisited()) {
			return westCell;
		}
		return null;
	}
	
	public boolean smells(GridCell currentCell) {
		GridCell northCell 	= getNorthCell(currentCell);
		GridCell southCell 	= getSouthCell(currentCell);
		GridCell eastCell	= getEastCell(currentCell);
		GridCell westCell	= getWestCell(currentCell);
		
		return (northCell != null && northCell.smells()) || 
				(southCell != null && southCell.smells()) || 
				(eastCell != null && eastCell.smells()) || 
				(westCell != null && westCell.smells());
	}
	
	public GridCell getNorthCell(GridCell currentCell) {
		int xCoord = currentCell.getXCoord();
		int yCoord = currentCell.getYCoord();
		
		int northXCoord = xCoord - 1;
		int northYCoord = yCoord;
		
		if (northXCoord < 0)
			return null;
		
		return map.get(northXCoord).get(northYCoord);
	}
	
	public GridCell getSouthCell(GridCell currentCell) {
		int xCoord = currentCell.getXCoord();
		int yCoord = currentCell.getYCoord();
		
		int southXCoord = xCoord + 1;
		int southYCoord = yCoord;
		
		if (southXCoord >= getMapHeight())
			return null;
		
		return map.get(southXCoord).get(southYCoord);
	}
	
	public GridCell getEastCell(GridCell currentCell) {
		int xCoord = currentCell.getXCoord();
		int yCoord = currentCell.getYCoord();
		
		int eastXCoord = xCoord;
		int eastYCoord = yCoord + 1;
		
		if (eastYCoord >= getMapWidth())
			return null;
		
		return map.get(eastXCoord).get(eastYCoord);
	}
	
	public GridCell getWestCell(GridCell currentCell) {
		int xCoord = currentCell.getXCoord();
		int yCoord = currentCell.getYCoord();
		
		int westXCoord = xCoord;
		int westYCoord = yCoord - 1;
		
		if (westYCoord < 0)
			return null;
		
		return map.get(westXCoord).get(westYCoord);
	}
}


















