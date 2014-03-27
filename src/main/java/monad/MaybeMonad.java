package monad;

import java.util.NoSuchElementException;

@SuppressWarnings("unchecked")
public abstract class MaybeMonad<T> extends Monad<MaybeMonad<T>, T> {

  public static <U> MaybeMonad<U> instance(U value) {
    return (MaybeMonad<U>) (value != null ? Just.instance(value) : Nothing.instance());
  }

  @Override public <U> MaybeMonad<U> bind(Function<T, ? extends Monad<?, U>> f) {
    return (MaybeMonad<U>) super.bind(f);
  }

  @Override protected <U> MaybeMonad<U> fail(RuntimeException e) {
    return Nothing.instance();
  }

  public abstract boolean isJust();

  public abstract boolean isNothing();
}

@SuppressWarnings("unchecked")
class Just<T> extends MaybeMonad<T> {

  public static <U> Just<U> instance(U value) {
    return new Just<U>(value);
  }

  private T value;

  private Just(T value) {
    this.value = value;
  }

  @Override public <U> U get() {
    return (U) value;
  }

  @Override protected <U> MaybeMonad<U> unit(U value) {
    return Just.instance(value);
  }

  @Override public boolean isJust() {
    return true;
  }

  @Override public boolean isNothing() {
    return false;
  }

  @Override public String toString() {
    return "Just(" + get() + ")";
  }
}

@SuppressWarnings("unchecked")
class Nothing<T> extends MaybeMonad<T> {

  private static final Nothing NOTHING = new Nothing();

  public static <U> Nothing<U> instance() {
    return NOTHING;
  }

  @Override public <U> U get() {
    throw new NoSuchElementException("Nothing.get");
  }

  @Override protected <U> MaybeMonad<U> unit(U value) {
    return NOTHING;
  }

  @Override protected <U> MaybeMonad<U> yield(Function<T, U> f) {
    return NOTHING;
  }

  @Override protected <U> MaybeMonad<U> join() {
    return NOTHING;
  }

  @Override public boolean isJust() {
    return false;
  }

  @Override public boolean isNothing() {
    return true;
  }

  @Override public String toString() {
    return "Nothing";
  }
}
