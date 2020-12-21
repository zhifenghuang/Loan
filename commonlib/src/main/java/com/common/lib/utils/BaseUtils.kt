package com.common.lib.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.common.lib.constant.Constants.JPG_EXTENSION
import com.common.lib.utils.LanguageUtil.getLanguage
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class BaseUtils {
    companion object StaticParams {

        fun createImagePath(context: Context): String {
            val fileName: String = UidUtil.createUid() + JPG_EXTENSION
            val dirPath = Environment.getExternalStorageDirectory()
                .toString() + "/Android/data/" + context.packageName + "/download"
            val file = File(dirPath)
            if (!file.exists() || !file.isDirectory) file.mkdirs()
            return "$dirPath/$fileName"
        }

        fun getSaveFilePath(
            context: Context?,
            fileName: String
        ): String {
            val dirPath = Environment.getExternalStorageDirectory()
                .toString() + "/Android/data/" + context!!.packageName + "/download"
            val file = File(dirPath)
            if (!file.exists() || !file.isDirectory) file.mkdirs()
            return "$dirPath/$fileName"
        }

        /**
         * dp转px
         *
         * @param context
         * @param dipValue
         * @return
         */
        fun dp2px(context: Context?, dipValue: Float): Int {
            val scale = context!!.resources.displayMetrics.density
            return (dipValue * scale + 0.5f).toInt()
        }

        /**
         * Return the status bar's height.
         *
         * @return the status bar's height
         */
        fun getStatusBarHeight(resources: Resources): Int {
            val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
            return resources.getDimensionPixelSize(resourceId)
        }

        fun getTextByKey(context: Context?, key: String): String {
            var value = PrefUtil.getString(context, key + "_${getLanguage()}", "")
            if (!TextUtils.isEmpty(value)) {
                return value!!
            }
            try {
                val stringId = context!!.resources.getIdentifier(
                    key,
                    "string", context.packageName
                )
                // 取出配置的string文件中的默认值
                value = context.resources.getString(stringId)

            } catch (e: Exception) {
                value = ""
            }
            return value!!
        }

        /**
         * 获取diviceId,在测试升级时可能用到，上线时可以不再获取，可重写此方法返回一个固定的字符串，如：android，
         * 这样就可以不添加读取手机状态的权限
         * 需要增加 权限     <uses-permission android:name="android.permission.READ_PHONE_STATE"></uses-permission>
         *
         * @param context
         * @return
         */
        @SuppressLint("MissingPermission")
        fun getDeviceId(context: Context): String? {
            var deviceId = PrefUtil.getString(context, "deviceId", "")
            if (TextUtils.isEmpty(deviceId)) {
                if (!PermissionUtil.isGrantPermission(
                        context,
                        Manifest.permission.READ_PHONE_STATE
                    )
                ) {
                    deviceId = Settings.System.getString(
                        context.contentResolver,
                        Settings.Secure.ANDROID_ID
                    )
                    PrefUtil.putString(context, "deviceId", deviceId)
                    return deviceId
                }
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                    val telephonyManager =
                        context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                    deviceId = telephonyManager.deviceId
                    if (deviceId == null) {
                        deviceId = Settings.Secure.getString(
                            context.contentResolver,
                            Settings.Secure.ANDROID_ID
                        )
                        if (deviceId == null) {
                            deviceId = ""
                        }
                    }
                } else {
                    deviceId = Settings.System.getString(
                        context.contentResolver,
                        Settings.Secure.ANDROID_ID
                    )
                }
                PrefUtil.putString(context, "deviceId", deviceId)
            }
            return deviceId
        }


        fun getMap(key: String, value: Any): HashMap<String, Any> {
            val map = HashMap<String, Any>()
            map.put(key, value)
            return map
        }

        fun getNewText(number: Int): String? {
            return if (number < 10) "0$number" else number.toString()
        }

        /**
         * 保存JPG图片
         *
         * @param bmp
         */
        fun saveJpeg(bmp: Bitmap, context: Context): File {
            val jpegFile = File(createImagePath(context))
            var fout: FileOutputStream? = null
            var bos: BufferedOutputStream? = null
            try {
                fout = FileOutputStream(jpegFile)
                bos = BufferedOutputStream(fout)
                bmp.compress(Bitmap.CompressFormat.JPEG, 70, bos)
            } catch (e: IOException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            } finally {
                try {
                    fout?.close()
                    if (bos != null) {
                        bos.flush()
                        bos.close()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return jpegFile
        }


        /**
         * 保存PNG图片
         *
         * @param bmp
         */
        fun savePng(bmp: Bitmap, context: Context): File {
            val jpegFile = File(createImagePath(context))
            var fout: FileOutputStream? = null
            var bos: BufferedOutputStream? = null
            try {
                fout = FileOutputStream(jpegFile)
                bos = BufferedOutputStream(fout)
                bmp.compress(Bitmap.CompressFormat.PNG, 100, bos)
            } catch (e: IOException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            } finally {
                try {
                    fout?.close()
                    if (bos != null) {
                        bos.flush()
                        bos.close()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return jpegFile
        }

        /**
         * 保存JPG图片
         *
         * @param bmp
         */
        fun saveJpeg(bmp: Bitmap, jpegFile: File): File {
            var fout: FileOutputStream? = null
            var bos: BufferedOutputStream? = null
            try {
                fout = FileOutputStream(jpegFile)
                bos = BufferedOutputStream(fout)
                bmp.compress(Bitmap.CompressFormat.JPEG, 70, bos)
            } catch (e: IOException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            } finally {
                try {
                    fout?.close()
                    if (bos != null) {
                        bos.flush()
                        bos.close()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return jpegFile
        }

        /**
         * 显示圆形图片
         *
         * @param context   上下文
         * @param defaultId
         * @param url       图片路径
         * @param imageView 图片view
         */
        fun displayCircleImageView(
            context: Context,
            defaultId: Int,
            url: String?,
            imageView: ImageView?
        ) {
            Glide.with(context.applicationContext)
                .load(url)
                .placeholder(defaultId)
                .error(defaultId)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(imageView!!)
        }

        /**
         * 复制文件
         *
         * @param oldPath
         * @param newPath
         */
        fun copyFile(oldPath: String?, newPath: String?): Boolean {
            var isSuccessful = false
            var inStream: InputStream? = null
            var fs: FileOutputStream? = null
            try {
                var byteread = 0
                val oldfile = File(oldPath)
                if (oldfile.exists()) {
                    inStream = FileInputStream(oldPath) //读入原文件
                    fs = FileOutputStream(newPath)
                    val buffer = ByteArray(1024)
                    while (inStream.read(buffer).also { byteread = it } != -1) {
                        fs.write(buffer, 0, byteread)
                    }
                    isSuccessful = true
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                isSuccessful = false
            } finally {
                try {
                    if (inStream != null) {
                        inStream.close()
                        inStream = null
                    }
                    if (fs != null) {
                        fs.close()
                        fs = null
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    isSuccessful = false
                }
            }
            return isSuccessful
        }

        /**
         * 显示圆角图片
         *
         * @param context   上下文
         * @param defaultId
         * @param url       图片路径
         * @param imageView 图片view
         */
        fun displayRoundImageView(
            context: Context,
            defaultId: Int,
            url: String?,
            imageView: ImageView?,
            radius: Int
        ) {
            Glide.with(context.applicationContext)
                .load(url)
                .placeholder(defaultId)
                .error(defaultId)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(radius)))
                .into(imageView!!)
        }

        /**
         * @param defaultId
         * @param path
         * @param iv
         */
        fun loadImage(
            context: Context,
            defaultId: Int,
            path: String?,
            iv: ImageView?
        ) {
            Glide.with(context.applicationContext)
                .load(path)
                .apply(
                    RequestOptions()
                        .placeholder(defaultId)
                        .error(defaultId)
                        .centerCrop() //中心切圖, 會填滿
                        .fitCenter() //中心fit, 以原本圖片的長寬為主
                        .diskCacheStrategy(DiskCacheStrategy.DATA) //                      .dontAnimate()
                )
                .into(iv!!)
        }

        /**
         * @param defaultId
         * @param path
         * @param iv
         */
        fun loadImage(
            context: Context,
            defaultId: Int,
            file: File,
            iv: ImageView?
        ) {
            Glide.with(context.applicationContext)
                .load(Uri.fromFile(file))
                .apply(
                    RequestOptions()
                        .placeholder(defaultId)
                        .error(defaultId)
                        .centerCrop() //中心切圖, 會填滿
                        .fitCenter() //中心fit, 以原本圖片的長寬為主
                        .diskCacheStrategy(DiskCacheStrategy.DATA) //                      .dontAnimate()
                )
                .into(iv!!)
        }

        /**
         * @param defaultId
         * @param path
         * @param iv
         */
        fun loadImage(
            context: Context,
            defaultId: Int,
            file: File,
            path: String?,
            iv: ImageView?
        ) {
            if (file.exists()) {
                loadImage(context, defaultId, file, iv)
            } else {
                loadImage(context, defaultId, path, iv)
            }
        }

        /**
         * @param defaultId
         * @param path
         * @param iv
         */
        fun loadImage(
            context: Context,
            defaultId: Int,
            path: Uri?,
            iv: ImageView?
        ) {
            Glide.with(context.applicationContext)
                .load(path)
                .apply(
                    RequestOptions()
                        .placeholder(defaultId)
                        .error(defaultId)
                        .centerCrop() //中心切圖, 會填滿
                        .fitCenter() //中心fit, 以原本圖片的長寬為主
                        .diskCacheStrategy(DiskCacheStrategy.DATA) //                      .dontAnimate()
                )
                .into(iv!!)
        }

        /**
         * 显示圆角图片
         *
         * @param context   上下文
         * @param defaultId
         * @param url       图片路径
         * @param imageView 图片view
         */
        fun displayLocalRoundImageView(
            context: Context,
            defaultId: Int,
            uri: Uri,
            imageView: ImageView,
            radius: Int
        ) {
            Glide.with(context.applicationContext)
                .load(uri)
                .placeholder(defaultId)
                .error(defaultId)
                .apply(
                    RequestOptions()
                        .transforms(
                            CenterCrop(), RoundedCorners(radius)
                        )
                )
                .into(imageView)
        }

        /**
         * 显示圆角图片
         *
         * @param context   上下文
         * @param defaultId
         * @param url       图片路径
         * @param imageView 图片view
         */
        fun displayNetRoundImageView(
            context: Context,
            defaultId: Int,
            url: String,
            imageView: ImageView,
            radius: Int
        ) {
            Glide.with(context.applicationContext)
                .load(url)
                .placeholder(defaultId)
                .error(defaultId)
                .transform(CenterCrop(), RoundedCorners(radius))
                .into(imageView)
        }

        /**
         * 显示圆角图片
         *
         * @param context   上下文
         * @param defaultId
         * @param file
         * @param url       图片路径
         * @param imageView 图片view
         */
        fun displayLocalOrNetRoundImageView(
            context: Context,
            defaultId: Int,
            file: File,
            url: String,
            imageView: ImageView,
            radius: Int
        ) {
            if (file.exists()) {
                displayLocalRoundImageView(
                    context,
                    defaultId,
                    Uri.fromFile(file),
                    imageView,
                    radius
                )
            } else {
                displayNetRoundImageView(context, defaultId, url, imageView, radius)
            }
        }

        fun longToDate(time: Long): String? {
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val date = Date(time)
            return sdf.format(date)
        }

        fun longToDate2(time: Long): String? {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
            val date = Date(time)
            return sdf.format(date)
        }

        fun longToDate3(time: Long): String? {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date = Date(time)
            return sdf.format(date)
        }

        fun dateStrToLong2(DateTime: String?): Long {
            try {
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
                val time = sdf.parse(DateTime)
                return time.time
            } catch (e: java.lang.Exception) {
            }
            return 0
        }

        fun longToChatTime(time: Long): String? {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = System.currentTimeMillis()
            val year = calendar[Calendar.YEAR]
            val month = calendar[Calendar.MONTH] + 1
            val day = calendar[Calendar.DAY_OF_MONTH]
            calendar.timeInMillis = time
            val year1 = calendar[Calendar.YEAR]
            val month1 = calendar[Calendar.MONTH] + 1
            val day1 = calendar[Calendar.DAY_OF_MONTH]
            val hour1 = calendar[Calendar.HOUR_OF_DAY]
            val minute1 = calendar[Calendar.MINUTE]
            if (year != year1) {
                return year1.toString() + "-" + getNewText(month1) + "-" + getNewText(day1) + " " + getNewText(hour1) + ":" + getNewText(minute1)
            } else if (month != month1 || day != day1) {
                return getNewText(month1) + "-" + getNewText(day1) + " " + getNewText(hour1) + ":" + getNewText(minute1)
            }
            return getNewText(hour1) + ":" + getNewText(minute1)
        }

        fun isShowTime(lastTime: Long, time: Long): Boolean {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = lastTime
            val year = calendar[Calendar.YEAR]
            val month = calendar[Calendar.MONTH] + 1
            val day = calendar[Calendar.DAY_OF_MONTH]
            val hour = calendar[Calendar.HOUR_OF_DAY]
            val minute = calendar[Calendar.MINUTE]
            calendar.timeInMillis = time
            val year1 = calendar[Calendar.YEAR]
            val month1 = calendar[Calendar.MONTH] + 1
            val day1 = calendar[Calendar.DAY_OF_MONTH]
            val hour1 = calendar[Calendar.HOUR_OF_DAY]
            val minute1 = calendar[Calendar.MINUTE]
            if (year != year1 || month != month1 || day != day1 || hour != hour1) {
                return true
            }
            return if (minute / 10 != minute1 / 10) {
                true
            } else false
        }

        fun getTimeStrOnlyHour(time: Long): String? {
            val mSdf = SimpleDateFormat("HH:mm")
            val dt = Date(time)
            return mSdf.format(dt)
        }
    }

}

