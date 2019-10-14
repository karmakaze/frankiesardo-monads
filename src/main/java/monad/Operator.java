package monad;

public interface Operator<T, U> {
  U apply(U arg1, T arg2);
}
