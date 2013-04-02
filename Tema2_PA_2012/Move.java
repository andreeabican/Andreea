
public class Move {
	int start;
	int dist;
	float score;
	/**
	 * 
	 * @param s - pozitia de start
	 * @param d - distanta pe care se va deplasa piesa
	 * @param sc - scorul - nu stiu daca fac ceva cu el
	 */
	public Move(int s, int d, float sc) {
		start = s;
		dist = d;
		score = sc;
	}
	
	@Override
	public String toString() {
		return "[" + start + ", " + dist + ", " + score + "]";
	}
}
