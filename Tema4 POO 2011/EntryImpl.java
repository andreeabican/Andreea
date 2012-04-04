/**
 * Intrare in tabela de dispersie (asociere cheie-valoare).
 * @author Simona Badoiu
 *
 * @param <K> tipul cheilor
 * @param <V> tipul valorilor
 */
public class EntryImpl<K, V> implements MyHashMap.Entry<K, V> {
	K key;
	V value;
	/**
	 * Constructor
	 * @param k - cheie
	 * @param v - valoare
	 */
	public EntryImpl(K k, V v) {
		key = k;
		value = v;
	}
	@Override
	public K getKey() {
		return key;
	}

	@Override
	public V getValue() {
		return value;
	}
	
}
