package monad;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

public class Getter {
    @SuppressWarnings("unchecked")
    public static <T, U> Function<T, U> getter(final String property) {
        return new Function<T, U>() {
            public U apply(T bean) {
                try {
                    Method getter = new PropertyDescriptor(property, bean.getClass()).getReadMethod();
                    return (U) getter.invoke(bean);
                } catch (Exception e) {
                    return null;
                }
            }
        };
    }
}
