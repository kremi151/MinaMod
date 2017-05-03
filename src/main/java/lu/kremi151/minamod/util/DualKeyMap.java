package lu.kremi151.minamod.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

public class DualKeyMap<T, U, V> implements Map<Pair<T, U>, V>{

	private final Map<Pair<T, U>, V> internalMap;

	public DualKeyMap(){
		this.internalMap = new HashMap<Pair<T, U>, V>();
	}
	
	public DualKeyMap(Map<Pair<T, U>, V> map){
		this.internalMap = map;
	}
	
	@Override
	public void clear() {
		internalMap.clear();
	}

	@Override
	@Deprecated
	public boolean containsKey(Object key) {
		return internalMap.containsKey(key);
	}
	
	public boolean containsKey(T key1, U key2) {
		return internalMap.containsKey(Pair.of(key1, key2));
	}

	@Override
	public boolean containsValue(Object value) {
		return internalMap.containsValue(value);
	}

	@Override
	public Set<java.util.Map.Entry<Pair<T, U>, V>> entrySet() {
		return internalMap.entrySet();
	}

	@Override
	@Deprecated
	public V get(Object key) {
		return internalMap.get(key);
	}
	
	public V get(T key1, U key2){
		return internalMap.get(Pair.of(key1, key2));
	}

	@Override
	public boolean isEmpty() {
		return internalMap.isEmpty();
	}

	@Override
	public Set<Pair<T, U>> keySet() {
		return internalMap.keySet();
	}

	@Override
	public V put(Pair<T, U> key, V value) {
		return internalMap.put(key, value);
	}
	
	public V put(T key1, U key2, V value){
		return internalMap.put(Pair.of(key1, key2), value);
	}

	@Override
	public void putAll(Map<? extends Pair<T, U>, ? extends V> m) {
		internalMap.putAll(m);
	}

	@Override
	@Deprecated
	public V remove(Object key) {
		return internalMap.remove(key);
	}
	
	@Override
	@Deprecated
	public boolean remove(Object key, Object value){
		return internalMap.remove(key, value);
	}
	
	public V removeKey(T key1, U key2) {
		return internalMap.remove(Pair.of(key1, key2));
	}

	@Override
	public int size() {
		return internalMap.size();
	}

	@Override
	public Collection<V> values() {
		return internalMap.values();
	}
}
