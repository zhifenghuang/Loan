package com.common.lib.bean

import java.io.Serializable

class LoanInfoBean : Serializable {
    var id = 0
    var user_id = 0
    var kf_id = 0
    var order_id = ""
    var status = 0
    var status_at = ""
    var amount = ""
    var amount_class = ""
    var rate = ""
    var term = ""
    var repay_m = ""
    var created_at = ""
    var updated_at = ""
}