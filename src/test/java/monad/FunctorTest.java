package monad;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FunctorTest {
    public static class Box<T> implements Functor<Box<T>, T> {
        private T value;

        public Box(T value) {
            this.value = value;
        }

        @Override
        public <U> Function<Box<T>, Box<U>> fmap(final Function<T, U> f) {
            return new Function<Box<T>, Box<U>>() {
                public Box<U> apply(Box<T> box) {
                    return new Box<U>(f.apply(box.value));
                }
            };
        }

        @Override
        public <U> U get() {
            return (U) value;
        }
    }

    @Test
    public void functor() {
        // create an increment function
        Function<Integer, Integer> increment = new Function<Integer, Integer>() {
            public Integer apply(Integer num) {
                return num + 1;
            }
        };

        // create a box with 1 in it
        Box<Integer> box1 = new Box<Integer>(1);

        // create a box that has the incremented value of box1 in it
        Box<Integer> box2 = box1.fmap(increment).apply(box1);

        assertEquals(2, box2.value);
    }
}
