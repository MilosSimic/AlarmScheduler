package com.alarm.scheduler.tools;

import com.alarm.scheduler.db.model.Alarm;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.alarm.scheduler.db.model.Alarm.EVERY_DAY_ALARM;
import static com.alarm.scheduler.db.model.Alarm.WEEKDAY_ALARM;

/**
 * Created by milossimic on 12/19/17.
 */

public class AlarmTools {

    public static final int NONE = -1;

    private static final DateFormat TWENTY_FOUR_TF = new SimpleDateFormat("HH:mm", Locale.US);
    private static final  SimpleDateFormat showFormat = new SimpleDateFormat("hh:mm aa", Locale.US);
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);

    public static String convert(Date time) {
        return TWENTY_FOUR_TF.format(time);
    }

    public static int dayToCalendar(String dayName) {
        switch (dayName) {
            case "Monday":
                return Calendar.MONDAY;
            case "Tuesday":
                return Calendar.TUESDAY;
            case "Wednesday":
                return Calendar.WEDNESDAY;
            case "Thursday":
                return Calendar.THURSDAY;
            case "Friday":
                return Calendar.FRIDAY;
            case "Saturday":
                return Calendar.SATURDAY;
            case "Sunday":
                return Calendar.SUNDAY;
            case "Every day":
                return EVERY_DAY_ALARM;
            case "Every weekday":
                return WEEKDAY_ALARM;
            default:
                return NONE;
        }
    }

    public static String getDay(Calendar when) {
        return when.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US);
    }

    public static String generateShow(Date date){
        return showFormat.format(date);
    }

    public static String nextAlarmText(long next, Alarm alarm){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(next);

        return String.format(Locale.US,"Alarm %d at %s", alarm.getId(), sdf.format(c.getTime()));
    }
}
