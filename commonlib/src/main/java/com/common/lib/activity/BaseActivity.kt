package com.common.lib.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Html
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.common.lib.constant.Constants
import com.common.lib.constant.EventBusEvent
import com.common.lib.fragment.BaseFragment
import com.common.lib.mvp.IPresenter
import com.common.lib.utils.BaseUtils
import com.common.lib.utils.LogUtil
import com.common.lib.utils.StringUtil
import com.gyf.immersionbar.ImmersionBar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.InputStream
import java.net.URL
import java.util.concurrent.TimeUnit

abstract class BaseActivity<P : IPresenter> : BaseDialogActivity(), View.OnClickListener {
    companion object {
        const val TAG = "BaseActivity"
    }

    protected var presenter: P? = null

    protected abstract fun getLayoutId(): Int

    protected abstract fun updateUIText()

    protected abstract fun onCreated(savedInstanceState: Bundle?)

    protected abstract fun onCreatePresenter(): P

    protected lateinit var context: Context

    protected val compositeDisposable = CompositeDisposable()
    protected val mFragments: ArrayList<BaseFragment<*>> = ArrayList()
    protected var mCurrentFragment: BaseFragment<*>? = null
    protected var mCurrentFragmentPosition = 0

    protected var isFinish = false

    /**
     * onCreate 後 的第一次 onResume 不調用onActivityResume
     * **/
    protected var mCreated = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCreated = true
        context = this
        isFinish = false
        setContentView(getLayoutId())
        EventBus.getDefault().register(this)
        createAndBindPresenter()
        initImmersionBar()
        onCreated(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        if (!mCreated) {
            mCurrentFragment?.onActivityResume()
        } else {
            mCreated = false
        }
        updateUIText()
    }

    override fun onStart() {
        super.onStart()
        requestExternalStoragePermission()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        unbindPresenter()
        isFinish = true
        compositeDisposable.clear()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onReceive(map: HashMap<String, Any>) {
        if (map.containsKey(EventBusEvent.FINISH_ACTIVITIES)) {
            finish()
        }
    }

    override fun onLogout() {
        presenter?.logout()
    }

    private fun createAndBindPresenter() {
        unbindPresenter()
        presenter = onCreatePresenter()
        presenter?.onBind()
    }

    private fun unbindPresenter() {
        presenter?.onUnbind()
        presenter = null
    }

    open fun showKeyboard(view: View?) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, 0)
    }

    open fun hideKeyboard(view: View?) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (view == null) {
            currentFocus?.let {
                imm.hideSoftInputFromWindow(it.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        } else {
            imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    protected open fun setTopStatusBarStyle(topView: View) {
        topView.setPadding(0, BaseUtils.getStatusBarHeight(resources) + topView.paddingTop, 0, 0)
    }

    protected open fun setTopStatusBarStyle(viewId: Int) {
        val topView = findViewById<View>(viewId)
        topView.setPadding(0, BaseUtils.getStatusBarHeight(resources) + topView.paddingTop, 0, 0)
    }

    protected fun setViewsOnClickListener(vararg views: View) {
        for (view in views) {
            view.setOnClickListener(this)
        }
    }

    protected fun setViewsOnClickListener(vararg ids: Int) {
        for (id in ids) {
            findViewById<View>(id)?.setOnClickListener(this)
        }
    }

    protected fun setBackgroundColor(id: Int, colorResId: Int) {
        findViewById<View>(id).setBackgroundColor(ContextCompat.getColor(this, colorResId))
    }

    protected fun setBackground(id: Int, drawableId: Int) {
        findViewById<View>(id).setBackgroundResource(drawableId)
    }

    protected fun setTextColor(tv: TextView, colorResId: Int) {
        tv.setTextColor(ContextCompat.getColor(this, colorResId))
    }

    protected fun setTextColor(id: Int, colorResId: Int) {
        findViewById<TextView>(id).setTextColor(ContextCompat.getColor(this, colorResId))
    }

    protected fun setText(id: Int, strId: Int) {
        findViewById<TextView>(id).setText(getString(strId))
    }

    protected fun setText(id: Int, str: String) {
        findViewById<TextView>(id).setText(str)
    }

    protected fun setImage(id: Int, resId: Int) {
        findViewById<ImageView>(id).setImageResource(resId)
    }

    protected fun setHtml(id: Int, str: String) {
        findViewById<TextView>(id).setText(
            Html.fromHtml(
                str,
                Html.ImageGetter { source ->
                    var `is`: InputStream? = null
                    try {
                        `is` = URL(source).content as InputStream
                        val d =
                            Drawable.createFromStream(`is`, "src")
                        d.setBounds(
                            0, 0, d.intrinsicWidth,
                            d.intrinsicHeight
                        )
                        `is`!!.close()
                        return@ImageGetter d
                    } catch (e: Exception) {
                        return@ImageGetter null
                    }
                }, null
            )
        )
    }

    protected fun getTextById(id: Int): String {
        return findViewById<TextView>(id).text.toString().trim()
    }

    protected fun setTextHintByServerKey(et: EditText, serverKey: String) {
        et.hint = getTextByKey(serverKey)
    }

    protected fun setViewVisible(vararg views: View) {
        for (view in views) {
            view.visibility = View.VISIBLE
        }
    }

    protected fun setViewVisible(vararg ids: Int) {
        for (id in ids) {
            findViewById<View>(id)?.visibility = View.VISIBLE
        }
    }

    protected fun setViewGone(vararg views: View) {
        for (view in views) {
            view.visibility = View.GONE
        }
    }

    protected fun setViewGone(vararg ids: Int) {
        for (id in ids) {
            findViewById<View>(id)?.visibility = View.GONE
        }
    }

    protected fun setViewInvisible(vararg views: View) {
        for (view in views) {
            view.visibility = View.INVISIBLE
        }
    }

    fun openActivity(cls: Class<*>) {
        openActivity(cls, null)
    }

    fun openActivity(pagerClass: Class<*>, bundle: Bundle?) {
        if (Activity::class.java.isAssignableFrom(pagerClass)) {
            val intent = Intent(this, pagerClass)
            if (bundle != null) {
                intent.putExtras(bundle)
            }
            startActivity(intent)
        } else {
            val name: String = pagerClass.name
            val intent = Intent(this, EmptyActivity::class.java)
            if (bundle != null) {
                intent.putExtras(bundle)
            }
            intent.putExtra(EmptyActivity.KEY_FRAGMENT_NAME, name)
            startActivity(intent)
        }
    }

    /**
     * 点后退按钮触发
     *
     * @param view
     */
    open fun onBackClick(view: View) {
        onBackPressed()
    }

    fun getTextByKey(key: String): String {
        return StringUtil.getString(this, key)
    }

    /**
     * 初始化状态栏
     */
    protected open fun initImmersionBar() {
        ImmersionBar.with(this)
                .statusBarDarkFont(true) //状态栏字体是深色，不写默认为亮色
                .init()
    }

    open fun getDisplayMetrics(): DisplayMetrics? {
        val displayMetrics = DisplayMetrics()
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics
    }

    private fun openPhotoPreviewActivity(path: String) {
        val bundle = Bundle()
        bundle.putString(Constants.BUNDLE_EXTRA, path)
        openActivity(PhotoPreviewActivity::class.java, bundle)
    }

    fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    fun showToast(resId: Int) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
    }

    fun lockView(view: View?) {
        view?.isClickable = false
        compositeDisposable.add(
                Single.timer(100, TimeUnit.MILLISECONDS)
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            view?.isClickable = true
                        }, {
                            Log.e(BaseFragment.TAG, "lockView fail: " + it.toString())
                        })
        )
    }

    open fun getContainerViewId(): Int {
        return 0
    }

    fun switchFragment(to: BaseFragment<*>) {
        var hasSelectedFragment = false
        if (mCurrentFragment !== to) {
            val ft = supportFragmentManager.beginTransaction()
            if (!to.isAdded) {
                if (mCurrentFragment != null) {
                    ft.hide(mCurrentFragment!!)
                }
                ft.add(getContainerViewId(), to).commitAllowingStateLoss()
            } else {
                if (mCurrentFragment != null) {
                    ft.hide(mCurrentFragment!!)
                }
                ft.show(to).commitAllowingStateLoss()
                hasSelectedFragment = true
            }
        }
        mCurrentFragment = to
        for ((i, f) in mFragments.withIndex()) {
            if (mCurrentFragment == f) {
                mCurrentFragmentPosition = i
                break
            }
        }

        if (hasSelectedFragment) {
            mCurrentFragment?.onSelectFragment()
        }
    }
}


