package com.alarm.scheduler.db.model;

import com.alarm.scheduler.db.contract.AlarmContract;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by milossimic on 12/19/17.
 */

@DatabaseTable(tableName = AlarmContract.ALARM_TABLE)
public class Alarm {

    //constants
    public static final int WEEKLY_ALARM = 1000;      //interval of 7 days Monday 1st to Monday 8th
    public static final int EVERY_DAY_ALARM = 2000; //interval of 1 day including Saturday and Sunday
    public static final int WEEKDAY_ALARM = 3000;   //interval of 1 day exclude Saturday and Sunday

    @DatabaseField(generatedId = true, columnName = AlarmContract.ALARM_ID)
    private int id;

    @DatabaseField(columnName = AlarmContract.ALARM_ACTIVE, canBeNull = false)
    private boolean active;

    @DatabaseField(columnName = AlarmContract.ALARM_TIME, canBeNull = false)
    private long when;

    @DatabaseField(columnName = AlarmContract.ALARM_SOUND)
    private String sound;

    @DatabaseField(columnName = AlarmContract.ALARM_MESSAGE)
    private String message;

    @DatabaseField(columnName = AlarmContract.ALARM_TYPE, canBeNull = false)
    private int type;

    @DatabaseField(columnName = AlarmContract.ALARM_SNOOZE)
    private long snooze;

    public Alarm() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public long getWhen() {
        return when;
    }

    public void setWhen(long when) {
        this.when = when;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getSnooze() {
        return snooze;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSnooze(long snooze) {
        this.snooze = snooze;
    }
}