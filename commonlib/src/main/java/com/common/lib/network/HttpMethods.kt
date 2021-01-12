package com.common.lib.network

import android.text.TextUtils
import com.common.lib.bean.*
import com.common.lib.camera.record.Constants
import com.common.lib.constant.Constants.APP_VERSION
import com.common.lib.manager.ConfigurationManager
import com.common.lib.manager.DataManager
import com.common.lib.utils.BaseUtils
import com.common.lib.utils.LogUtil
import com.common.lib.utils.NetUtil
import com.google.gson.GsonBuilder
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.time.temporal.TemporalAmount
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


class HttpMethods private constructor() {

    private val TAG: String = "HttpMethods"

    private val api: Api

    companion object {
        const val TAG = "HttpMethods"
        const val CONNECT_TIMEOUT: Long = 30
        const val WRITE_TIMEOUT: Long = 30
        const val READ_TIMEOUT: Long = 30

        @Volatile
        private var instance: HttpMethods? = null

        fun getInstance() =
            instance
                ?: synchronized(this) {
                    instance
                        ?: HttpMethods()
                            .also { instance = it }
                }
    }

    init {
        val builder = OkHttpClient.Builder()
        val loggingInterceptor =
            HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    LogUtil.LogE(message)
                }
            })
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)


        val interceptor: Interceptor = object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                val builder = chain.request()
                    .newBuilder()
                val token = DataManager.getInstance().getToken();
                if (!TextUtils.isEmpty(token)) {
                    builder.addHeader(
                        "token", token
                    )
                }
                return chain.proceed(builder.build())
            }
        }
        builder
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .addInterceptor(loggingInterceptor)

        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder()
            .client(builder.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .baseUrl(getBaseUrl())
            .build()
        api = retrofit.create(Api::class.java)
    }

    public fun getBaseUrl(): String {
        return if (APP_VERSION == 0) {
            "http://18.162.96.211:88/"
        } else {
            "http://18.166.30.109:88/"
        }
    }

    fun installIndex(
        observer: HttpObserver<BasicResponse<Any>, Any>
    ) {
        //device_type=设备类型:1安卓2苹果
        val observable = api.installIndex(
            1,
            "android", BaseUtils.getDeviceId(ConfigurationManager.getInstance().getContext()!!)!!
        )
        toSubscribe(observable, observer)
    }

    fun register(
        phone: String,
        password: String,
        code: String,
        observer: HttpObserver<BasicResponse<Any>, Any>
    ) {
        //device_type=设备类型:1安卓2苹果
        val observable = api.register(
            phone, password, code, 1,
            "android", BaseUtils.getDeviceId(ConfigurationManager.getInstance().getContext()!!)!!
        )
        toSubscribe(observable, observer)
    }

    fun login(
        phone: String,
        password: String,
        observer: HttpObserver<BasicResponse<String>, String>
    ) {
        val observable = api.login(phone, password)
        toSubscribe(observable, observer)
    }

    fun sms(
        phone: String,
        observer: HttpObserver<BasicResponse<Any>, Any>
    ) {
        val observable = api.sms(phone)
        toSubscribe(observable, observer)
    }

    fun paramsIndex(
        observer: HttpObserver<BasicResponse<ArrayList<HashMap<String, String>>>, ArrayList<HashMap<String, String>>>
    ) {
        val observable = api.paramsIndex()
        toSubscribe(observable, observer)
    }

    fun banner(
        observer: HttpObserver<BasicResponse<ArrayList<String>>, ArrayList<String>>
    ) {
        val observable = api.banner()
        toSubscribe(observable, observer)
    }

    fun applyLoan(
        amount: String,
        a_class: String,
        term: String,
        observer: HttpObserver<BasicResponse<Any>, Any>
    ) {
        val observable = api.applyLoan(amount, a_class, term)
        toSubscribe(observable, observer)
    }

    fun getServiceUrl(
        observer: HttpObserver<BasicResponse<String>, String>
    ) {
        val observable = api.serviceUrl()
        toSubscribe(observable, observer)
    }

    fun aboutUs(
        observer: HttpObserver<BasicResponse<ArrayList<ArticleBean>>, ArrayList<ArticleBean>>
    ) {
        val observable = api.aboutUs()
        toSubscribe(observable, observer)
    }

    fun articleList(
        observer: HttpObserver<BasicResponse<ArrayList<ArticleBean>>, ArrayList<ArticleBean>>
    ) {
        val observable = api.articleList()
        toSubscribe(observable, observer)
    }

    fun contract(
        type: Int,
        observer: HttpObserver<BasicResponse<ArrayList<ArticleBean>>, ArrayList<ArticleBean>>
    ) {
        val observable = api.contract(type)
        toSubscribe(observable, observer)
    }

    fun balance(
        observer: HttpObserver<BasicResponse<BalanceBean>, BalanceBean>
    ) {
        val observable = api.balance()
        toSubscribe(observable, observer)
    }

    fun loanInfo(
        observer: HttpObserver<BasicResponse<ArrayList<LoanInfoBean>>, ArrayList<LoanInfoBean>>
    ) {
        val observable = api.loanInfo()
        toSubscribe(observable, observer)
    }

    fun applyWithDraw(
        pas: String,
        observer: HttpObserver<BasicResponse<Any>, Any>
    ) {
        val observable = api.applyWithdraw(pas)
        toSubscribe(observable, observer)
    }

    fun withdrawDetail(
        observer: HttpObserver<BasicResponse<ArrayList<WithdrawDetailBean>>, ArrayList<WithdrawDetailBean>>
    ) {
        val observable = api.withdrawDetail()
        toSubscribe(observable, observer)
    }

    fun getRepayList(
        observer: HttpObserver<BasicResponse<ArrayList<RepayBean>>, ArrayList<RepayBean>>
    ) {
        val observable = api.getRepayList()
        toSubscribe(observable, observer)
    }


    fun uploadDataInfo(
        education: String,
        income: String,
        purpose: String,
        house: Int,
        car: Int,
        observer: HttpObserver<BasicResponse<Any>, Any>
    ) {
        val observable = api.uploadDataInfo(education, income, purpose, house, car)
        toSubscribe(observable, observer)
    }

    fun phoneVerify(
        phone: String,
        observer: HttpObserver<BasicResponse<Any>, Any>
    ) {
        val observable = api.phoneVerify(phone)
        toSubscribe(observable, observer)
    }

    fun realInfo(
        observer: HttpObserver<BasicResponse<RealInfoBean>, RealInfoBean>
    ) {
        val observable = api.realInfo()
        toSubscribe(observable, observer)
    }

    fun identityVerify(
        name: String,
        id_card: String,
        card_img1: File,
        card_img2: File,
        observer: HttpObserver<BasicResponse<RealInfoBean>, RealInfoBean>
    ) {
        val part1: MultipartBody.Part =
            MultipartBody.Part.createFormData(
                "card_img1",
                card_img1.getName(),
                RequestBody.create("image".toMediaTypeOrNull(), card_img1)
            )
        val part2: MultipartBody.Part =
            MultipartBody.Part.createFormData(
                "card_img2",
                card_img2.getName(),
                RequestBody.create("image".toMediaTypeOrNull(), card_img2)
            )
        val observable = api.identityAuth(
            RequestBody.create("text/plain".toMediaTypeOrNull(), name),
            RequestBody.create("text/plain".toMediaTypeOrNull(), id_card),
            part1,
            part2
        )
        toSubscribe(observable, observer)
    }

    fun writeSign(
        sign: File,
        observer: HttpObserver<BasicResponse<RealInfoBean>, RealInfoBean>
    ) {
        val part: MultipartBody.Part =
            MultipartBody.Part.createFormData(
                "sign",
                sign.getName(),
                RequestBody.create("image".toMediaTypeOrNull(), sign)
            )
        val observable = api.writeSign(part)
        toSubscribe(observable, observer)
    }


    fun realBank(
        bank_user: String,
        bank_id_card: String,
        bank_name: String,
        bank_card: String,
        observer: HttpObserver<BasicResponse<Any>, Any>
    ) {
        val observable = api.realBank(bank_user, bank_id_card, bank_name, bank_card)
        toSubscribe(observable, observer)
    }

    fun changePsw(
        password: String,
        new1: String,
        new2: String,
        observer: HttpObserver<BasicResponse<Any>, Any>
    ) {
        val observable = api.changePsw(password, new1, new2)
        toSubscribe(observable, observer)
    }

    private fun <T : BasicResponse<Data>, Data> toSubscribe(
        observable: Observable<T>,
        observer: HttpObserver<T, Data>
    ) {
        observable.retry(2) { throwable ->
            NetUtil.isConnected(ConfigurationManager.getInstance().getContext())
                    && (throwable is SocketTimeoutException ||
                    throwable is ConnectException ||
                    throwable is TimeoutException)
        }.subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(observer)
    }

}