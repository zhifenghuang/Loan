package com.common.lib.network

import com.common.lib.bean.*
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*


interface Api {

    @POST("api/user/reg")
    fun register(
        @Query("phone") phone: String,
        @Query("password") password: String,
        @Query("code") code: String,
        @Query("device_type") device_type: Int,
        @Query("device_info") device_info: String,
        @Query("device_key") device_key: String
    ): Observable<BasicResponse<Any>>

    @POST("api/user/login")
    fun login(
        @Query("phone") phone: String,
        @Query("password") password: String
    ): Observable<BasicResponse<String>>

    @POST("api/user/forget")
    fun forgetPassword(
        @Query("phone") phone: String,
        @Query("code") code: String,
        @Query("password") password: String
    ): Observable<BasicResponse<Any>>

    @GET("api/user/sms")
    fun sms(
        @Query("phone") phone: String
    ): Observable<BasicResponse<Any>>

    @GET("api/params/index")
    fun paramsIndex(
        @Query("device_type") device_type: Int,
        @Query("device_info") device_info: String,
        @Query("device_key") device_key: String
    ): Observable<BasicResponse<ArrayList<HashMap<String, String>>>>

    @GET("api/params/banner")
    fun banner(): Observable<BasicResponse<String>>

    @GET("api/params/kfurl")
    fun serviceUrl(): Observable<BasicResponse<String>>

    @GET("api/article/about")
    fun aboutUs(): Observable<BasicResponse<ArrayList<ArticleBean>>>

    @GET("api/article/list")
    fun articleList(): Observable<BasicResponse<ArrayList<ArticleBean>>>

    @GET("api/cashout/umoney")
    fun balance(): Observable<BasicResponse<BalanceBean>>

    @GET("api/loan/info")
    fun loanInfo(): Observable<BasicResponse<ArrayList<LoanInfoBean>>>

    @POST("api/params/cpwd")
    fun changePsw(
        @Query("password") password: String,
        @Query("new1") new1: String,
        @Query("new2") new2: String
    ): Observable<BasicResponse<Any>>

    @GET("api/real/info")
    fun realInfo(): Observable<BasicResponse<RealInfoBean>>

    @POST("api/real/information")
    fun uploadDataInfo(
        @Query("education") education: String,
        @Query("income") income: String,
        @Query("purpose") purpose: String,
        @Query("house") house: Int,
        @Query("car") car: Int
    ): Observable<BasicResponse<Any>>

    @POST("api/real/phone")
    fun uploadRealPhone(
        @Query("phone") phone: String,
        @Query("code") code: String
    ): Observable<BasicResponse<Any>>

    @POST("api/loan/apply")
    fun applyLoan(
        @Query("amount") amount: String,
        @Query("a_class") code: String,
        @Query("term") term: String
    ): Observable<BasicResponse<Any>>

    @Multipart
    @POST("api/real/idcard")
    fun identityAuth(
        @Part("name") name: RequestBody,
        @Part("id_card") id_card: RequestBody,
        @Part card_img1: MultipartBody.Part,
        @Part card_img2: MultipartBody.Part
    ): Observable<BasicResponse<RealInfoBean>>

    @Multipart
    @POST("api/real/writesign")
    fun writeSign(
        @Part sign: MultipartBody.Part
    ): Observable<BasicResponse<RealInfoBean>>

    @POST("api/real/bank")
    fun realBank(
        @Query("bank_user") bank_user: String,
        @Query("bank_id_card") bank_id_card: String,
        @Query("bank_name") bank_name: String,
        @Query("bank_card") bank_card: String
    ): Observable<BasicResponse<Any>>


    @POST("api/real/phone")
    fun phoneVerify(
        @Query("phone") phone: String
    ): Observable<BasicResponse<Any>>

    @POST("api/cashout/apply")
    fun applyWithdraw(
        @Query("code") code: String
    ): Observable<BasicResponse<Any>>

    @GET("api/cashout/info")
    fun withdrawDetail(): Observable<BasicResponse<ArrayList<WithdrawDetailBean>>>

    @GET("api/loan/repay")
    fun getRepayList(): Observable<BasicResponse<ArrayList<RepayBean>>>
}