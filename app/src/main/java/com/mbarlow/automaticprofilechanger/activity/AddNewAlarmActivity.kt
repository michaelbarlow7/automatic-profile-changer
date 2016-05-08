package com.mbarlow.automaticprofilechanger.activity

import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TimePicker
import com.mbarlow.automaticprofilechanger.R
import com.mbarlow.automaticprofilechanger.fragment.TimePickerFragment
import com.mbarlow.automaticprofilechanger.model.Alarm
import kotlinx.android.synthetic.main.activity_add_alarm.*
import kotlinx.android.synthetic.main.content_add_alarm.*

/**
 * Created by michael on 8/05/16.
 */
class AddNewAlarmActivity : AppCompatActivity(){

    var alarm : Alarm? = null

    val startTimePickListener = TimePickerDialog.OnTimeSetListener({view: TimePicker?, hourOfDay: Int, minute: Int ->
        alarm?.setStartTime(hourOfDay, minute)
        startTimeButton.text = alarm?.startTimeString
    })
    val endTimePickListener = TimePickerDialog.OnTimeSetListener({view: TimePicker?, hourOfDay: Int, minute: Int ->
        alarm?.setEndTime(hourOfDay, minute)
        endTimeButton.text = alarm?.endTimeString
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_alarm)
        setSupportActionBar(toolbar)

        // Get alarm from intent if we're editting
        alarm = Alarm()

        startTimeButton.setOnClickListener({ view ->
            val timePickerFragment = TimePickerFragment(startTimePickListener, alarm?.startTimeHours ?: 0, alarm?.startTimeMinutes ?: 0);
            timePickerFragment.show(fragmentManager, "starttimepicker")
        })

        endTimeButton.setOnClickListener({ view ->
            val timePickerFragment = TimePickerFragment(endTimePickListener, alarm?.endTimeHours ?: 0, alarm?.endTimeMinutes ?: 0);
            timePickerFragment.show(fragmentManager, "endtimepicker")
        })
    }


}