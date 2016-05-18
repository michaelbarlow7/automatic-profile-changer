/*
 * Copyright (C) 2012-2013 Seamus Phelan <SeamusPhelan@gmail.com>
 *
 * This file is part of ProfileSwitcher.
 *
 * ProfileSwitcher is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ProfileSwitcher is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ProfileSwitcher.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.mbarlow.automaticprofilechanger.receiver

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.ProfileManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v7.app.NotificationCompat
import android.text.format.Time
import android.util.Log
import com.mbarlow.automaticprofilechanger.AutomaticProfileChangerApplication
import com.mbarlow.automaticprofilechanger.R
import java.util.*

class AlarmBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("TAG", "Received broadcast!"  + intent)

        val myApp = context.applicationContext as AutomaticProfileChangerApplication
        if (intent.action == Intent.ACTION_BOOT_COMPLETED || intent.action == Intent.ACTION_MY_PACKAGE_REPLACED){
            // Reboot
            val sharedPreferences = context.getSharedPreferences("END_ALARM_PREFERENCE", 0)
            val profile = sharedPreferences.getString("existingProfile", null)
            if (profile != null){
                val time = sharedPreferences.getLong("alarmTime", -1L) // Stores time as long
                if (System.currentTimeMillis() <= time){
                    // Set alarm to change back to profile
                    setEndAlarm(context, profile, time)

                    return
                }

                // We've passed the time for this alarm, so clear prefs and set next stored alarm
                sharedPreferences.edit().clear().apply()
            }

            // Find and set alarm for next profile
            myApp.alarmDataHelper.findAndSetNextAlarm()
            return
        }
        // If this isn't a boot, this is an alarm
        val profile = intent.getStringExtra("alarmProfile") ?: return
        val profileManager = ProfileManager.getService();

        val existingProfile = profileManager.activeProfile


        profileManager.setActiveProfileByName(profile)

        // DEBUG STUFF
        val builder = NotificationCompat.Builder(context)
        builder.setSmallIcon(R.drawable.ic_stat_action_notification)
        builder.setContentTitle("Profile changed")
        val calendar = Calendar.getInstance()
        builder.setContentText("Profile: $profile at: ${calendar.get(Calendar.HOUR_OF_DAY)} : ${calendar.get(Calendar.MINUTE)}")
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, builder.build())

        if (!intent.getBooleanExtra("isStart", true)){
            val sharedPreferences = context.getSharedPreferences("END_ALARM_PREFERENCE", 0)
            sharedPreferences.edit().clear().apply()
            myApp.alarmDataHelper.findAndSetNextAlarm()
            return
        }

        val endTime = intent.getLongExtra("endTime", -1L)
        if (endTime < 0){
            return
        }

        setEndAlarm(context, existingProfile.name, endTime)

        return
    }

    private fun setEndAlarm(context: Context, profile: String?, time: Long) {
        val alarmIntent = Intent(context, AlarmBroadcastReceiver::class.java)
        alarmIntent.putExtra("isStart", false)
        alarmIntent.putExtra("alarmProfile", profile)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent)
    }
}
