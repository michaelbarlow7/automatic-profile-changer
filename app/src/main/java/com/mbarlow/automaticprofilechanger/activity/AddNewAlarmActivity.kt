package com.mbarlow.automaticprofilechanger.activity

import android.app.ProfileManager
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.*
import com.mbarlow.automaticprofilechanger.AutomaticProfileChangerApplication
import com.mbarlow.automaticprofilechanger.R
import com.mbarlow.automaticprofilechanger.fragment.TimePickerFragment
import com.mbarlow.automaticprofilechanger.model.Alarm
import com.mbarlow.automaticprofilechanger.model.AlarmDao
import kotlinx.android.synthetic.main.activity_add_alarm.*
import kotlinx.android.synthetic.main.content_add_alarm.*

/**
 * Created by michael on 8/05/16.
 */
class AddNewAlarmActivity : AppCompatActivity(){

    var alarm = Alarm()

    val startTimePickListener = TimePickerDialog.OnTimeSetListener({view: TimePicker?, hourOfDay: Int, minute: Int ->
        alarm.setStartTime(hourOfDay, minute)
        startTimeButton.text = alarm.startTimeString
    })
    val endTimePickListener = TimePickerDialog.OnTimeSetListener({view: TimePicker?, hourOfDay: Int, minute: Int ->
        alarm.setEndTime(hourOfDay, minute)
        endTimeButton.text = alarm.endTimeString
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_alarm)
        setSupportActionBar(toolbar)
        //TODO: Add up/back button up the top

        // Get alarm from intent if we're editting
        var intentAlarm = intent.getSerializableExtra("ALARM")
        if (intentAlarm != null){
            //TODO: Change title to "Edit alarm" or something
            alarm = intentAlarm as Alarm
        }else{
            deleteButton.visibility = View.GONE
        }

        nameField.setText(alarm.name)

        // Tick days
        var i = 0
        while(i < enabledDaysLayout.childCount){
            var dayCheckBox = enabledDaysLayout.getChildAt(i) as CheckBox
            dayCheckBox.isChecked = alarm.isDayAtIndexEnabled(i)!!
            i++
        }

        startTimeButton.text = alarm.startTimeString ?: "START TIME"
        endTimeButton.text = alarm.endTimeString ?: "END TIME"

        startTimeButton.setOnClickListener({ view ->
            val timePickerFragment = TimePickerFragment(startTimePickListener, alarm.startTimeHours, alarm.startTimeMinutes);
            timePickerFragment.show(fragmentManager, "starttimepicker")
        })

        endTimeButton.setOnClickListener({ view ->
            val timePickerFragment = TimePickerFragment(endTimePickListener, alarm.endTimeHours, alarm.endTimeMinutes);
            timePickerFragment.show(fragmentManager, "endtimepicker")
        })

        //TODO: Exception handling if not cyanogenmod?

        var profileManager = ProfileManager.getService();

        var profileNames = Array(profileManager.profiles.size, {i -> profileManager.profiles[i].name})
        var alarmProfileIndex = 0
        for (i in profileNames.indices){
            if (profileNames[i].equals(alarm.profile)){
                alarmProfileIndex = i
                break;
            }
        }

        var profilesAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, profileNames)

        profilesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        profileSpinner.adapter = profilesAdapter

        profileSpinner.setSelection(alarmProfileIndex)

        confirmButton.setOnClickListener { view ->
            var j = 0
            while(j < enabledDaysLayout.childCount){
                var dayCheckBox = enabledDaysLayout.getChildAt(j) as CheckBox
                alarm.setDayAtIndexEnabled(j, dayCheckBox.isChecked)
                j++
            }
            if (nameField.text.isNullOrEmpty()){
                Toast.makeText(view.context, "No name set", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (alarm.enabled.toInt() == 0){
                Toast.makeText(view.context, "No days set", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (alarm.startTime == null){
                Toast.makeText(view.context, "Start time not set", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (alarm.endTime == null){
                Toast.makeText(view.context, "End time not set", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            //TODO: Check for empty/invalid fields
            val myApp = application as AutomaticProfileChangerApplication
            val alarmDao = myApp.daoSession.alarmDao

            var enabledFlags = (alarm.enabled.toInt() and 0x7F)

            //TODO: Use columnNames

            // Check if there are any overlaps in the same day
            var query = alarmDao.queryRaw("WHERE (ENABLED | ? != 0) " +
            "AND (_ID != ? ) " +
            "AND (END_TIME > START_TIME) " +
            "AND (END_TIME > ? ) " +
            "AND (START_TIME < ? )",
                    enabledFlags.toString(),
                    alarm.id?.toString() ?: "-1",
                    alarm.startTime.toString(),
                    alarm.endTime.toString())

            if (query.count() > 0){
                Log.e("TAG", "Error!! Overlap with an alarm from today!")
                finish()
                return@setOnClickListener
            }
            var yesterdayEnabled = (enabledFlags shl 1)
            // Need to wrap around for saturday
            var saturday = (yesterdayEnabled and 0x80)
            if (saturday != 0){
                yesterdayEnabled = (yesterdayEnabled or 0x01)
                yesterdayEnabled = (yesterdayEnabled and 0x7f)
            }

            query = alarmDao.queryRaw("WHERE (ENABLED | ? != 0) " +
                    "AND (_ID != ? ) " +
                    "AND (END_TIME < START_TIME) " +
                    "AND (END_TIME > ? ) ",
                    yesterdayEnabled.toString(),
                    alarm.id?.toString() ?: "-1",
                    alarm.startTime.toString())

            if (query.count() > 0){
                Log.e("TAG", "Error!! Overlap with an alarm from yesterday!")
                finish()
                return@setOnClickListener
            }

            if (alarm.endTime < alarm.startTime){
                val saturdayEnabled = (enabledFlags and 0x01)
                var tomorrowEnabled = (enabledFlags shr 1)
                if (saturdayEnabled != 0){
                    tomorrowEnabled = (tomorrowEnabled or 0x40)
                }

                query = alarmDao.queryRaw("WHERE (ENABLED | ? != 0) " +
                        "AND (_ID != ? ) " +
                        "AND (START_TIME < ? ) ",
                        tomorrowEnabled.toString(),
                        alarm.id?.toString() ?: "-1",
                        alarm.endTime.toString())

                if (query.count() > 0){
                    Log.e("TAG", "Error!! Overlap with an alarm tomorrow!")
                    finish()
                    return@setOnClickListener
                }
            }

            // Save alarm
            //TODO: Only do the following if it's a new alarm
            alarm.enabled = (alarm.enabled.toInt() or 0x80).toByte()
            alarm.name = nameField.text.toString()
            alarm.profile = profileSpinner.selectedItem.toString()
            alarmDao.insertOrReplace(alarm)
            //TODO: Schedule next alarm

            finish()
        }

        cancelButton.setOnClickListener {
            finish()
        }

        deleteButton.setOnClickListener {
            //TODO: Show a dialog
            val myApp = application as AutomaticProfileChangerApplication
            val alarmDao = myApp.daoSession.alarmDao
            alarmDao.delete(alarm)
            finish()
        }
    }


}