package com.mbarlow.automaticprofilechanger.activity

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.mbarlow.automaticprofilechanger.R
import com.mbarlow.automaticprofilechanger.adapter.AlarmAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        alarmRecyclerView.setHasFixedSize(true); // I think this is true
        alarmRecyclerView.layoutManager = LinearLayoutManager(this);

//        val alarm1 = Alarm(name = "First alarm",
//                enabled = true,
//                enabledMonday = false,
//                enabledTuesday = true,
//                enabledWednesday = true,
//                enabledThursday = true,
//                enabledFriday = false,
//                enabledSaturday = false,
//                enabledSunday = true,
//                startTime = "3:00pm",
//                endTime = "9:00pm"
//        )
//
//        val alarm2 = Alarm(name = "Second alarm",
//                enabled = false,
//                enabledMonday = false,
//                enabledTuesday = true,
//                enabledWednesday = true,
//                enabledThursday = true,
//                enabledFriday = true,
//                enabledSaturday = false,
//                enabledSunday = false,
//                startTime = "7:00am",
//                endTime = "4:00pm"
//        )

//        val alarmList = ArrayList<Alarm>();
//        alarmList.add(alarm1);
//        alarmList.add(alarm2);

//        var alarmAdapter = AlarmAdapter(alarmList);

//        alarmRecyclerView.adapter = alarmAdapter;
        fab.setOnClickListener { view -> Snackbar.make(view, "TODO: Add alarm here", Snackbar.LENGTH_LONG).setAction("Action", null).show() }
    }
}
