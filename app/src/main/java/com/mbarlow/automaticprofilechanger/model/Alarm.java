package com.mbarlow.automaticprofilechanger.model;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
import android.util.Pair;
// KEEP INCLUDES END


/**
 * Entity mapped to table "ALARM".
 */
public class Alarm {

    private Long id;
    private String name;
    private Byte enabled;
    private Integer startTime;
    private Integer endTime;

    // KEEP FIELDS - put your custom fields here
    private static final Pair[] dayMaskPairs = new Pair[]{
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

    public Alarm(Long id, String name, Byte enabled, Integer startTime, Integer endTime) {
        this.id = id;
        this.name = name;
        this.enabled = enabled;
        this.startTime = startTime;
        this.endTime = endTime;
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

    public Byte getEnabled() {
        return enabled;
    }

    public void setEnabled(Byte enabled) {
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

    // KEEP METHODS - put your custom methods here
    public boolean isEnabled(){
        return (enabled & 0x80) != 0;
    }

    public String getDaysEnabledString() {
        StringBuilder stringBuilder = new StringBuilder();
        boolean isFirst = true;
        for (Pair<String, Integer> dayMaskPair : dayMaskPairs){
            if ((enabled & dayMaskPair.second) != 0){
                if (!isFirst){
                    stringBuilder.append(", ");
                    isFirst = false;
                }
                stringBuilder.append(dayMaskPair.first);
            }
        }
        return stringBuilder.toString();
    }
    // KEEP METHODS END

}
