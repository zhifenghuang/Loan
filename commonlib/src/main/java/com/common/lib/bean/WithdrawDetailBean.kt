package com.common.lib.bean

import android.text.TextUtils
import java.io.Serializable
import java.text.DecimalFormat

class WithdrawDetailBean : Serializable {
    var id = 0
    var user_id = 0
    var status = 0
    var tips = ""
    var money = ""
    var created_at = ""
    var updated_at = ""

    fun geMoneyStr(): String {
        try {
            return DecimalFormat.getNumberInstance().format(money.toInt())
        } catch (e: Exception) {

        }
        return money;
    }
//
//    fun getMoneyStr(): String? {
//        if (TextUtils.isEmpty(money)) {
//            return ""
//        }
//        val length = money.length
//        var monerStr = ""
//        for (i in 1..length) {
//            monerStr+=money[i-1]
//            if(i)
//        }
//    }
}