
public class Fisier extends AFisier {
	String content;
	Fisier (String name, String type, String owner_user, String owner_group, String c, AFisier parinte) {
		super(name, type, owner_user, owner_group, parinte);
		content = c;
	}
	@Override
	/**
	 * Afiseaza continutul fisierului
	 */
	public String toString() {
		return super.toString();
	}
}
