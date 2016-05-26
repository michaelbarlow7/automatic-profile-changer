package com.mbarlow.automaticprofilechanger.fragment

import android.app.Dialog
import android.app.DialogFragment
import android.app.TimePickerDialog
import android.os.Bundle

/**
 * Created by michael on 8/05/16.
 */
class TimePickerFragment(
        val timeSetListener: TimePickerDialog.OnTimeSetListener,
        val hour : Int = 0,
        val minutes : Int = 0) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog? = TimePickerDialog(activity, timeSetListener, hour, minutes, false)
}