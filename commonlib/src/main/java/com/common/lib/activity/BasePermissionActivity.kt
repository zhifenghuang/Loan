package com.common.lib.activity

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.common.lib.interfaces.PermissionCallBack
import com.common.lib.utils.PermissionUtil
import java.util.*

abstract class BasePermissionActivity : AppCompatActivity() {

    private var requestPermissionRequestCode = -1
    private var mPermissionCallBack: PermissionCallBack? = null

    fun requestPermission(
        callback: PermissionCallBack?,
        vararg permissions: String
    ) {
        val uncheckPermissions = PermissionUtil.getUncheckPermissions(this, *permissions)
        requestPermissionRequestCode = Random().nextInt(10000)
        if (uncheckPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                uncheckPermissions.toTypedArray(),
                requestPermissionRequestCode
            )
            mPermissionCallBack = callback
        } else {
            mPermissionCallBack?.onSuccess()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestPermissionRequestCode != requestCode) {
            return
        }

        var isAllGranted = true
        for (result in grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                isAllGranted = false
                break
            }
        }

        if (isAllGranted) {
            mPermissionCallBack?.onSuccess()
        } else {
            mPermissionCallBack?.onFailure()
        }
        mPermissionCallBack = null
    }

    open fun requestExternalStoragePermission() {
        val uncheckPermission = PermissionUtil.getUncheckPermissions(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
        )

        if (uncheckPermission.isNotEmpty()) {
            requestPermission(null, *uncheckPermission.toTypedArray())
        }
    }

    /**
     * @return has permission ?
     * **/
    fun requestCameraPermission(): Boolean {
        val permission = Manifest.permission.CAMERA
        return if (!PermissionUtil.isGrantPermission(this, permission)) {
            requestPermission(object : PermissionCallBack {
                override fun onSuccess() {
                    onGetCameraPermission()
                }

                override fun onFailure() {
                }

            }, permission)
            false
        } else {
            true
        }
    }

    /**
     * @return has permission ?
     * **/
    fun requestCameraVideoPermission(): Boolean {
        val permission = Manifest.permission.CAMERA
        return if (!PermissionUtil.isGrantPermission(this, permission)) {
            requestPermission(object : PermissionCallBack {
                override fun onSuccess() {
                    onGetCameraVideoPermission()
                }

                override fun onFailure() {
                }

            }, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
            false
        } else {
            true
        }
    }

    open fun onGetCameraPermission() {

    }

    open fun onGetCameraVideoPermission() {

    }

    open fun requestGalleryPermission(type: Int): Boolean {
        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
        return if (!PermissionUtil.isGrantPermission(this, permission)) {
            requestPermission(object : PermissionCallBack {
                override fun onSuccess() {
                    if (type == 0) {
                        onGetGalleryPermission()
                    } else {
                        onGetGalleryVideoPermission()
                    }
                }

                override fun onFailure() {
                }

            }, permission)
            false
        } else {
            true
        }
    }

    open fun onGetGalleryPermission() {

    }

    open fun onGetGalleryVideoPermission() {

    }

    open fun requestPhonePermission(): Boolean {
        val permission = Manifest.permission.READ_PHONE_STATE
        return if (!PermissionUtil.isGrantPermission(this, permission)) {
            requestPermission(object : PermissionCallBack {
                override fun onSuccess() {
                    onGetPhonePermission()
                }

                override fun onFailure() {
                }

            }, permission)
            false
        } else {
            true
        }
    }

    open fun onGetPhonePermission() {


    }

}