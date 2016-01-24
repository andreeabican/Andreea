import java.util.ArrayList;
import java.util.LinkedList;


public class Robot {
	public LinkedList<GridCell> totalPath = new LinkedList<>();
	public LinkedList<GridCell> currentPath = new LinkedList<>();
	public GridCell				currentCell;
	Map							map;
	GridCell[][] 				robotMap;
	
	public Robot(int xCoord, int yCoord) {
		map = new Map();
		int mapWidth = map.getMapWidth();
		int mapHeight = map.getMapHeight();
		robotMap = new GridCell[mapWidth][mapHeight];
		initRobotMap();
		currentCell = map.getCell(xCoord, yCoord); 
		currentCell.visit();
		robotMap[xCoord][yCoord] = new SecureCell(xCoord, yCoord);
		currentPath.push(currentCell);
	}
	
	public void discoverMap() {
		currentCell = map.nextCell(currentCell);
		
		while (currentCell != null) {
			currentCell.visit();
			int xCoord = currentCell.getXCoord();
			int yCoord = currentCell.getYCoord();
			if (currentCell instanceof WallCell) {
				robotMap[xCoord][yCoord] = currentCell;
			}
			else if (map.smells(currentCell)) {
				robotMap[xCoord][yCoord] = new SecureCell(xCoord, yCoord);
				setUnsecureNeighbours(currentCell);
			}
			else {
				robotMap[xCoord][yCoord] = new SecureCell(xCoord, yCoord);
			}
			
			if (currentCell instanceof EmptyCell) {
				currentPath.addLast(currentCell);
				totalPath.addLast(currentCell);
				currentCell = map.nextCell(currentCell);
			}
			else {
				currentCell = null;
			}
		}
		
		if (!currentPath.isEmpty()) {
			currentCell = currentPath.pollLast();
			if (map.availableCells(currentCell) > 1 /*map.nextCell(currentCell) != null*/) {
				currentPath.addLast(currentCell);
				totalPath.add(currentCell);
				discoverMap();
			} else {
				discoverMap();
			}
		} else {
			System.out.println("Robot Map:");
			findSwamps();
			printRobotMap();
		}
	}
	
	public void setUnsecureNeighbours(GridCell currentCell) {
		GridCell gridCell;
		int xCoord;
		int yCoord;
		
		gridCell = getNorthCell(currentCell);
		if (gridCell instanceof DummyCell) {
			xCoord = gridCell.getXCoord();
			yCoord = gridCell.getYCoord();
			
			robotMap[xCoord][yCoord] = new UnsecureCell(xCoord, yCoord);
		}
		
		gridCell = getSouthCell(currentCell);
		if (gridCell instanceof DummyCell) {
			xCoord = gridCell.getXCoord();
			yCoord = gridCell.getYCoord();
			
			robotMap[xCoord][yCoord] = new UnsecureCell(xCoord, yCoord);
		}
		
		gridCell = getEastCell(currentCell);
		if (gridCell instanceof DummyCell) {
			xCoord = gridCell.getXCoord();
			yCoord = gridCell.getYCoord();
			
			robotMap[xCoord][yCoord] = new UnsecureCell(xCoord, yCoord);
		}
		
		gridCell = getWestCell(currentCell);
		if (gridCell instanceof DummyCell) {
			xCoord = gridCell.getXCoord();
			yCoord = gridCell.getYCoord();
			
			robotMap[xCoord][yCoord] = new UnsecureCell(xCoord, yCoord);
		}
	}
	
	public void findSwamps() {
		for (GridCell gc : totalPath) {
			if (map.smells(gc)) {
				GridCell northCell = getNorthCell(gc);
				GridCell southCell = getSouthCell(gc);
				GridCell eastCell = getSouthCell(gc);
				GridCell westCell = getWestCell(gc);
				
				int secureCells = 0;
				GridCell swampCell = null;
				
				if (!(northCell instanceof UnsecureCell) && !(northCell instanceof DummyCell) && !(northCell instanceof SwampCell)) {
					secureCells++;
				} else {
					swampCell = northCell; 
				}
				if (!(southCell instanceof UnsecureCell) && !(southCell instanceof DummyCell) && !(southCell instanceof SwampCell)) {
					secureCells++;
				} else {
					swampCell = southCell;
				}
				if (!(eastCell instanceof UnsecureCell) && !(eastCell instanceof DummyCell) && !(eastCell instanceof SwampCell)) {
					secureCells++;
				} else {
					swampCell = eastCell;
				}
				
				if (!(westCell instanceof UnsecureCell) && !(westCell instanceof DummyCell) && !(westCell instanceof SwampCell)) {
					secureCells++;
				} else {
					swampCell = westCell;
				}
				
				if (secureCells == 3) {
					int xCoord = swampCell.getXCoord();
					int yCoord = swampCell.getYCoord();
					robotMap[xCoord][yCoord] = new SwampCell(xCoord, yCoord);
				}
			}
		}
	}
	
	public void updateNeighbours(GridCell currentCell) {
		GridCell northCell = getNorthCell(currentCell);
		GridCell southCell = getSouthCell(currentCell);
		GridCell eastCell = getSouthCell(currentCell);
		GridCell westCell = getWestCell(currentCell);
		
		GridCell swampCell = null;
		
		int knownCells = 0;
		if (northCell instanceof WallCell || northCell instanceof SecureCell) {
			knownCells++;
		} else {
			swampCell = northCell;
		}
		if (southCell instanceof WallCell || southCell instanceof SecureCell) {
			knownCells++;
		} else {
			swampCell = southCell;
		}
		if (eastCell instanceof WallCell || eastCell instanceof SecureCell) {
			knownCells++;
		} else {
			swampCell = eastCell;
		}
		if (westCell instanceof WallCell || westCell instanceof SecureCell) {
			knownCells++;
		} else {
			swampCell = westCell;
		}
		if (knownCells == 3) {
			if (map.smells(currentCell)) {
				int swampXCoord = swampCell.getXCoord();
				int swampYCoord = swampCell.getYCoord();
				robotMap[swampXCoord][swampYCoord] = swampCell;
			}
		}
	}
	
	public GridCell getNorthCell(GridCell currentCell) {
		int xCoord = currentCell.getXCoord();
		int yCoord = currentCell.getYCoord();
		
		int northXCoord = xCoord - 1;
		int northYCoord = yCoord;
		
		if (northXCoord < 0)
			return null;
		
		return robotMap[northXCoord][northYCoord];
	}
	
	public GridCell getSouthCell(GridCell currentCell) {
		int xCoord = currentCell.getXCoord();
		int yCoord = currentCell.getYCoord();
		
		int southXCoord = xCoord + 1;
		int southYCoord = yCoord;
		
		if (southXCoord >= map.getMapHeight())
			return null;
		
		return robotMap[southXCoord][southYCoord];
	}
	
	public GridCell getEastCell(GridCell currentCell) {
		int xCoord = currentCell.getXCoord();
		int yCoord = currentCell.getYCoord();
		
		int eastXCoord = xCoord;
		int eastYCoord = yCoord + 1;
		
		if (eastYCoord >= map.getMapWidth())
			return null;
		
		return robotMap[eastXCoord][eastYCoord];
	}
	
	public GridCell getWestCell(GridCell currentCell) {
		int xCoord = currentCell.getXCoord();
		int yCoord = currentCell.getYCoord();
		
		int westXCoord = xCoord;
		int westYCoord = yCoord - 1;
		
		if (westYCoord < 0)
			return null;
		
		return robotMap[westXCoord][westYCoord];
	}
	
	public void initRobotMap() {
		int mapWidth = map.getMapWidth();
		int mapHeight = map.getMapHeight();
		
		for (int i = 0; i < mapWidth; i++) {
			for (int j = 0; j < mapHeight; j++) {
				robotMap[i][j] = new DummyCell(i, j);
			}
		}
	}
	
	public void printRobotMap() {
		int mapWidth = map.getMapWidth();
		int mapHeight = map.getMapHeight();
		
		for (int i = 0; i < mapWidth; i++) {
			for (int j = 0; j < mapHeight; j++) {
				System.out.print(robotMap[i][j]);
			}
			System.out.println();
		}
	}
}
