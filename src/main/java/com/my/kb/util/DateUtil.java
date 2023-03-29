package com.my.kb.util;

import java.util.Date;

public final class DateUtil {

    private DateUtil() {
    }

    public static String getDate() {
        Date date = new Date();
        return date.toString();
    }
}
