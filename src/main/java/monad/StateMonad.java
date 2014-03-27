package monad;

@SuppressWarnings("unchecked")
public abstract class StateMonad<T, State> extends Monad<ValueWithState<T, State>, T> {

  public static <U, State> StateMonad<U, State> instance(final U value) {
    return new StateMonad<U, State>() {
      @Override protected ValueWithState<U, State> getValueWithState(State state) {
        return new ValueWithState<U, State>(value, state);
      }
    };
  }

  @Override protected <U> Applicative<?, U> unit(U value) {
    return instance(value);
  }

  @Override protected <U> StateMonad<U, State> yield(final Function<T, U> f) {
    return new StateMonad<U, State>() {
      @Override protected ValueWithState<U, State> getValueWithState(State state) {
        ValueWithState<T, State> valueWithState = StateMonad.this.getValueWithState(state);
        U newValue = f.apply(valueWithState.getValue());
        return new ValueWithState<U, State>(newValue, valueWithState.getState());
      }
    };
  }

  @Override protected <U> StateMonad<U, State> join() {
    return (StateMonad<U, State>) getValueWithState(null).getValue();
  }

  protected abstract ValueWithState<T, State> getValueWithState(State state);

  // don't use me
  @Override public <U> U get() {
    return null;
  }

  @Override public <U> StateMonad<U, State> bind(Function<T, ? extends Monad<?, U>> f) {
    return (StateMonad<U, State>) super.bind(f);
  }
}

class ValueWithState<Value, State> {
  private final Value value;
  private final State state;

  ValueWithState(Value value, State state) {
    this.value = value;
    this.state = state;
  }

  public Value getValue() {
    return value;
  }

  public State getState() {
    return state;
  }
}
