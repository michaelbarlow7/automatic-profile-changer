package com.mbarlow.automaticprofilechanger.model;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
import android.util.Pair;
// KEEP INCLUDES END
/**
 * Entity mapped to table "ALARM".
 */
public class Alarm implements java.io.Serializable {

    private Long id;
    private String name;
    private byte enabled;
    private Integer startTime;
    private Integer endTime;
    /** Not-null value. */
    private String profile;

    // KEEP FIELDS - put your custom fields here
    public static final Pair[] dayMaskPairs = new Pair[]{
            new Pair<>("Sun", 0x40),
            new Pair<>("Mon", 0x20),
            new Pair<>("Tue", 0x10),
            new Pair<>("Wed", 0x08),
            new Pair<>("Thu", 0x04),
            new Pair<>("Fri", 0x02),
            new Pair<>("Sat", 0x01)
    };
    // KEEP FIELDS END

    public Alarm() {
    }

    public Alarm(Long id) {
        this.id = id;
    }

    public Alarm(Long id, String name, byte enabled, Integer startTime, Integer endTime, String profile) {
        this.id = id;
        this.name = name;
        this.enabled = enabled;
        this.startTime = startTime;
        this.endTime = endTime;
        this.profile = profile;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getEnabled() {
        return enabled;
    }

    public void setEnabled(byte enabled) {
        this.enabled = enabled;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    /** Not-null value. */
    public String getProfile() {
        return profile;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setProfile(String profile) {
        this.profile = profile;
    }

    // KEEP METHODS - put your custom methods here
    public boolean isEnabled(){
        return (enabled & 0x80) != 0;
    }

    public void setIsEnabled(boolean isEnabled){
        if (isEnabled){
            enabled |= 0x80;
        }else{
            enabled &= 0x7F;
        }
    }

    public String getDaysEnabledString() {
        StringBuilder stringBuilder = new StringBuilder();
        boolean isFirst = true;
        for (Pair<String, Integer> dayMaskPair : dayMaskPairs){
            if ((enabled & dayMaskPair.second) != 0){
                if (isFirst){
                    isFirst = false;
                }else{
                    stringBuilder.append(", ");
                }
                stringBuilder.append(dayMaskPair.first);
            }
        }
        return stringBuilder.toString();
    }

    private String getTimeString(Integer time){
        if (time == null){
            return null;
        }
        if (time < 0 || time > 1439){
            //ERROR: time is out of range
            return Integer.toString(time);
        }
        String ampm = "am";
        int hours = time / 60;
        if (hours > 12){
            hours -= 12;
            ampm = "pm";
        }
        int mins = time % 60;
        return String.format("%d:%02d %s", hours, mins, ampm);
    }

    public int getStartTimeHours(){
        return startTime == null ? 0 : startTime / 60;
    }

    public int getStartTimeMinutes(){
        return startTime == null ? 0 : startTime % 60;
    }

    public int getEndTimeHours(){
        return endTime == null ? 0 : endTime/ 60;
    }

    public int getEndTimeMinutes(){
        return endTime == null ? 0 : endTime % 60;
    }

    public String getStartTimeString(){
        return getTimeString(startTime);
    }

    public String getEndTimeString(){
        return getTimeString(endTime);
    }

    private int convertHoursAndMinutes(int hour, int minute){
        return (hour * 60) + minute;
    }

    public void setStartTime(int hour, int minute){
        startTime = convertHoursAndMinutes(hour, minute);
    }

    public void setEndTime(int hour, int minute){
        endTime = convertHoursAndMinutes(hour, minute);
    }

    public boolean isDayAtIndexEnabled(int index){
        int mask = (int) dayMaskPairs[index].second;
        return (mask & enabled) != 0;
    }

    public void setDayAtIndexEnabled(int index, boolean isEnabled){
        int mask = (int) dayMaskPairs[index].second;
        if (isEnabled){
            enabled = (byte) (enabled | mask);
        }else{
            enabled = (byte) (enabled & ~mask);
        }
    }
    // KEEP METHODS END

}
