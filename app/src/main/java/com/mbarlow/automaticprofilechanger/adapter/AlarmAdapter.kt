package com.mbarlow.automaticprofilechanger.adapter

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import com.mbarlow.automaticprofilechanger.AutomaticProfileChangerApplication
import com.mbarlow.automaticprofilechanger.R
import com.mbarlow.automaticprofilechanger.activity.AddNewAlarmActivity
import com.mbarlow.automaticprofilechanger.model.Alarm

/**
 * Created by michael on 25/04/16.
 */

class AlarmAdapter(private var alarmList: List<Alarm>) : RecyclerView.Adapter<AlarmAdapter.ViewHolder>(){

    override fun getItemCount(): Int {
        return alarmList.size;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.alarm_item, parent, false);
        return ViewHolder(view);
    }

    override fun onBindViewHolder(holder: AlarmAdapter.ViewHolder?, position: Int) {
        holder?.bindAlarm(alarmList[position])
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnLongClickListener, CompoundButton.OnCheckedChangeListener{

        val alarmCheckbox: CheckBox = itemView.findViewById(R.id.alarmCheckbox) as CheckBox
        val daysTextView = itemView.findViewById(R.id.daysTextView) as TextView
        val timesTextView = itemView.findViewById(R.id.timesTextView) as TextView
        val profileTextView = itemView.findViewById(R.id.alarmProfile) as TextView

        init{
            itemView.setOnLongClickListener(this)
            alarmCheckbox.setOnCheckedChangeListener(this)
        }

        fun bindAlarm(alarm: Alarm) {
            with(alarm){
                alarmCheckbox.text = name
                alarmCheckbox.isChecked = isEnabled
                profileTextView.text = profile
                daysTextView.text = alarm.daysEnabledString
                timesTextView.text = alarm.startTimeString + " - " + alarm.endTimeString
                itemView.tag = this
            }
        }

        override fun onLongClick(v: View?): Boolean {
            val addAlarmActivityIntent = Intent(v?.context, AddNewAlarmActivity::class.java)
            addAlarmActivityIntent.putExtra("ALARM", v?.tag as Alarm)
            v?.context?.startActivity(addAlarmActivityIntent)
            return false
        }

        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            if (itemView.tag == null){
                return
            }
            val alarm = itemView.tag as Alarm
            val myApp = buttonView?.context?.applicationContext as AutomaticProfileChangerApplication
            alarm.setIsEnabled(isChecked)
            myApp.daoSession.insertOrReplace(alarm)
            //TODO: Need to reset the next alarm
        }
    }
}