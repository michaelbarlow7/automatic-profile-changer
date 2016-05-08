package com.mbarlow.automaticprofilechanger.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.mbarlow.automaticprofilechanger.R
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

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val alarmCheckbox: CheckBox = itemView.findViewById(R.id.alarmCheckbox) as CheckBox
        val daysTextView = itemView.findViewById(R.id.daysTextView) as TextView
        val timesTextView = itemView.findViewById(R.id.timesTextView) as TextView

        fun bindAlarm(alarm: Alarm) {
            with(alarm){
                alarmCheckbox.text = name
//                alarmCheckbox.isChecked = alarm.enabled
//                var stringBuilder = StringBuilder();
//                if (alarm.enabledSunday){
//                    stringBuilder.append("Sun, ");
//                }
//                if (alarm.enabledMonday){
//                    stringBuilder.append("Mon, ");
//                }
//                if (alarm.enabledTuesday){
//                    stringBuilder.append("Tue, ");
//                }
//                if (alarm.enabledWednesday){
//                    stringBuilder.append("Wed, ");
//                }
//                if (alarm.enabledThursday){
//                    stringBuilder.append("Thu, ");
//                }
//                if (alarm.enabledFriday){
//                    stringBuilder.append("Fri, ");
//                }
//                if (alarm.enabledSaturday){
//                    stringBuilder.append("Sat");
//                }
                daysTextView.text = alarm.daysEnabledString
                timesTextView.text = alarm.startTimeString + " - " + alarm.endTimeString
            }
        }
    }
}