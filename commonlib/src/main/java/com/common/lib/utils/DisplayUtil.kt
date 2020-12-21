package com.common.lib.utils

import android.annotation.SuppressLint
import android.content.Context
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object DisplayUtil {
    const val TAG = "DisplayUtil"
    private const val ONE_MIN = 60 * 1000
    private const val ONE_HOUR = 60 * ONE_MIN
    private const val ONE_DAY = 24 * ONE_HOUR
    private const val TWO_DAY = 2 * ONE_DAY
    private const val DATE_FORMAT_HOUR_AND_TIME = "hh:mm"

    fun getTimeString(deltaTimeMillis: Long): String {
        val hour = TimeUnit.MILLISECONDS.toHours(deltaTimeMillis)
        return String.format(
            "%d:%d",
            hour,
            TimeUnit.MILLISECONDS.toMinutes(deltaTimeMillis) - TimeUnit.HOURS.toMinutes(hour)
        )
    }

    @SuppressLint("SimpleDateFormat")
    fun getChatMessageTime(context: Context, timeMillis: Long): String {
        val deltaTimeMillis = (System.currentTimeMillis() - timeMillis)
        if (deltaTimeMillis < ONE_MIN) {
            return StringUtil.getString(context, "pblc_txt_justnow")
        } else if (deltaTimeMillis < ONE_DAY) {
            return SimpleDateFormat(DATE_FORMAT_HOUR_AND_TIME).format(Date(timeMillis))
        } else if (deltaTimeMillis < TWO_DAY) {
            val formatString = StringUtil.getString(context, "pblc_txt_yesterdaytime")
            val timeString = SimpleDateFormat(DATE_FORMAT_HOUR_AND_TIME).format(Date(timeMillis))
            return String.format(formatString, timeString)
        } else {
            return SimpleDateFormat("MM-dd HH:mm").format(Date(timeMillis))
        }
    }
}