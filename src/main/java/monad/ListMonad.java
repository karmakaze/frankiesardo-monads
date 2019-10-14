package monad;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

@SuppressWarnings("unchecked")
public final class ListMonad<T> extends Monad<List<T>, T> implements List<T> {

  public static <U> ListMonad<U> instance(U value) {
    return new ListMonad<U>(Collections.singletonList(value));
  }

  public static <U> ListMonad<U> instance(List<U> list) {
    return new ListMonad<U>(list);
  }

  private List<T> list;

  public ListMonad() {
    this.list = new LinkedList<T>();
  }

  public ListMonad(List<T> list) {
    this.list = new LinkedList<T>(list);
  }

  public <U> U get() {
    return (U) list;
  }

  @Override public <U> ListMonad<U> bind(Function<T, ? extends Monad<?, U>> f) {
    return (ListMonad<U>) super.bind(f);
  }

  @Override protected <U> ListMonad<U> unit(U elem) {
    return instance(elem);
  }

  @Override protected <U> ListMonad<U> fail(RuntimeException e) {
    return new ListMonad<U>();
  }

  @Override protected <U> ListMonad<U> yield(final Function<T, U> f) { // map
    return foldLeft(new ListMonad<U>(), new Operator<T, ListMonad<U>>() {
      public ListMonad<U> apply(ListMonad<U> result, T elem) {
        result.add(f.apply(elem));
        return result;
      }
    });
  }

  @Override protected <U> ListMonad<U> join() { // concat
    return foldLeft(new ListMonad<U>(), new Operator<T, ListMonad<U>>() {
      public ListMonad<U> apply(ListMonad<U> result, T nested) {
        result.addAll((List<U>) nested);
        return result;
      }
    });
  }

  public <U> U foldLeft(U z, Operator<T, U> op) {
    U result = z;
    for (T elem : list) {
      result = op.apply(result, elem);
    }
    return result;
  }

  public T reduceLeft(final Operator<T, T> op) {
    if (!list.isEmpty()) {
      T head = list.get(0);
      if (list.size() > 1) {
        ListMonad<T> tail = instance(list.subList(1, list.size()));
        return tail.foldLeft(head, op);
      } else {
        return head;
      }
    } else {
      throw new UnsupportedOperationException("empty.reduceLeft");
    }
  }

  public ListMonad<T> filter(final Function<T, Boolean> pred) {
    return bind(new Function<T, ListMonad<T>>() {
      public ListMonad<T> apply(T elem) {
        if (pred.apply(elem)) {
          return instance(elem);
        } else {
          return instance(Collections.EMPTY_LIST);
        }
      }
    });
  }

  // Boilerplate

  public void add(int location, T object) {
    list.add(location, object);
  }

  public boolean add(T object) {
    return list.add(object);
  }

  public boolean addAll(int location, Collection<? extends T> collection) {
    return list.addAll(location, collection);
  }

  public boolean addAll(Collection<? extends T> collection) {
    return list.addAll(collection);
  }

  @Override public void clear() {
    list.clear();
  }

  @Override public boolean contains(Object object) {
    return list.contains(object);
  }

  @Override public boolean containsAll(Collection<?> collection) {
    return list.containsAll(collection);
  }

  @Override public boolean equals(Object object) {
    return list.equals(object);
  }

  @Override public T get(int location) {
    return list.get(location);
  }

  @Override public int hashCode() {
    return list.hashCode();
  }

  @Override public int indexOf(Object object) {
    return list.indexOf(object);
  }

  @Override public boolean isEmpty() {
    return list.isEmpty();
  }

  @Override public Iterator<T> iterator() {
    return list.iterator();
  }

  @Override public int lastIndexOf(Object object) {
    return list.lastIndexOf(object);
  }

  @Override public ListIterator<T> listIterator() {
    return list.listIterator();
  }

  @Override public ListIterator<T> listIterator(int location) {
    return list.listIterator(location);
  }

  @Override public T remove(int location) {
    return list.remove(location);
  }

  @Override public boolean remove(Object object) {
    return list.remove(object);
  }

  @Override public boolean removeAll(Collection<?> collection) {
    return list.removeAll(collection);
  }

  @Override public boolean retainAll(Collection<?> collection) {
    return list.retainAll(collection);
  }

  public T set(int location, T object) {
    return list.set(location, object);
  }

  @Override public int size() {
    return list.size();
  }

  @Override public List<T> subList(int start, int end) {
    return list.subList(start, end);
  }

  @Override public Object[] toArray() {
    return list.toArray();
  }

  @Override public <T1> T1[] toArray(T1[] array) {
    return list.toArray(array);
  }

  @Override public String toString() {
    return list.toString();
  }
}
