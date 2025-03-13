package org.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberUtil {

    public static String getDecimalText(String number) {
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

    public static int getIntegerNumber(String number){
        return Integer.parseInt(number);
    }

    public static double getDecimalNumber(String decimal){
        return Double.parseDouble(decimal);
    }
}

