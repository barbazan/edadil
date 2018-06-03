package com.barbazan.edadil.utils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Dmitry Malyshev
 * Date: 20.11.13 Time: 18:22
 * Email: dmitry.malyshev@gmail.com
 */
public class Time implements Comparable {

    public static final String DATE_PATTEN = "dd MMM yyyy";
    public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd MMM HH:mm", new Locale("RU"));
    public static SimpleDateFormat DATE_FORMAT_2 = new SimpleDateFormat(DATE_PATTEN, new Locale("RU"));
    public static SimpleDateFormat DATE_FORMAT_3 = new SimpleDateFormat("dd MMM yyyy [HH:mm]", new Locale("RU"));

    public static final long SECOND_MILLIS = 1000;
    public static final long MINUTE_MILLIS = 60 * SECOND_MILLIS;
    public static final long HOUR_MILLIS = 60 * MINUTE_MILLIS;
    public static final long DAY_MILLIS = 24 * HOUR_MILLIS;
    public static final long WEEK_MILLIS = 7 * DAY_MILLIS;
    public static final long MONTH_MILLIS = 30 * DAY_MILLIS;
    public static final long YEAR_MILLIS = 365 * DAY_MILLIS;

    private long hours;
    private long minutes;
    private long seconds;
    private boolean isLongFormat;

    public Time() {
    }

    public Time(String timeStr) {
        StringTokenizer st = new StringTokenizer(timeStr, ":");
        setHours(Integer.parseInt(st.nextToken()));
        setMinutes(Integer.parseInt(st.nextToken()));
        setSeconds(Integer.parseInt(st.nextToken()));
    }

    public Time(Date date) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);
        setHours(gregorianCalendar.get(GregorianCalendar.HOUR_OF_DAY));
        setMinutes(gregorianCalendar.get(GregorianCalendar.MINUTE));
        setSeconds(gregorianCalendar.get(GregorianCalendar.SECOND));
    }

    public Time(long millis) {
        long seconds = millis / 1000;
        setHours(seconds / 3600);
        seconds %= 3600;
        setMinutes(seconds / 60);
        setSeconds(seconds % 60);
    }

    public Time(long millis, boolean isLongFormat) {
        this(millis);
        this.isLongFormat = isLongFormat;
    }

    public Time(long hours, long minutes, long seconds) {
        setHours(hours);
        setMinutes(minutes);
        setSeconds(seconds);
    }

    /**
     * Возвращает ближайший момент времени, кратный (относительно начала суток) указанному временному интервалу
     */
    public static long getNextTimeAdjusted(long from, long interval) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(from);
        long dayTime = getDayTime(gregorianCalendar);
        long time = from - dayTime;
        while (time < from) {
            time += interval;
        }
        return time;
    }

    public static long getNextTimeAdjusted(long interval) {
        return getNextTimeAdjusted(System.currentTimeMillis(), interval);
    }

    public static long getDayTime(GregorianCalendar gregorianCalendar) {
        return gregorianCalendar.get(GregorianCalendar.HOUR_OF_DAY) * 3600000
                + gregorianCalendar.get(GregorianCalendar.MINUTE) * 60000
                + gregorianCalendar.get(GregorianCalendar.SECOND) * 1000
                + gregorianCalendar.get(GregorianCalendar.MILLISECOND);
    }


    public long getWeeks() {
        return toMillis() / WEEK_MILLIS;
    }

    public long getDays() {
        return toMillis() / DAY_MILLIS;
    }

    public long getHours() {
        return hours;
    }

    public void setHours(long hours) {
        if (hours < 0) {
            hours = 0;
        }
        this.hours = hours;
    }

    public long getMinutes() {
        return minutes;
    }

    public void setMinutes(long minutes) {
        if (minutes < 0 || minutes > 59) {
            minutes = 0;
        }
        this.minutes = minutes;
    }

    public long getSeconds() {
        return seconds;
    }

    public void setSeconds(long seconds) {
        if (seconds < 0 || seconds > 59) {
            seconds = 0;
        }
        this.seconds = seconds;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Time)) return false;
        Time time = (Time) o;
        return hours == time.hours && minutes == time.minutes && seconds == time.seconds;
    }

    public int hashCode() {
        long result;
        result = hours;
        result = 29 * result + minutes;
        result = 29 * result + seconds;
        return new Long(result).hashCode();
    }

    public String toString() {
        return expand(hours) + ":" + expand(minutes) + ":" + expand(seconds);
    }

    public String toStringWithoutSeconds() {
        return expand(hours) + ":" + expand(minutes);
    }

    public String toStringWithoutHours() {
        return expand(minutes) + ":" + expand(seconds);
    }

    public String toStringInVeryShortFormat() {
        long millis = toMillis();

        if(millis < 1000) {
            return "0:0";
        }

        Date date = new Date();
        date.setTime(millis);

        if (millis > HOUR_MILLIS) {
            long hours = millis / HOUR_MILLIS;
            return hours +":"+ new SimpleDateFormat("mm:ss").format(date);
        } else {
            return new SimpleDateFormat("m:ss").format(date);
        }
    }

    public long toMillis() {
        return toSeconds() * 1000;
    }

    public long toSeconds() {
        return hours * 3600 + minutes * 60 + seconds;
    }

    public int compareTo(Object o) {
        Time time = (Time) o;
        return Long.signum(toSeconds() - time.toSeconds());
    }

    private static String expand(long i) {
        String s = String.valueOf(i);
        while (s.length() < 2) {
            s = "0" + s;
        }
        return s;
    }

    public String toStringInHumanFormat() {
        return toStringInHumanFormat(DAY_MILLIS);
    }

    public String toStringInHumanFormat(long limit) {
        double millis = Math.max(1000, toMillis());
        if(millis >= DAY_MILLIS && limit >= DAY_MILLIS) {
            int days = (int)Math.round(millis / DAY_MILLIS);
            return days + " " + getDaysName(days);
        } else if(millis >= HOUR_MILLIS && limit >= HOUR_MILLIS) {
            int hours = (int)Math.round(millis / HOUR_MILLIS);
            return hours + " " + getHoursName(hours);
        } else if(millis >= MINUTE_MILLIS && limit >= MINUTE_MILLIS) {
            int minutes = (int)Math.round(millis / MINUTE_MILLIS);
            return minutes + " " + getMinutesName(minutes);
        } else {
            int seconds = (int)Math.round(millis / 1000);
            return seconds + " " + getSecondsName(seconds);
        }
    }

    private String toStringDAYS(double millis) {
        int days = (int)Math.floor(millis / DAY_MILLIS);
        int hours = (int)Math.round((millis - days * DAY_MILLIS)  / HOUR_MILLIS);
        if (hours == 24) {
            days += 1;
            hours = 0;
        }
        return days + " " + getDaysName(days) + (hours == 0 ? "" : " " + hours + " " + getHoursName(hours));
    }

    private String toStringHOURS(double millis) {
        int hours = (int)Math.floor(millis / HOUR_MILLIS);
        int minutes = (int)Math.round((millis - hours * HOUR_MILLIS) / MINUTE_MILLIS);
        if (minutes == 60) {
            hours += 1;
            minutes = 0;
        }
        if (hours == 24) {
            return toStringDAYS(millis);
        }
        return hours + " " + getHoursName(hours) + (minutes == 0 ? "" : " " + minutes + " " + getMinutesName(minutes));
    }

    private String toStringMINUTES(double millis) {
        int minutes = (int)Math.floor(millis / MINUTE_MILLIS);
        int seconds = (int)Math.round((millis - minutes * MINUTE_MILLIS) / SECOND_MILLIS);
        if (seconds == 60) {
            minutes += 1;
            seconds = 0;
        }
        if (minutes == 60) {
            return toStringHOURS(millis);
        }
        return minutes + " " + getMinutesName(minutes) + (seconds == 0 ? "" : " " + seconds + " " + getSecondsName(seconds));
    }

    private String toStringSECONDS(double millis) {
        int seconds = (int)Math.round(millis / 1000);
        if (seconds == 60) {
            return toStringMINUTES(millis);
        }
        return seconds + " " + getSecondsName(seconds);
    }

    public String toStringInHumanFormat2() {
        double millis = Math.max(1000, toMillis());
        if(millis >= DAY_MILLIS) {
            return toStringDAYS(millis);
        } else if(millis >= HOUR_MILLIS) {
            return toStringHOURS(millis);
        } else if(millis >= MINUTE_MILLIS) {
            return toStringMINUTES(millis);
        } else {
            return toStringSECONDS(millis);
        }
    }

    public static String getNumericWord(int value, String wordFor1, String wordFor2, String wordFor5) {
        int mod = value % 10;
        int mod2 = value % 100;
        if(mod2 >= 10 && mod2 <= 20) {
            return wordFor5;
        }
        switch (mod) {
            case 1: return wordFor1;
            case 2:
            case 3:
            case 4: return wordFor2;
            default: return wordFor5;
        }
    }

    public static boolean isSunday() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int dayOfWeek = calendar.get(GregorianCalendar.DAY_OF_WEEK);
        return dayOfWeek == 1;
    }

    public static int getDayOfWeek() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return calendar.get(GregorianCalendar.DAY_OF_WEEK);
    }

    public static String getDayOfWeekName(int value) {
        switch (value) {
            case 1: return "воскресенье";
            case 2: return "понедельник";
            case 3: return "вторник";
            case 4: return "среда";
            case 5: return "четверг";
            case 6: return "пятница";
            case 7: return "суббота";
        }
        return "";
    }

    private String getDaysName(int value) {
        return isLongFormat ? getNumericWord(value, "день", "дня", "дней") : "д";
    }

    private String getHoursName(int value) {
        return isLongFormat ? getNumericWord(value, "час", "часа", "часов") : "ч";
    }

    private String getMinutesName(int value) {
        return isLongFormat ? getNumericWord(value, "минуту", "минуты", "минут") : "м";
    }

    private String getSecondsName(int value) {
        return isLongFormat ? getNumericWord(value, "секунду", "секунды", "секунд") : "с";
    }

    public static boolean isBirthDayNow(Date birthDay) {
        if (birthDay != null ) {
            GregorianCalendar now = new GregorianCalendar();
            now.setTime(new Date());
            GregorianCalendar bd = new GregorianCalendar();
            bd.setTime(birthDay);
            return  now.get(GregorianCalendar.DAY_OF_MONTH) == bd.get(GregorianCalendar.DAY_OF_MONTH)
                    && now.get(GregorianCalendar.MONTH) == bd.get(GregorianCalendar.MONTH);

        } else {
            return false;
        }
    }

    public static void main(String args[]) {
        System.err.println(new Time("123:56:58"));
        System.err.println(new Time(new Time(111, 23, 3).toMillis()));
    }

    public static boolean isToday(Date time) {
        if(time == null) {
            return false;
        }
        GregorianCalendar calendar1 = new GregorianCalendar();
        GregorianCalendar calendar2 = new GregorianCalendar();
        calendar1.setTime(time);
        calendar2.setTimeInMillis(System.currentTimeMillis());
        return calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR);
    }

    public static boolean isYesterday(Date time) {
        if(time == null) {
            return false;
        }
        GregorianCalendar calendar1 = new GregorianCalendar();
        GregorianCalendar calendar2 = new GregorianCalendar();
        calendar1.setTime(time);
        calendar2.setTimeInMillis(System.currentTimeMillis());
        int day1 = calendar1.get(Calendar.DAY_OF_YEAR);
        int day2 = calendar2.get(Calendar.DAY_OF_YEAR);
        boolean is1JanAnd31Dec = calendar1.getActualMaximum(Calendar.DAY_OF_YEAR) == day1 && day2 == 1;
        return day1 + 1 == day2 || is1JanAnd31Dec;
    }

    public static String getTimeDiff(Date time) {
        return time == null ? "00:00" : new Time(
                time.getTime() < System.currentTimeMillis() ? 0 : time.getTime() - System.currentTimeMillis())
                .toStringInHumanFormat2();
    }

    public boolean isLongFormat() {
        return isLongFormat;
    }

    public void setLongFormat(boolean longFormat) {
        isLongFormat = longFormat;
    }
}
