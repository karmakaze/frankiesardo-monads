package monad;

@SuppressWarnings("unchecked")
public abstract class EitherMonad<M, L, R, T> extends Monad<M, T> implements Either<L, R> {
  private T value;

  protected EitherMonad(T value) {
    this.value = value;
  }

  public final <U> U get() {
    return (U) value;
  }

  @Override public final <U> EitherMonad<M, L, R, U> bind(Function<T, ? extends Monad<?, U>> f) {
    return (EitherMonad<M, L, R, U>) super.bind(f);
  }
}

interface Either<L, R> {

  Left<L, R> left();

  Right<L, R> right();

  boolean isLeft();

  boolean isRight();
}

@SuppressWarnings("unchecked")
class Left<L, R> extends EitherMonad<Left<L, R>, L, R, L> {

  public static <L, R> Left<L, R> instance(L value) {
    return new Left<L, R>(value);
  }

  protected Left(L value) {
    super(value);
  }

  @Override protected <U> Left<U, R> unit(U value) {
    return instance(value);
  }

  @Override public final Left<L, R> left() {
    return this;
  }

  @Override public final Right<L, R> right() {
    return new Right<L, R>((R) get()) {
      @Override protected <U> Right<L, U> yield(Function<R, U> f) {
        return (Right<L, U>) this;
      }

      @Override protected <U> Right<L, U> join() {
        return (Right<L, U>) this;
      }
    };
  }

  @Override public final boolean isLeft() {
    return true;
  }

  @Override public final boolean isRight() {
    return false;
  }

  @Override public final String toString() {
    return "Left(" + get() + ")";
  }
}

@SuppressWarnings("unchecked")
class Right<L, R> extends EitherMonad<Right<L, R>, L, R, R> {

  public static <L, R> Right<L, R> instance(R value) {
    return new Right<L, R>(value);
  }

  protected Right(R value) {
    super(value);
  }

  @Override protected <U> Right<L, U> unit(U value) {
    return instance(value);
  }

  @Override public final Right<L, R> right() {
    return this;
  }

  @Override public final Left<L, R> left() {
    return new Left<L, R>((L) get()) {
      @Override protected <U> Left<U, R> yield(Function<L, U> f) {
        return (Left<U, R>) this;
      }

      @Override protected <U> Left<U, R> join() {
        return (Left<U, R>) this;
      }
    };
  }

  @Override public final boolean isLeft() {
    return false;
  }

  @Override public final boolean isRight() {
    return true;
  }

  @Override public final String toString() {
    return "Right(" + get() + ")";
  }
}


