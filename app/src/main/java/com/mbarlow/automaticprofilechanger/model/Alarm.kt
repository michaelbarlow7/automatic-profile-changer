package com.mbarlow.automaticprofilechanger.model

/**
 * Created by michael on 25/04/16.
 */
data class Alarm(
        val name: String,
        val enabled: Boolean,
        val enabledMonday: Boolean,
        val enabledTuesday: Boolean,
        val enabledWednesday: Boolean,
        val enabledThursday: Boolean,
        val enabledFriday: Boolean,
        val enabledSaturday: Boolean,
        val enabledSunday: Boolean,
        val startTime: String, //TODO: Use a numerical value (0 - 1439)
        val endTime: String //TODO: Use a numerical value (0 - 1439)
// I guess it's easier if this is the amount of minutes it takes to expire
// cos it deals with rolling over onto the next day.
) {
}