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

        fab.setOnClickListener { view ->
            val addAlarmActivityIntent = Intent(view.context, AddNewAlarmActivity::class.java)
            view.context.startActivity(addAlarmActivityIntent)
//            val myApp = application as AutomaticProfileChangerApplication
//            myApp.alarmDataHelper.findAndSetNextAlarm()
        }
    }

    override fun onResume() {
        super.onResume()

        //TODO: Seems more verbose than a Java cast, there's probably a better way
        val myApp = application as AutomaticProfileChangerApplication
        val alarmDao = myApp.daoSession.alarmDao

        val alarmList = alarmDao.loadAll()

        var alarmAdapter = AlarmAdapter(alarmList)

        alarmRecyclerView.adapter = alarmAdapter;
    }
}
