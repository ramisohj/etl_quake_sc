package org.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static Date getDateSimpleFormat(String date) throws ParseException {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = date.trim();
        return inputFormat.parse(date);
    }

    public static String getDateStringFormat(String date) throws ParseException {
        Date newDate = getDateSimpleFormat(date);
        return newDate.toString();
    }

}
