package com.common.lib.network

interface HttpListener<Data> {

    fun onSuccess(bean: Data?, msg: String?)

    fun dataError(code: Int, msg: String?)

    fun connectError(e: Throwable?)
}