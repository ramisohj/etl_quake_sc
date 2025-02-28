package org.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilNumber {

    public static String getDecimalNumber(String number) {
        number = number.replace(";", ".");
        number = number.replaceAll("[()]", "");
        number = number.replaceAll("[a-zA-Z]", "");
        number = number.replaceAll("\\s", "");

        Pattern pattern = Pattern.compile("\\d+\\.\\d+");
        Matcher matcher = pattern.matcher(number);

        if (matcher.find()) {
            number = matcher.group();
        }

        return number.trim();
    }
}
