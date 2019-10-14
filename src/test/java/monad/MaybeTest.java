package monad;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Random;

import static monad.Getter.getter;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MaybeTest {
    public static final MathContext ROUND_3 = new MathContext(3, RoundingMode.HALF_EVEN);

    @SuppressWarnings("unchecked")
    public static <T, U> Function<T, MaybeMonad<U>> mgetter(final String property) {
        return new Function<T, MaybeMonad<U>>() {
            public MaybeMonad<U> apply(T bean) {
                return (MaybeMonad<U>) MaybeMonad.instance(getter(property).apply(bean));
            }
        };
    }

    @Test
    void bankBranchContactInfo() {
        Function<Bank, MaybeMonad<Branch>> getBranch = mgetter("branch");
        Function<Branch, MaybeMonad<ContactInfo>> getContactInfo = mgetter("contactInfo");
        Function<ContactInfo, MaybeMonad<String>> getEmail = mgetter("email");

        Bank bank = new Bank(new Branch(new ContactInfo("test@example.com")));

        MaybeMonad<String> email =
                MaybeMonad.instance(bank).bind(getBranch).bind(getContactInfo).bind(getEmail);

        assertEquals("test@example.com", email.get());
    }

    @Test
    void reciprocal() {
        Function<Integer, BigDecimal> reciprocal = new Function<Integer, BigDecimal>() {
            public BigDecimal apply(Integer num) {
                return new BigDecimal(1).divide(new BigDecimal(num), 4, RoundingMode.HALF_UP);
            }
        };

        Random random = new Random();
        int num = random.nextInt(10);
        BigDecimal result;
        try {
            result = reciprocal.apply(num);
        } catch (ArithmeticException e) {
            result = new java.math.BigDecimal(0); // undefined value = 0 ???
        }
        BigDecimal computeOriginal = result.multiply(BigDecimal.valueOf(num)).round(ROUND_3);
        assertEquals(0, computeOriginal.compareTo(BigDecimal.ONE), "1/"+ num);
    }
}
