package monad;

public interface Functor<F, T> {
  <U> Function<? extends Functor<F, T>, ? extends Functor<?, U>> fmap(Function<T, U> f);
  <U> U get();
}