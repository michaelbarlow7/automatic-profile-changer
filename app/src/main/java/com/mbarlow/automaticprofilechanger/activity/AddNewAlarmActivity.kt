package com.mbarlow.automaticprofilechanger.activity

import android.app.ProfileManager
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TimePicker
import android.widget.Toast
import com.mbarlow.automaticprofilechanger.AutomaticProfileChangerApplication
import com.mbarlow.automaticprofilechanger.R
import com.mbarlow.automaticprofilechanger.fragment.TimePickerFragment
import com.mbarlow.automaticprofilechanger.model.Alarm
import com.mbarlow.automaticprofilechanger.model.AlarmDao
import de.greenrobot.dao.query.WhereCondition
import kotlinx.android.synthetic.main.activity_add_alarm.*
import kotlinx.android.synthetic.main.content_add_alarm.*

/**
 * Created by michael on 8/05/16.
 */
class AddNewAlarmActivity : AppCompatActivity(){

    var alarm = Alarm()
    var editting = false

    val startTimePickListener = TimePickerDialog.OnTimeSetListener({view: TimePicker?, hourOfDay: Int, minute: Int ->
        alarm.setStartTime(hourOfDay, minute)
        startTimeButton.text = alarm.startTimeString
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_alarm)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowCustomEnabled(true)

        // Get alarm from intent if we're editting
        val intentAlarm = intent.getSerializableExtra("ALARM")
        if (intentAlarm != null){
            supportActionBar?.title = "Edit Alarm"
            alarm = intentAlarm as Alarm
            editting = true
        }else{
            deleteButton.visibility = View.GONE
        }

        nameField.setText(alarm.name)

        // Tick days
        var i = 0
        while(i < enabledDaysLayout.childCount){
            val dayCheckBox = enabledDaysLayout.getChildAt(i) as CheckBox
            dayCheckBox.isChecked = alarm.isDayAtIndexEnabled(i)!!
            i++
        }

        startTimeButton.text = alarm.startTimeString ?: "START TIME"

        startTimeButton.setOnClickListener({ view ->
            val timePickerFragment = TimePickerFragment(startTimePickListener, alarm.startTimeHours, alarm.startTimeMinutes);
            timePickerFragment.show(fragmentManager, "starttimepicker")
        })

        //TODO: Exception handling if not cyanogenmod

        val profileManager = ProfileManager.getService();

        val profileNames = Array(profileManager.profiles.size, {i -> profileManager.profiles[i].name})
        var alarmProfileIndex = 0
        for (i in profileNames.indices){
            if (profileNames[i].equals(alarm.profile)){
                alarmProfileIndex = i
                break;
            }
        }

        val profilesAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, profileNames)

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

            val myApp = application as AutomaticProfileChangerApplication
            val alarmDao = myApp.daoSession.alarmDao

            val enabledFlags = (alarm.enabled.toInt() and 0x7F)

            // Need to check we're not setting alarms on the same time
            val queryBuilder = alarmDao.queryBuilder()
            queryBuilder.where(WhereCondition.StringCondition("${AlarmDao.Properties.Enabled.columnName} & ${enabledFlags.toString()} != 0"),
                    AlarmDao.Properties.Id.notEq(alarm.id ?: "-1"),
                    AlarmDao.Properties.StartTime.eq(alarm.startTime))

            val query = queryBuilder.list()

            if (query.count() > 0){
                Toast.makeText(view.context, "Clashing with one or more alarms set on same day(s)", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Save alarm
            if (!editting){
                alarm.enabled = (alarm.enabled.toInt() or 0x80).toByte()
            }
            alarm.name = nameField.text.toString()
            alarm.profile = profileSpinner.selectedItem.toString()
            alarmDao.insertOrReplace(alarm)

            (applicationContext as AutomaticProfileChangerApplication).alarmDataHelper.resetAlarm()

            finish()
        }

        cancelButton.setOnClickListener {
            finish()
        }

        deleteButton.setOnClickListener {
            val myApp = application as AutomaticProfileChangerApplication
            val alarmDao = myApp.daoSession.alarmDao
            alarmDao.delete(alarm)
            (applicationContext as AutomaticProfileChangerApplication).alarmDataHelper.resetAlarm()
            finish()
        }
    }


}