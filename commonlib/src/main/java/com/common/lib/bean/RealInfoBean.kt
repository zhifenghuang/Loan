package com.common.lib.bean

import java.io.Serializable

class RealInfoBean : Serializable {
    var id = 0
    var user_id = 0
    var name = ""
    var id_card = ""
    var card_img1 = ""
    var card_img2 = ""
    var phone = ""
    var bank_id_card = ""
    var bank_user = ""
    var bank_card = ""
    var bank_name = ""
    var education = ""
    var income = ""
    var purpose = ""
    var house = 0
    var car = 0
    var sign = ""
    var created_at = ""
    var updated_at = ""

    fun getBankCardNo(): String? {
        val length = bank_card.length
        if (length >= 4) {
            var cardNo = ""
            for (i in 1..length - 4) {
                cardNo += "*"
            }
            cardNo += bank_card.substring(length - 4)
            return cardNo
        }
        return ""
    }
}