package com.mbarlow.automaticprofilechanger.activity

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.mbarlow.automaticprofilechanger.R
import com.mbarlow.automaticprofilechanger.adapter.AlarmAdapter
import com.mbarlow.automaticprofilechanger.model.Alarm
import com.mbarlow.automaticprofilechanger.model.DaoMaster
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        //TODO: Move this to the application class when we create it

        val helper = DaoMaster.DevOpenHelper(this, "alarm-db", null)
        val db = helper.writableDatabase

        val daoMaster = DaoMaster(db)
        val daoSession = daoMaster.newSession()
        val alarmDao = daoSession.alarmDao

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

        val alarmList = alarmDao.loadAll();

//        val alarmList = ArrayList<Alarm>();
//        alarmList.add(alarm1);
//        alarmList.add(alarm2);

        var alarmAdapter = AlarmAdapter(alarmList);

        alarmRecyclerView.adapter = alarmAdapter;
        fab.setOnClickListener { view -> Snackbar.make(view, "TODO: Add alarm here", Snackbar.LENGTH_LONG).setAction("Action", null).show() }
    }
}
