/**
 * 
 * @author andreea
 * Retine parametrii medie si deviatie (u si sigma)
 */
public class Param {
	double u,s;
	
	public Param(double medie, double deviatie) {
		u = medie;
		s = deviatie;
	} 
	
	@Override
	public String toString() {
		return "medie: " + u + "\n" + "deviatie: " + s;
		}
}
