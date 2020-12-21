package com.common.lib.manager

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log

class ConfigurationManager private constructor() {

    private var mContext: Context? = null
    private var mActivityRecord: Int = 0
    var mCurrentActivity: Activity? = null

    private var mActivityList: ArrayList<Activity>? = null


    companion object {
        @Volatile
        private var instance: ConfigurationManager? = null

        fun getInstance() =
                instance ?: synchronized(this) {
                    instance ?: ConfigurationManager().also { instance = it }
                }
    }

    fun setContext(context: Context?) {
        mContext = context
        mActivityList = ArrayList()
        (mContext as Application).registerActivityLifecycleCallbacks(object :
                Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(
                    activity: Activity,
                    savedInstanceState: Bundle?
            ) {
                mActivityList?.add(activity)
            }

            override fun onActivityStarted(activity: Activity) {
                ++mActivityRecord
                mCurrentActivity = activity
            }

            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {
                --mActivityRecord
            }

            override fun onActivitySaveInstanceState(
                    activity: Activity,
                    outState: Bundle
            ) {
            }

            override fun onActivityDestroyed(activity: Activity) {
                if (mCurrentActivity == activity) {
                    mCurrentActivity = null
                }
                mActivityList?.remove(activity)
            }
        })
    }

    fun finishAllOtherActivity(expectActivityName: String) {
        if (mActivityList == null) {
            return
        }
        for (activity in mActivityList!!) {
            Log.e("aaaaaaa", activity.javaClass.name + ", " + expectActivityName)
            if (activity.javaClass.name.contains(expectActivityName)) {
                continue
            }
            activity.finish()
        }
    }

    fun getContext() =
            mContext

    fun isInApp(): Boolean =
            (mActivityRecord > 0)
}