package com.common.lib.manager

import android.text.TextUtils
import com.common.lib.bean.BalanceBean
import com.common.lib.bean.RealInfoBean
import com.common.lib.utils.PrefUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class DataManager private constructor() {


    private var mMyUserInfo: RealInfoBean? = null

    private val mGson = Gson()

    companion object {
        const val TAG = "DataManager"

        @Volatile
        private var instance: DataManager? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance
                    ?: DataManager()
                        .also { instance = it }
            }
    }

    fun saveMyInfo(myInfo: RealInfoBean?) {
        if (myInfo == null) {
            mMyUserInfo = null
            PrefUtil.putString(ConfigurationManager.getInstance().getContext(), "user", "")
            return
        }
        mMyUserInfo = myInfo
        PrefUtil.putString(
            ConfigurationManager.getInstance().getContext(),
            "user",
            mGson.toJson(myInfo)
        )
    }


    fun getMyInfo(): RealInfoBean? {
        if (mMyUserInfo == null) {
            val str =
                PrefUtil.getString(ConfigurationManager.getInstance().getContext(), "user", "")
            if (TextUtils.isEmpty(str)) {
                return null
            }
            mMyUserInfo = mGson.fromJson(str, RealInfoBean::class.java)
        }
        return mMyUserInfo
    }

    fun saveLoanInfo(list: ArrayList<HashMap<String, String>>) {
        PrefUtil.putString(
            ConfigurationManager.getInstance().getContext(),
            "loan_info",
            mGson.toJson(list)
        )
    }


    fun getLoanInfo(): ArrayList<HashMap<String, String>>? {
        val str =
            PrefUtil.getString(ConfigurationManager.getInstance().getContext(), "loan_info", "")
        if (TextUtils.isEmpty(str)) {
            return null
        }
        return mGson.fromJson(
            str,
            object : TypeToken<ArrayList<HashMap<String, String>>?>() {}.type
        )
    }

    fun saveToken(token: String) {
        PrefUtil.putString(ConfigurationManager.getInstance().getContext(), "token", token)
    }

    fun getToken(): String {
        return PrefUtil.getString(ConfigurationManager.getInstance().getContext(), "token", "")
    }

    fun saveBalance(bean: BalanceBean?) {
        PrefUtil.putString(
            ConfigurationManager.getInstance().getContext(),
            "balance",
            if (bean == null) {
                ""
            } else {
                mGson.toJson(bean)
            }
        )
    }

    fun getBalance(): BalanceBean? {
        val str =
            PrefUtil.getString(ConfigurationManager.getInstance().getContext(), "balance", "")
        if (TextUtils.isEmpty(str)) {
            return null
        }
        return mGson.fromJson(str, BalanceBean::class.java)
    }


    fun saveKeyboardHeight(keyboardHeight: Int) {
        PrefUtil.putInt(
            ConfigurationManager.getInstance().getContext(),
            "keyboard_height",
            keyboardHeight
        )
    }

    fun getKeyboardHeight(): Int {
        return PrefUtil.getInt(
            ConfigurationManager.getInstance().getContext(),
            "keyboard_height",
            0
        )
    }

    fun logout() {
        saveMyInfo(null)
        saveToken("")
        saveBalance(null)
    }
}