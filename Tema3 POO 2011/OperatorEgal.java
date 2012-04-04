/**
 * 
 * @author Simona Badoiu
 *
 */
public class OperatorEgal extends Node implements Visitable {

	public OperatorEgal(String str, int col, int lin) {
		super(str, col, lin);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
		
	}

}
