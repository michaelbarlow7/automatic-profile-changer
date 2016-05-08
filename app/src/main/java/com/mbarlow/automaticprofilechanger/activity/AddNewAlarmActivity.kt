package com.mbarlow.automaticprofilechanger.activity

import android.app.ProfileManager
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
import android.widget.TimePicker
import com.mbarlow.automaticprofilechanger.AutomaticProfileChangerApplication
import com.mbarlow.automaticprofilechanger.R
import com.mbarlow.automaticprofilechanger.fragment.TimePickerFragment
import com.mbarlow.automaticprofilechanger.model.Alarm
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

        // Get alarm from intent if we're editting
        var intentAlarm = intent.getSerializableExtra("ALARM")
        if (intentAlarm != null){
            alarm = intentAlarm as Alarm
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

        confirmButton.setOnClickListener({view ->
            // Save alarm
            alarm.name = nameField.text.toString()
            var i = 0
            while(i < enabledDaysLayout.childCount){
                var dayCheckBox = enabledDaysLayout.getChildAt(i) as CheckBox
                alarm.setDayAtIndexEnabled(i, dayCheckBox.isChecked)
                i++
            }
            alarm.profile = profileSpinner.selectedItem.toString()


            val myApp = application as AutomaticProfileChangerApplication
            val alarmDao = myApp.daoSession.alarmDao
            alarmDao.insertOrReplace(alarm)

            finish()
        })

        cancelButton.setOnClickListener({view ->
            finish()
        })
    }


}