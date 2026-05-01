package sk.ditec.kmd.data.ws.dto;

import java.util.ArrayList;

public class DTOCiselnikRecord {

	ArrayList<Object> values = new ArrayList<Object>();
	String dummy;

	public int size() {
		return values.size();
	}

	public boolean isEmpty() {
		return values.isEmpty();
	}

	public boolean contains(Object o) {
		return values.contains(o);
	}

	// public Iterator<Object> iterator() {
	// return values.iterator();
	// }

	public Object[] toArray() {
		return values.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return values.toArray(a);
	}

	public boolean add(Object e) {
		return values.add(e);
	}

	public boolean remove(Object o) {
		return values.remove(o);
	}

	public boolean containsAll(ArrayList<?> c) {
		return values.containsAll(c);
	}

	public boolean addAll(ArrayList<? extends Object> c) {
		return values.addAll(c);
	}

	public boolean addAll(int index, ArrayList<? extends Object> c) {
		return values.addAll(index, c);
	}

	public boolean removeAll(ArrayList<?> c) {
		return values.removeAll(c);
	}

	public boolean retainAll(ArrayList<?> c) {
		return values.retainAll(c);
	}

	public void clear() {
		values.clear();
	}

	public Object get(int index) {
		return values.get(index);
	}

	public Object set(int index, Object element) {
		return values.set(index, element);
	}

	public void add(int index, Object element) {
		values.add(index, element);
	}

	public Object remove(int index) {
		return values.remove(index);
	}

	public int indexOf(Object o) {
		return values.indexOf(o);
	}

	public int lastIndexOf(Object o) {
		return values.lastIndexOf(o);
	}

	// public ListIterator<Object> listIterator() {
	// return listIterator();
	// }
	//
	// public ListIterator<Object> listIterator(int index) {
	// return values.listIterator(index);
	// }

	public ArrayList<Object> subList(int fromIndex, int toIndex) {
		ArrayList<Object> result = new ArrayList<Object>();
		result.addAll(values.subList(fromIndex, toIndex));
		return result;
	}

	public ArrayList<Object> getValues() {
		return values;
	}

	public void setValues(ArrayList<Object> values) {
		this.values = values;
	}

	public String getDummy() {
		return dummy;
	}

	public void setDummy(String dummy) {
		this.dummy = dummy;
	}


}
