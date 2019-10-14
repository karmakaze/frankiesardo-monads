package monad;

import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EitherTest {
    public String encode1(String str) throws UnsupportedEncodingException {
        return URLEncoder.encode(str, "utf-8");
    }

    public Either<UnsupportedEncodingException, String> encode2(String str) {
        try {
            return Right.instance(URLEncoder.encode(str, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            return Left.instance(e);
        }
    }

    Function<String, String> toUpper = new Function<String, String>() {
        public String apply(String arg) {
            return arg.toUpperCase();
        }
    };

    @Test
    void either() {
        // caller of encode1 has to ignore an exception that will never happen
        String encoded = null;
        try {
            encoded = encode1("will always succeed");
            encoded = toUpper.apply(encoded);
        } catch (UnsupportedEncodingException ignore) {
            // will never happen
        }
        assertEquals("WILL+ALWAYS+SUCCEED", encoded);

        // caller of encode2 can just get on with the job
        encoded = encode2("will always succeed").right().map(toUpper).get();
        assertEquals("WILL+ALWAYS+SUCCEED", encoded);
    }
}
