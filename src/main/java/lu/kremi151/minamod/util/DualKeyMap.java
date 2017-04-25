package lu.kremi151.minamod.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lu.kremi151.minamod.interfaces.ICallback;

public class DualKeyMap<T, U, V> implements Map<DualKeyMap.Key<T, U>, V>{

	private final Map<Key<T, U>, V> internalMap;

	public DualKeyMap(){
		this.internalMap = new HashMap<Key<T, U>, V>();
	}
	
	public DualKeyMap(ICallback<Map<Key<T, U>, V>> constructor){
		this.internalMap = constructor.callback();
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
		return internalMap.containsKey(new Key<T, U>(key1, key2));
	}

	@Override
	public boolean containsValue(Object value) {
		return internalMap.containsValue(value);
	}

	@Override
	public Set<java.util.Map.Entry<Key<T, U>, V>> entrySet() {
		return internalMap.entrySet();
	}

	@Override
	@Deprecated
	public V get(Object key) {
		return internalMap.get(key);
	}
	
	public V get(T key1, U key2){
		return internalMap.get(new Key<T, U>(key1, key2));
	}

	@Override
	public boolean isEmpty() {
		return internalMap.isEmpty();
	}

	@Override
	public Set<Key<T, U>> keySet() {
		return internalMap.keySet();
	}

	@Override
	public V put(Key<T, U> key, V value) {
		return internalMap.put(key, value);
	}
	
	public V put(T key1, U key2, V value){
		return internalMap.put(new Key<T, U>(key1, key2), value);
	}

	@Override
	public void putAll(Map<? extends Key<T, U>, ? extends V> m) {
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
		return internalMap.remove(new Key<T, U>(key1, key2));
	}

	@Override
	public int size() {
		return internalMap.size();
	}

	@Override
	public Collection<V> values() {
		return internalMap.values();
	}
	
	public static class UnorderedKey<T> extends Key<T, T>{

		public UnorderedKey(T key1, T key2) {
			super(key1, key2);
		}
		
		@Override
		public boolean equals(Object obj){
			if(obj instanceof Key){
				Key other = (Key)obj;
				return (this.key1.equals(other.key1) && this.key2.equals(other.key2)) || (this.key1.equals(other.key2) && this.key2.equals(other.key1));
			}else{
				return false;
			}
		}
		
		@Override
		public int hashCode(){
			return key1.hashCode() ^ key2.hashCode();
		}
	}
	
	public static class Key<T, U>{
		
		protected final T key1;
		protected final U key2;
		
		public Key(T key1, U key2){
			this.key1 = key1;
			this.key2 = key2;
		}
		
		@Override
		public boolean equals(Object obj){
			if(obj instanceof Key){
				Key other = (Key)obj;
				return this.key1.equals(other.key1) && this.key2.equals(other.key2);
			}else{
				return false;
			}
		}
		
		@Override
		public int hashCode(){
			return (key1.hashCode() * 31) + key2.hashCode();
		}
	}
}
