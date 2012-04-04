import java.util.ArrayList;
import java.util.List;

/**
 * Implementeaza interfata Bucket
 * @author Simona Badoiu
 *
 * @param <K> tipul cheilor
 * @param <V> tipul valorilor
 */
public class BucketImpl<K, V> implements MyHashMap.Bucket<K, V> {
	// creeaza vectorul bucket
	List<EntryImpl<K, V>> bucket = new ArrayList<EntryImpl<K, V>>();
	
	@Override
	public List<? extends EntryImpl<K, V>> getEntries() {
		return bucket;
	}
}
