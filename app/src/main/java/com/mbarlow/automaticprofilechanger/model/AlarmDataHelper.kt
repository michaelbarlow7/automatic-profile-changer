package com.mbarlow.automaticprofilechanger.model

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.mbarlow.automaticprofilechanger.AutomaticProfileChangerApplication
import com.mbarlow.automaticprofilechanger.receiver.AlarmBroadcastReceiver
import de.greenrobot.dao.query.WhereCondition
import java.util.*

/**
 * Created by michael on 9/05/16.
 */
class AlarmDataHelper(val application: AutomaticProfileChangerApplication) {

    fun getIntent() : Intent {
        return Intent(application, AlarmBroadcastReceiver::class.java)
    }

    fun resetAlarm(){
        val sharedPreferences = application.getSharedPreferences("END_ALARM_PREFERENCE", 0)
        if (sharedPreferences.contains("existingProfile")){
            // We're already in an alarm period, don't mess with it
            return
        }

        val intent = getIntent()
        val pendingIntent = PendingIntent.getBroadcast(application, 0, intent, 0)
        val alarmManager = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)

        findAndSetNextAlarm()
    }

    fun findAndSetNextAlarm(){
        val daoSession = application.daoSession
        val currentCalendar = Calendar.getInstance()
        val today = currentCalendar.get(Calendar.DAY_OF_WEEK) - 1 // 0-indexing it
        var currentTime = currentCalendar.get(Calendar.HOUR_OF_DAY) * 60 + currentCalendar.get(Calendar.MINUTE)

        //TODO: If the startTime for an alarm is the current time, set the profile to that start and set the
        // alarm for the end
        var day = today
        var nextAlarm : Alarm? = null
        do {
            val dayMask = (Alarm.dayMaskPairs[day].second as Int or 0x80) // Alarm must be enabled
            val qb = daoSession.alarmDao.queryBuilder()
            qb.where(WhereCondition.StringCondition("ENABLED & $dayMask == $dayMask"),AlarmDao.Properties.StartTime.gt(currentTime))
            qb.orderAsc(AlarmDao.Properties.StartTime)

            val list = qb.list()
            if (list != null && !list.isEmpty()){
                nextAlarm = list[0]
                break
            }

            currentTime = 0 // On following days we want to find the soonest time starting from midnight
            day++
            day %= 7
        } while (day != today)

        if (nextAlarm == null){
            // No alarm to set
            return
        }
        // Set this alarm
        val alarmManager = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = getIntent()
        intent.putExtra("isStart", true)
        intent.putExtra("alarmProfile", nextAlarm.profile)

        currentCalendar.set(Calendar.SECOND, 0)
        currentCalendar.set(Calendar.MILLISECOND, 0)
        currentCalendar.set(Calendar.MINUTE, nextAlarm.startTimeMinutes)
        currentCalendar.set(Calendar.HOUR_OF_DAY, nextAlarm.startTimeHours)
        currentCalendar.set(Calendar.DAY_OF_WEEK, day + 1)

        val alarmMillis = currentCalendar.timeInMillis
        if (alarmMillis < System.currentTimeMillis()){
            // Can happen if setting the day of week. Just make it a week later.
            currentCalendar.add(Calendar.DAY_OF_MONTH, 7)
        }

        val alarmTime = currentCalendar.timeInMillis

        // Get endTime and put in intent
        if (nextAlarm.startTime > nextAlarm.endTime){
            currentCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        currentCalendar.set(Calendar.MINUTE, nextAlarm.endTimeMinutes)
        currentCalendar.set(Calendar.HOUR_OF_DAY, nextAlarm.endTimeHours)
        intent.putExtra("endTime", currentCalendar.timeInMillis)

        val pendingIntent = PendingIntent.getBroadcast(application, 0, intent, 0)
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent)
    }
}