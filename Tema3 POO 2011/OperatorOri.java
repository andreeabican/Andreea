/**
 * 
 * @author Simona Badoiu
 *
 */
public class OperatorOri extends Node implements Visitable {

	public OperatorOri(String str, int col, int lin) {
		super(str, col, lin);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
		
	}
}
