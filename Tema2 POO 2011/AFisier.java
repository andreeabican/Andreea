/**
 * 
 * @author Simona Andreea Badoiu
 *
 */
public abstract class AFisier {
	String name, type, owner_user, owner_group;
	AFisier parinte;
	//Stringuri pentru drepturi
	String duser="rwx", dgrup="rwx", dall="";
	
	/**
	 * Constructor
	 * @param n - numele fisierului/directorului
	 * @param t - tipul fisierului/directorului
	 * @param owner_u - numele utilizatorului care a creat fisierul/directorul
	 * @param owner_g - numele grupului din care face parte utilizatorul care a creat fisierul/directorul
	 * @param f - parintele acestui fisier/director
	 */
	AFisier(String n, String t, String owner_u, String owner_g, AFisier f) {
		name = n;
		type = t;
		owner_user = owner_u;
		owner_group = owner_g;
		parinte = f;
	}
	@Override
	public String toString() {
		String s =name+" "+type+" "+owner_user+" "+owner_group+" ";
		return s;
	}
}
