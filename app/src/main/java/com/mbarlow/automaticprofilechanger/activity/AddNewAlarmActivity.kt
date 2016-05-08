package com.mbarlow.automaticprofilechanger.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.mbarlow.automaticprofilechanger.R
import kotlinx.android.synthetic.main.activity_add_alarm.*

/**
 * Created by michael on 8/05/16.
 */
class AddNewAlarmActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_alarm)
        setSupportActionBar(toolbar)
    }
}