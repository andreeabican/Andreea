
public class SecureCell implements GridCell {
	int xCoord, yCoord;
	boolean visited = false;
	
	public SecureCell(int xCoord, int yCoord) {
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
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public String toString() {
		return "S";
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
		// TODO Auto-generated method stub
		return false;
	}
	
}
