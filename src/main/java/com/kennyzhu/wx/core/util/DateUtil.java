package com.kennyzhu.wx.core.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.DAY_OF_YEAR;

/**
 * User: KennyZhu
 * Date: 16/7/2
 * Desc:
 */
public final class DateUtil {
    private DateUtil() {
    }

    public static Date getDay(int specify) {
        return getDay(new Date(), specify);
    }

    public static final String getDayStr(int specify) {
        Date day = getDay(specify);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(day);
    }

    public static void main(String[] args) {
        System.out.println(getDay(getMondayOfThisWeek(), -1));
        System.out.println(getSundayOfThisWeek());
    }

    /**
     * @param current
     * @param specify
     * @return
     */
    public static Date getDay(Date current, int specify) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(current);
        calendar.set(DAY_OF_YEAR, specify);
        return calendar.getTime();
    }

    /**
     * 得到本周周一
     *
     * @return yyyy-MM-dd
     */
    public static Date getMondayOfThisWeek() {
        Calendar c = Calendar.getInstance();
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        c.add(Calendar.DATE, -day_of_week + 1);
        return c.getTime();
    }


    /**
     * 得到本周周日
     *
     * @return yyyy-MM-dd
     */
    public static Date getSundayOfThisWeek() {
        Calendar c = Calendar.getInstance();
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        c.add(Calendar.DATE, -day_of_week + 7);
        return c.getTime();
    }


}
