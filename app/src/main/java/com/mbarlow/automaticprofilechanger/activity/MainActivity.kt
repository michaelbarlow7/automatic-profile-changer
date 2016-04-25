package com.mbarlow.automaticprofilechanger.activity

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.mbarlow.automaticprofilechanger.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view -> Snackbar.make(view, "TODO: Add alarm here", Snackbar.LENGTH_LONG).setAction("Action", null).show() }
    }
}
