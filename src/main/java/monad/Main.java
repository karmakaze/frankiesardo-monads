package monad;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {

  static Function<Integer, Monad<ListMonad<Integer>, Integer>> repeatNumber =
      new Function<Integer, Monad<ListMonad<Integer>, Integer>>() {
        @Override public Monad<ListMonad<Integer>, Integer> apply(Integer arg) {
          return ListMonad.instance(Collections.nCopies(arg, arg));
        }
      };

  static Function<Integer, ListMonad<Object>> listFail = new Function<Integer, ListMonad<Object>>() {
    @Override public ListMonad<Object> apply(Integer arg) {
      throw new RuntimeException();
    }
  };

  static Function<String, MaybeMonad<Integer>> hashCode =
      new Function<String, MaybeMonad<Integer>>() {
        @Override public MaybeMonad<Integer> apply(String arg) {
          return MaybeMonad.instance(arg.hashCode());
        }
      };

  static Function<Integer, MaybeMonad<Object>> maybeFail = new Function<Integer, MaybeMonad<Object>>() {
    @Override public MaybeMonad<Object> apply(Integer arg) {
      throw new RuntimeException();
    }
  };

  public static Either<UnsupportedEncodingException, String> encode2(String str) {
    try {
      return Right.instance(URLEncoder.encode(str, "utf-8"));
    } catch (UnsupportedEncodingException e) {
      return Left.instance(e);
    }
  }

  static Function<String, String> toUpper = new Function<String, String>() {
    public String apply(String arg) {
      return arg.toUpperCase();
    }
  };

  static Function<Integer, StateMonad<Integer, String>> increment =
      new Function<Integer, StateMonad<Integer, String>>() {
        @Override public StateMonad<Integer, String> apply(final Integer arg) {
          return new StateMonad<Integer, String>() {
            @Override protected ValueWithState<Integer, String> getValueWithState(String s) {
              return new ValueWithState<Integer, String>(arg + 1, s + "\n Increment");
            }
          };
        }
      };

  static Function<Integer, StateMonad<Integer, String>> multiply =
      new Function<Integer, StateMonad<Integer, String>>() {
        @Override public StateMonad<Integer, String> apply(final Integer arg) {
          return new StateMonad<Integer, String>() {
            @Override protected ValueWithState<Integer, String> getValueWithState(String s) {
              return new ValueWithState<Integer, String>(arg * 3, s + "\n Multiply");
            }
          };
        }
      };



  public static void main(String... args) {
    ListMonad comprehension =
        (ListMonad) ListMonad.instance(
            Arrays.asList(1, 2, 3, 4)).bind(repeatNumber).map(new Function<Integer, Integer>() {
          @Override public Integer apply(Integer arg) {
            return arg * 2;
          }
        });//.bind(listFail);
    System.out.println(comprehension);

    separator();

    MaybeMonad failSafe = MaybeMonad.instance("Frankie").bind(hashCode);//.bind(maybeFail);

    System.out.println(failSafe);

    separator();

    Object encoding = encode2("will always succeed").left().map(new Function<UnsupportedEncodingException, Object>() {
      @Override public Object apply(UnsupportedEncodingException arg) {
        return arg.getMessage();
      }
    }).get();

    System.out.println(encoding);

    separator();

    ValueWithState<Integer, Object> valueWithState = StateMonad.instance(2)
        .bind(increment)
        .bind(multiply)
        .getValueWithState("First state\n");

    System.out.println(valueWithState.getValue());
    System.out.println(valueWithState.getState());
  }

  private static void separator() {
    System.out.println("\n-----------\n");
  }
}
