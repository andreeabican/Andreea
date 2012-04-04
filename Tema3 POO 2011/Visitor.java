/**
 * 
 * @author Simona Badoiu
 * 
 */
public interface Visitor {
	/**
	 * 
	 * @param v - un nod de tip Variabila
	 */
	public void visit(Variabila v);
	/**
	 *
	 * @param v - un nod de tip Valoare
	 */
	public void visit(Valoare v);
	/**
	 * 
	 * @param e - un nod de tip OperatorEgal
	 */
	public void visit(OperatorEgal e);
	/**
	 * 
	 * @param p - un nod de tip OperatorPlus
	 */
	public void visit(OperatorPlus p);
	/**
	 * 
	 * @param o - un nod de tip OperatorOri
	 */
	public void visit(OperatorOri o);
}
