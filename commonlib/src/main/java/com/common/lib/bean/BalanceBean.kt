package com.common.lib.bean

import java.io.Serializable
import java.text.DecimalFormat

class BalanceBean : Serializable {
    var money = 0.0

    fun getMoneyStr() : String{
        return  DecimalFormat.getNumberInstance().format(money.toInt())
    }
}