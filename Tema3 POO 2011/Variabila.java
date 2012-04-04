
public class Variabila extends Node implements Visitable {

	public Variabila(String str, int col, int lin) {
		super(str, col, lin);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
		
	}

}
