package j.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    private static final String format = "yyyy-MM-dd HH:mm:ss";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(format);
    public static Date stringToDate(String date) throws ParseException {
        return dateFormat.parse(date);
    }
    public static String dateToString(Date date) {
        return dateFormat.format(date);
    }
}
