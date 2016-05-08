package com.mbarlow.automaticprofilechanger.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.mbarlow.automaticprofilechanger.AutomaticProfileChangerApplication
import com.mbarlow.automaticprofilechanger.R
import com.mbarlow.automaticprofilechanger.adapter.AlarmAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        alarmRecyclerView.setHasFixedSize(true) // I think this is true
        alarmRecyclerView.layoutManager = LinearLayoutManager(this)

//        val alarm1 = Alarm()
//        alarm1.name = "First alarm"
//        alarm1.enabled = 0x3F
//        alarm1.startTime = 243
//        alarm1.endTime = 1299
//
//        val alarm2 = Alarm()
//        alarm2.name = "Second alarm"
//        alarm2.enabled = 0x23
//        alarm2.startTime = 443
//        alarm2.endTime = 1099

//        alarmDao.insertInTx(alarm1, alarm2)
        //TODO: Seems more verbose than a Java cast, there's probably a better way
        val myApp : AutomaticProfileChangerApplication = application as AutomaticProfileChangerApplication
        val alarmDao = myApp.daoSession.alarmDao

        val alarmList = alarmDao.loadAll()

        var alarmAdapter = AlarmAdapter(alarmList)

//        val intent = Intent(this, AddNewAlarmActivity.class)

        alarmRecyclerView.adapter = alarmAdapter;
        fab.setOnClickListener { view ->
           /* new activity here */
//            val intent = Intent(AddNewAlarmActivity.class);
            val addAlarmActivityIntent = Intent(view.context, AddNewAlarmActivity::class.java)
            view.context.startActivity(addAlarmActivityIntent)
//            Snackbar.make(view, "TODO: Add alarm here", Snackbar.LENGTH_LONG).setAction("Action", null).show()
        }
    }
}
