package monad;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListTest {
    @Test
    void encrypt() {
        Function<String, ListMonad<Character>> strToChars =
                new Function<String, ListMonad<Character>>() {
                    public ListMonad<Character> apply(String str) {
                        ListMonad<Character> chars = new ListMonad<Character>();
                        for (char ch : str.toCharArray()) {
                            chars.add(ch);
                        }
                        return chars;
                    }
                };

        Function<Character, String> encrypt = new Function<Character, String>() {
            public String apply(Character ch) {
                int intval = ((int) ch) - 32;
                int encrypted =
                        new BigDecimal(intval)
                                .pow(7).remainder(new BigDecimal(97)).intValue();
                return String.format("%02d", encrypted);
            }
        };

        Operator<String, String> concat = new Operator<String, String>() {
            public String apply(String str1, String str2) {
                return str1 + str2;
            }
        };

        String passPhrase = "The list monad rocks!";

        ListMonad<String> instance = ListMonad.instance(passPhrase);
        ListMonad<Character> listChar = instance.bind(strToChars);
        Monad<ListMonad<?>, String> listString = (Monad<ListMonad<?>, String>) listChar.map(encrypt);
        ListMonad<String> encryptedChars = ListMonad.safeCast(listString);
        String encrypted = encryptedChars.reduceLeft(concat);

        assertEquals("634920008743820700692742116000142746228201", encrypted);
    }

    @Test
    void decrypt() {
        Function<String, ListMonad<String>> encryptedChars =
                new Function<String, ListMonad<String>>() {
                    public ListMonad<String> apply(String str) {
                        return ListMonad.instance(Arrays.asList(str.split("(?<=\\G.{2})")));
                    }
                };

        Function<String, Character> decrypt = new Function<String, Character>() {
            public Character apply(String str) {
                int decrypted =
                        new BigDecimal(Integer.parseInt(str))
                                .pow(55).remainder(new BigDecimal(97)).intValue();
                return Character.toChars(decrypted + 32)[0];
            }
        };

        Operator<Character, String> concat = new Operator<Character, String>() {
            public String apply(String str, Character ch) {
                return str + ch;
            }
        };

        String encrypted = "634920008743820700692742116000142746228201";

        ListMonad<String> listString = ListMonad.instance(encrypted).bind(encryptedChars);
        Monad<ListMonad<?>, Character> chars = (Monad<ListMonad<?>, Character>) listString.map(decrypt);
        ListMonad<Character> decryptedChars = ListMonad.safeCast(chars);
        String passPhrase = decryptedChars.foldLeft("", concat);

        assertEquals("The list monad rocks!", passPhrase);
    }

    @Test
    void increment() {
        Function<Integer, Integer> increment = new Function<Integer, Integer>() {
            public Integer apply(Integer num) {
                return num + 1;
            }
        };

        List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5);

        Monad<ListMonad<?>, Integer> ints = (Monad<ListMonad<?>, Integer>) ListMonad.instance(nums).map(increment);
        ListMonad<Integer> incremented = ListMonad.safeCast(ints);

        assertEquals("[2, 3, 4, 5, 6]", incremented.toString());
    }

    @Test
    void sum() {
        Operator<Integer, Integer> plus = new Operator<Integer, Integer>() {
            public Integer apply(Integer int1, Integer int2) {
                return int1 + int2;
            }
        };

        List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5);

        int sum = ListMonad.instance(nums).reduceLeft(plus);
        assertEquals(15, sum);
    }

    @Test
    void namesOfUsersWithEmail() {
        Function<User, Boolean> hasEmail = new Function<User, Boolean>() {
            public Boolean apply(User user) {
                return user.getEmail() != null;
            }
        };

        Function<User, String> getName = new Function<User, String>() {
            public String apply(User user) {
                return user.getName();
            }
        };

        List<User> users = Arrays.asList(
                new User("Jack", "jack@acme.com"),
                new User("Scott"),
                new User("Jill", "jill@acme.com"));

        Monad<ListMonad<?>, String> strings = (Monad<ListMonad<?>, String>) ListMonad.instance(users).filter(hasEmail).map(getName);
        ListMonad<String> names = ListMonad.safeCast(strings);

        assertEquals("[Jack, Jill]", names.toString());
    }

    @Test
    void failure() {
        Function<Integer, Integer> increment = new Function<Integer, Integer>() {
            public Integer apply(Integer arg) {
                return arg + 1;
            }
        };

        // list with null entry
        List<Integer> list2 = Arrays.asList(1, 2, null, 4, 5);

        Monad<ListMonad<?>, Integer> ints = (Monad<ListMonad<?>, Integer>) ListMonad.instance(list2).map(increment);
        ListMonad<Integer> result = ListMonad.safeCast(ints);
        assertEquals("[]", result.toString());
    }
}
