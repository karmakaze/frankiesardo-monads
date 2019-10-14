package monad;

import org.junit.jupiter.api.Test;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FunctionTest {
    public static class ContactInfo {
        String email;

        ContactInfo(String email) {
            this.email = email;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

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

    /* Sample Usage */
    @Test
    void applyGetter() {
        // Create a ContactInfo.email property getter function
        Function<ContactInfo, String> getEmail = getter("email");

        ContactInfo contactInfo = new ContactInfo("test@example.com");
        // Apply the getter function to a contactInfo bean to get email address
        String email = getEmail.apply(contactInfo);

        assertEquals("test@example.com", email);
    }
}
