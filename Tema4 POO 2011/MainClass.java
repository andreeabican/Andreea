import java.util.*;
/**
 * Clasa main
 * @author Simona Badoiu
 *
 */
public class MainClass {
	public static void main(String[] args) {
		Random generator = new Random();
	// lungimea numelui nu este mai mare decat 20
	// varsta nu o sa fie mai mare decat 25
	List<Student> l = new ArrayList<Student>();
	MyHashMap<Student, Integer> hmap = new MyHashMapImpl<Student, Integer>(20);
		//Generare aleatoare Student
		for (int i = 0; i < 1100; i++) {
			
			// lungimea numelui studentului
			int len = Math.abs(generator.nextInt()) % 21;
			
			// generare nume student
			StringBuffer name = new StringBuffer();
			for (int x = 0; x < len; x++) {
				name.append((char)((int)(Math.random()*26)+97));  
			}
		    
		    //generare varsta student
		    int age = Math.abs(generator.nextInt()) % 26;
			l.add(new Student(name.toString(), age));
		}
		Iterator<Student> it = l.iterator();
		while (it.hasNext()) {
			int nota = Math.abs(generator.nextInt()) % 11;
			Student s = it.next();
			hmap.put(s, nota);
		}
		
		// Generare aleatoare LazyStudent
		List<LazyStudent> lls = new ArrayList<LazyStudent>();
		MyHashMap<LazyStudent, Integer> hmapls = new MyHashMapImpl<LazyStudent, Integer>(20);
			//Generare aleatoare Student
			for (int i = 0; i < 1100; i++) {
				
				// lungimea numelui studentului
				int len = Math.abs(generator.nextInt()) % 21;
				
				// generare nume student
				StringBuffer name = new StringBuffer();
				for (int x = 0; x < len; x++) {
					name.append((char)((int)(Math.random()*26)+97));  
				}
			    
			    //generare varsta student
			    int age = Math.abs(generator.nextInt()) % 26;
				lls.add(new LazyStudent(name.toString(), age));
			}
			Iterator<LazyStudent> itls = lls.iterator();
			while (itls.hasNext()) {
				int nota = Math.abs(generator.nextInt()) % 11;
				LazyStudent ls = itls.next();
				hmapls.put(ls, nota);
			}
		
		//Pentru testare get - generam 1000 de numere intre 0 si 1100 si incercam sa le gasim in hmap
		
		// Student
		long start = System.currentTimeMillis();
		for (int i =0; i < 1000; i++) {
			int k = Math.abs(generator.nextInt()) % 1100;
			@SuppressWarnings("unused")
			int n = hmap.get(l.get(k));
		}
		long stop = System.currentTimeMillis();
		System.out.println(stop - start + " - pentru Student");
		
		// LazyStudent
		long startl = System.currentTimeMillis();
		for (int i =0; i < 1000; i++) {
			int k = Math.abs(generator.nextInt()) % 1100;
			@SuppressWarnings("unused")
			int n = hmapls.get(lls.get(k));
		}
		long stopl = System.currentTimeMillis();
		System.out.println(stopl - startl + " - pentru LazyStudent");
	}
}
