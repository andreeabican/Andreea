/**
 * Clasa in care se suprascrie functia hashCode din Student astfel incat sa returneze valoarea 1 intotdeauna
 * @author Simona Badoiu
 *
 */
public class LazyStudent extends Student {
	/**
	 * Constructor LazyStudent
	 * @param n - numele studentului lenes
	 * @param v - varsta studentului lenes
	 */
	LazyStudent(String n, int v) {
		super(n, v);
	}

	@Override
	public int hashCode() {
		return 1;
	}
}
