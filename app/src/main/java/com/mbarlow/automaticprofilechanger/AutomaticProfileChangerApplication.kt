package com.mbarlow.automaticprofilechanger

import android.app.Application
import com.mbarlow.automaticprofilechanger.model.AlarmDataHelper
import com.mbarlow.automaticprofilechanger.model.DaoMaster
import com.mbarlow.automaticprofilechanger.model.DaoSession

/**
 * Created by michael on 8/05/16.
 */
class AutomaticProfileChangerApplication : Application() {

    val daoSession : DaoSession by lazy {
        val helper = DaoMaster.DevOpenHelper(this, "alarm-db", null)
        val db = helper.writableDatabase

        val daoMaster = DaoMaster(db)
        daoMaster.newSession()
    }

    val alarmDataHelper : AlarmDataHelper by lazy {
        AlarmDataHelper(this)
    }

}

