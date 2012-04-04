import java.util.Map;

public class Director extends AFisier {
	ContentDir content = new ContentDir();
	Director(String name, String type, String owner_user, String owner_group, ContentDir c, AFisier parinte) {
		super(name, type, owner_user, owner_group, parinte);
		content = c;
		}
	@Override
	public String toString() {
		// Listarea continutului directorului
		String s="";
		for (Map.Entry<String, AFisier> entry : content.c.entrySet()) {
			s += entry.getValue().name + " ";
		}
		return super.toString()+s;
	}
	/*public void remove(AFisier f) {
		// Fisierul nu o sa aiba o functie remove pentru ca nu putem sterge decat din interiorul unui
		//director, iar eu o sa merg pana la directorul in care se gaseste directorul sau folderul
		// pe care trebuie sa il sterg
		content.c.remove(f.name); //Sterg elementul din map, al carei cheie este numele fisierului/directorului
								 // pe care vrem sa il stergem
	}*/
}