package org.dvsa.testing.framework.Utils.common;

import activesupport.number.Int;
import activesupport.string.Str;
import activesupport.system.Properties;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.IntStream;

public class RandomUtils {

    public static String email() {
        return String.format("automated.test.%s.%s.%s@dvsa.co.uk", Properties.get("env"), Properties.get("user.name").toLowerCase(), Str.randomWord(3, 10));
    }

    public static String birthDate(){
        return String.format(
                "%d-%d-%d",
                Int.random(1900, 2018), Int.random(1, 12), Int.random(1, 28)
        );
    }

    public static String number(int length) {
        StringBuilder number = new StringBuilder(length);

        IntStream
                .rangeClosed(1, length)
                .forEach(i -> number.append(Int.random(0, 9)));

        return number.toString();
    }

    public static String letter(@NotNull String subject) {
        int index = Int.random(0, subject.length() - 1);
        return String.valueOf(subject.charAt(index));
    }

    public static <T> T itemFromCollection(List<T> collection) {
        int index = Int.random(0, collection.size() - 1);
        return collection.get(index);
    }

}
