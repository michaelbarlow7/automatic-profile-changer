package com.mbarlow.automaticprofilechanger.model

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import com.mbarlow.automaticprofilechanger.AutomaticProfileChangerApplication
import com.mbarlow.automaticprofilechanger.receiver.AlarmBroadcastReceiver
import de.greenrobot.dao.query.WhereCondition
import java.util.*

/**
 * Created by michael on 9/05/16.
 */
class AlarmDataHelper(val application: AutomaticProfileChangerApplication) {

    fun getPendingIntent() : PendingIntent {
        val intent = Intent(application, AlarmBroadcastReceiver::class.java);
        return PendingIntent.getBroadcast(application, 0, intent, 0)
    }

    fun resetAlarm(){
        val sharedPreferences = application.getSharedPreferences("END_ALARM_PREFERENCE", 0)
        if (sharedPreferences.contains("existingProfile")){
            // We're already in an alarm period, don't mess with it
            return
        }

        val alarmManager = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = getPendingIntent()
        alarmManager.cancel(pendingIntent)

        // Find and set next alarm (put in own function)
    }

    fun findAndSetNextAlarm(){
        val daoSession = application.daoSession
        val instance = Calendar.getInstance()
        val today = instance.get(Calendar.DAY_OF_WEEK) - 1 // 0-indexing it
        var currentTime = instance.get(Calendar.HOUR_OF_DAY) * 60 + instance.get(Calendar.MINUTE)

        var day = today
        var result : Alarm? = null
        do {
            var dayMask = (Alarm.dayMaskPairs[day].second as Int or 0x80) // Alarm must be enabled
            val qb = daoSession.queryBuilder(Alarm::class.java)
            qb.where(WhereCondition.StringCondition("ENABLED & $dayMask == $dayMask"),AlarmDao.Properties.StartTime.gt(currentTime))
            qb.orderAsc(AlarmDao.Properties.StartTime)

            var list = qb.list()
            if (list != null && !list.isEmpty()){
                result = list[0]
                break
            }

            currentTime = 0 // On following days we want to find the soonest time starting from midnight
            day++
            day %= 7
        }while (day != today)

        if (result == null){
            // No alarm to set
            return
        }
        // Set this alarm
    }
}