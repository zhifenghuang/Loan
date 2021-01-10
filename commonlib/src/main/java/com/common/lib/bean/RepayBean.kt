package com.common.lib.bean

import java.io.Serializable
import java.text.DecimalFormat

class RepayBean : Serializable {
    var order_id = ""
    var repay_time = ""
    var repay_money = ""
    var repay_way = ""
    var repay_status = ""

    fun geRepayMoneyStr(): String {
        try {
            return DecimalFormat.getNumberInstance().format(repay_money.toInt())
        } catch (e: Exception) {

        }
        return repay_money;
    }
}