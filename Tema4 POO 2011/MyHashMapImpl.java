import java.util.ArrayList;
import java.util.List;

/**
 * Implementeaza interfata pentru o tabela de dispersie asociativa.
 * @author Simona Badoiu
 * 
 * @param <K> tipul cheilor
 * @param <V> tipul valorilor
 */
public class MyHashMapImpl<K, V> implements MyHashMap<K, V> {
	int size = 0;
	BucketImpl<K, V>[] list;
	
	/**
	 *  Constructor
	 * @param numBuckets - initializeaza vectorul de tip BucketImpl - cu numBuckets elemente
	 */
	@SuppressWarnings("unchecked")
	public MyHashMapImpl(int numBuckets) {
		if (numBuckets != 0) {
			list = new BucketImpl[numBuckets];
		}
		else
			list = null;
	}
	
	@Override
	public V get(K key) {
		if (list == null)
			return null;
		else {
			// functia index calculeaza pozitia din HashMap pe baza valorii variabilei key
			int index = index(key);
			// daca avem valori pe pozitia generata cu functia index
			if (list[index] != null) {
				// parcurgem lista si verificam daca in aceasta lista exista un element cu cheia key
				List<? extends EntryImpl<K, V>> l = list[index].bucket;
				java.util.Iterator<? extends EntryImpl<K, V>> it = l.iterator();
				while (it.hasNext()) {
					EntryImpl<K, V> e = it.next();
					if (e.key.equals(key) || e.key == key) {
						// Daca exista returnam valoarea, iar daca nu, returnam null
						return e.value;
					}
				}
			}
		}
		return null;
	}

	@Override
	public V put(K key, V value) {
		if (list == null) {
			return null;
		}
		// se determina indexul corespunzator
		int index = index(key);
		// ok devine 1 daca cheia K exista deja in lista
		int ok = 0;
		BucketImpl<K, V> b = list[index];
		if ( b != null) {
			java.util.Iterator<? extends EntryImpl<K, V>> it = list[index].bucket.iterator();
			while (it.hasNext()) {
				EntryImpl<K, V> e = it.next();
				// Daca mai exista o valoarea cu aceeasi cheie, atunci o inlocuim cu noua valoare
				if (key.equals(e.getKey()) || key == e.getKey()) {
					V oldV = e.getValue();
					e.value = value;
					ok = 1;
					// si returnam vechea valoare
					return oldV;
				}
			}
			// daca cheia nu exista inca, adaugam noua pereche cheie-valoare in bucketul de pe pozitia index din lista
			if (ok == 0) {
				EntryImpl<K, V> aux = new EntryImpl<K, V>(key, value);
				list[index].bucket.add(aux);
				size ++;
			}
		}
		// daca la acel index nu s-a adaugat inca nici o valoare, se initializeaza list[index] si se adauga noua pereche
		// cheie-element
		else {
			list[index] = new BucketImpl<>();
			EntryImpl<K, V> aux = new EntryImpl<K, V>(key, value);
			list[index].bucket.add(aux);
			size++;
		}
		return null;
	}

	@Override
	public V remove(K key) {
		if (list == null) {
			return null;
		}
		// determinam indexul din list
		int index = index(key);
		// mergem la acel index si cautam in bucket daca nu este null
		if (list[index] != null) {
			java.util.Iterator<? extends EntryImpl<K, V>> it = list[index].bucket.iterator();
			while (it.hasNext()) {
				EntryImpl<K, V> e = it.next();
				// daca gasim cheia key in bucket, stergem acest element din bucket si returnam valoarea
				if (e.key.equals(key) || e.key == key) {
					V oldV = e.value;
					it.remove();
					size--;
					return oldV;
				}
			}
		}
		return null;
	}

	@Override
	public int size() {
		return size;
	}
	// calculeaza indexul din list corespunzator valorii key
	public int index(K key) {
		if (list != null) {
			return (Math.abs(key.hashCode())%(list.length));
		}
		return 0;
	}

	@Override
	public List<? extends MyHashMap.Bucket<K, V>> getBuckets() {
		if ( list == null ) {
			return null;
		}
		// se parcurge lista de buckets si fiecare element se adauga in lista pe care o returnam
		List<BucketImpl<K, V>> l = new ArrayList<BucketImpl<K, V>>();
				for (int i = 0; i < list.length; i++) {
					BucketImpl<K, V> b = list[i];
					l.add(i, b);
				}
		return l;
	}

}
