package com.common.lib.network


import android.content.ComponentName
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.common.lib.bean.BasicResponse
import com.common.lib.manager.ConfigurationManager
import com.common.lib.manager.DataManager
import com.common.lib.mvp.IView
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable


class HttpObserver<T : BasicResponse<Data>, Data> : Observer<T> {

    companion object {
        const val SHOW_LOADING = 1
        const val HIDE_LOADING = 2
    }

    private var isShowLoading: Boolean? = true
    private var view: IView? = null
    private var listener: HttpListener<Data>? = null
    private var compositeDisposable: CompositeDisposable? = null


    constructor(
        listener: HttpListener<Data>,
        compositeDisposable: CompositeDisposable?
    ) : this(false, null, listener, compositeDisposable)

    constructor(
        view: IView?,
        listener: HttpListener<Data>,
        compositeDisposable: CompositeDisposable?
    ) : this(true, view, listener, compositeDisposable)

    constructor(
        isShowLoading: Boolean,
        view: IView?,
        listener: HttpListener<Data>,
        compositeDisposable: CompositeDisposable?
    ) {
        this.isShowLoading = isShowLoading
        this.view = view
        this.listener = listener
        this.compositeDisposable = compositeDisposable
    }

    override fun onComplete() {
        hideLoading()
    }

    override fun onSubscribe(d: Disposable) {
        compositeDisposable?.add(d)
        showLoading()
    }

    override fun onNext(t: T) {
        if (t.isSuccess()) {
            listener?.onSuccess(t.data, t.msg)
        } else {
            if (t.code == 404) {
                val intent = Intent()
                val com = ComponentName(
                    "com.elephant.loan",
                    "com.elephant.loan.activity.LoginActivity"
                )
                intent.component = com
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                ConfigurationManager.getInstance().getContext()?.startActivity(intent)
                DataManager.getInstance().logout()
                return
            }
            listener?.dataError(t.code, t.msg)
        }
    }

    override fun onError(e: Throwable) {
        hideLoading()
        listener?.connectError(e)
    }

    private val mHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                SHOW_LOADING -> {
                    view?.showProgressDialog()
                }
                HIDE_LOADING -> {
                    view?.dismissProgressDialog()
                }
            }
        }
    }

    fun showLoading() {
        if (isShowLoading!! && view != null) {
            mHandler.obtainMessage(SHOW_LOADING).sendToTarget()
        }
    }

    fun hideLoading() {
        if (isShowLoading!! && view != null) {
            mHandler.obtainMessage(HIDE_LOADING).sendToTarget()
        }
    }

}