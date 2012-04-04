/**
 * 
 * @author Simona Badoiu
 *
 */
public class Student {
	String nume;
	Integer varsta;
	
	Student(String n, int v) {
		nume = n;
		varsta = v;
	}
	
	@Override
	public int hashCode() {
		int h = 17;
		h = 37*h + nume.hashCode();
		h = 37 * h + varsta.hashCode();
		return h;
	}
	
	@Override
	public boolean equals(Object obj) {
		Student s = (Student)obj;
		if (s.varsta == varsta && s.nume.equals(nume))
			return true;
		return false;
	}
}
