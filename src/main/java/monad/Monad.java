package monad;

@SuppressWarnings("unchecked")
public abstract class Monad<M, T> extends Applicative<M, T> {

  protected <U> Monad<?, U> yield(Function<T, U> f) {
    Applicative<Function<T, U>, U> ff = (Applicative<Function<T, U>, U>) unit(f);
    return (Monad<?, U>) apply(ff).apply(this);
  }

  protected <U> Monad<?, U> join() {
    return get();
  }

  protected <U> Monad<?, U> fail(RuntimeException e) {
    throw e;
  }

  public <U> Function<Monad<M, T>, Monad<?, U>> fmap(final Function<T, U> f) {
    return new Function<Monad<M, T>, Monad<?, U>>() {
      public Monad<?, U> apply(final Monad<M, T> mt) {
        try {
          return mt.yield(f);
        } catch (RuntimeException e) {
          return mt.fail(e);
        }
      }
    };
  }

  public <U> Monad<?, U> map(Function<T, U> f) {
    return fmap(f).apply(this);
  }

  public <U> Monad<?, U> bind(Function<T, ? extends Monad<?, U>> f) {
    return map(f).join();
  }
}
