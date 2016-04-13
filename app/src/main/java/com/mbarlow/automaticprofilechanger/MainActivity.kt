package com.mbarlow.automaticprofilechanger

import android.app.ProfileManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        fab.setOnClickListener { view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show() }

        ringerModeNormal.setOnClickListener { view ->
            var profileManager = ProfileManager.getService();
            profileManager.setActiveProfileByName("Default");
            Log.d("APC", "Ringer mode default");
        }

        ringerModeSilent.setOnClickListener { view ->
            var profileManager = ProfileManager.getService();
            profileManager.setActiveProfileByName("Silent");
            Log.d("APC", "Ringer mode silent");
        }

        ringerModeVibrate.setOnClickListener { view ->
            var profileManager = ProfileManager.getService();
            profileManager.setActiveProfileByName("Vibrate");
            Log.d("APC", "Ringer mode vibrate");
        }
    }
}
