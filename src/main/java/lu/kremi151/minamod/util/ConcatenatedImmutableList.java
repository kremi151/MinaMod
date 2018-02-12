package lu.kremi151.minamod.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.annotation.Nullable;

/**
 * Efficient and immutable view of a simulated concatenation of two lists. This list is compatible with modifications done to the two original lists.
 * @author kremi151
 *
 * @param <T> The type of objects in this list
 */
public class ConcatenatedImmutableList<T> implements List<T>{
	
	private final List<T> a, b;
	
	public ConcatenatedImmutableList(List<T> first, List<T> second) {
		this.a = first;
		this.b = second;
	}

	@Override
	public boolean add(T arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(int arg0, T arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(Collection<? extends T> arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(int arg0, Collection<? extends T> arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean contains(Object arg0) {
		return a.contains(arg0) || b.contains(arg0);
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		HashSet<?> set = new HashSet<>(arg0);
		Iterator<?> it = set.iterator();
		while(it.hasNext()) {
			Object obj = it.next();
			if(a.contains(obj) || b.contains(obj)) {
				it.remove();
			}
		}
		return set.isEmpty();
	}

	@Override
	public T get(int arg0) {
		if(arg0 >= a.size()) {
			return b.get(arg0 - a.size());
		}else {
			return a.get(arg0);
		}
	}

	@Override
	public int indexOf(Object arg0) {
		int index = a.indexOf(arg0);
		if(index != -1) {
			return index;
		}else {
			index = b.indexOf(arg0);
			if(index != -1) {
				return a.size() + index;
			}
		}
		return -1;
	}

	@Override
	public boolean isEmpty() {
		return a.isEmpty() && b.isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		return new CListIterator();
	}

	@Override
	public int lastIndexOf(Object arg0) {
		int lastIndex = b.lastIndexOf(arg0);
		if(lastIndex != -1) {
			return a.size() + lastIndex;
		}else {
			lastIndex = a.lastIndexOf(arg0);
			if(lastIndex != -1) {
				return lastIndex;
			}
		}
		return -1;
	}

	@Override
	public ListIterator<T> listIterator() {
		return new CListIterator();
	}

	@Override
	public ListIterator<T> listIterator(int arg0) {
		return new CListIterator(arg0);
	}

	@Override
	public boolean remove(Object arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public T remove(int arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public T set(int arg0, T arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		return a.size() + b.size();
	}

	@Override
	public List<T> subList(int arg0, int arg1) {
		throw new UnsupportedOperationException();//Sorry but, not sorry
	}

	@Override
	public Object[] toArray() {
		Object array[] = new Object[a.size() + b.size()];
		for(int i = 0 ; i < a.size() ; i++) {
			array[i] = a.get(i);
		}
		for(int i = 0 ; i < b.size() ; i++) {
			array[i + a.size()] = b.get(i);
		}
		return array;
	}

	@Override
	public <U> U[] toArray(U[] arg0) {
		ArrayList<U> arrayList = new ArrayList<>(a.size() + b.size());
		arrayList.addAll((Collection<? extends U>) a);
		arrayList.addAll((Collection<? extends U>) b);
		return arrayList.toArray(arg0);
	}
	
	/**
	 * Creates a concatenated view of multiple lists. May be inefficient if more than 2 lists.
	 * @param lists The lists to concatenate
	 * @return Returns the concatenated view of the supplied lists if at least 2 lists provided, the given list if only one list provided, or null if none provided.
	 */
	@Nullable
	public static <V> List<V> concat(List<V>... lists){
		if(lists.length == 0) {
			return null;
		}else if(lists.length == 1) {
			return lists[0];
		}else if(lists.length == 2) {
			return new ConcatenatedImmutableList<V>(lists[0], lists[1]);
		}else {
			ConcatenatedImmutableList<V> last = new ConcatenatedImmutableList<V>(lists[0], lists[1]);
			for(int i = 2 ; i < lists.length ; i++) {
				last = new ConcatenatedImmutableList<V>(last, lists[i]);
			}
			return last;
		}
	}
	
	private class CListIterator implements ListIterator<T>, Iterator<T>{
		
		private int index = 0;
		
		CListIterator(){}
		
		CListIterator(int index){
			this.index = index;
		}

		@Override
		public void add(T arg0) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean hasNext() {
			return index < size();
		}

		@Override
		public boolean hasPrevious() {
			return index > 0;
		}

		@Override
		public T next() {
			return get(index++);
		}

		@Override
		public int nextIndex() {
			return index;
		}

		@Override
		public T previous() {
			return get(index--);
		}

		@Override
		public int previousIndex() {
			return index;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void set(T arg0) {
			throw new UnsupportedOperationException();
		}
		
	}

}
