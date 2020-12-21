package com.common.lib.bean

class BasicResponse<T> {

    var code: Int = 0 // 返回的结果标志

    var msg // 错误描述
            : String? = null

    var data: T? = null

    fun isSuccess(): Boolean {
        return code == 1
    }

}