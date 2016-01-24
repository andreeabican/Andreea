
public class WallCell implements GridCell {
	int xCoord, yCoord;
	boolean visited;
	
	public WallCell(int xCoord, int yCoord) {
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		visited = false;
	}

	@Override
	public void visit() {
		visited = true;
	}

	@Override
	public boolean smells() {
		return false;
	}
	
	@Override
	public String toString() {
		return "w";
	}

	@Override
	public int getXCoord() {
		// TODO Auto-generated method stub
		return xCoord;
	}

	@Override
	public int getYCoord() {
		// TODO Auto-generated method stub
		return yCoord;
	}

	@Override
	public boolean canBeVisited() {
		if (!visited)
			return true;
		return false;
	}
	
}
