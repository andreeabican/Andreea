
public class Continut {
	double c, f;
	int v, pixVecin, pixNod;
	
	public Continut(double capacitate, double flux, int vecin, int valPixelV, int valPixelN ) {
		c = capacitate;
		f = flux;
		v = vecin;
		pixVecin = valPixelV;
		pixNod = valPixelN;
	}
	
	@Override
	public String toString() {
		return "v: " + v + ", c: " + c + ", f: " + f;
	}
}
