package monad;

import org.junit.jupiter.api.Test;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import static monad.Getter.getter;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FunctionTest {
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
