package spring;

import java.util.Calendar;

public class Utils {
    public static long getMinutes(Calendar date) {
        return (date.getTime().getTime()/1000)/60;
    }
}
