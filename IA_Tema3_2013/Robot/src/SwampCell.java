
public class SwampCell implements GridCell {
	int xCoord, yCoord;
	boolean visited;
	
	public SwampCell(int xCoord, int yCoord) {
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
		return true;
	}
	
	@Override
	public String toString() {
		return "s";
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
