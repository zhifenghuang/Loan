package com.common.lib.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.common.lib.R

class MyDialogFragment(layout: Int) : DialogFragment(), View.OnClickListener {

    private var mLayout = 0

    init {
        mLayout = layout
    }

    private var mOnMyDialogListener: OnMyDialogListener? = null

    private var mListener: IDismissListener? = null

    private var mIsClickDismiss = true

    private var mIsNeedDismiss = false

    private var mOutClickDismiss = false


    fun setOnMyDialogListener(onMyDialogListener: OnMyDialogListener?) {
        mOnMyDialogListener = onMyDialogListener
    }

    /**
     * 必须在OnMyDialogListener的initView中调
     *
     * @param dialogView
     * @param viewIds
     */
    fun setDialogViewsOnClickListener(
        dialogView: View?,
        vararg viewIds: Int
    ) {
        if (viewIds == null || dialogView == null) {
            return
        }
        for (viewId in viewIds) {
            dialogView.findViewById<View>(viewId).setOnClickListener(this)
        }
    }

    fun setClickDismiss(isClickDismiss: Boolean) {
        mIsClickDismiss = isClickDismiss
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.dialog_fragment_style)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mLayout == 0) {
            return null
        }
        val view = inflater.inflate(mLayout, container, false)
        if (mOnMyDialogListener != null) {
            mOnMyDialogListener!!.initView(view)
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window!!.setLayout(width, height)
            dialog.setCanceledOnTouchOutside(mOutClickDismiss)
        }
    }

    override fun onResume() {
        super.onResume()
        if (mIsNeedDismiss) {
            dismiss()
        }
    }

    fun setNeedDismiss(isNeedDismiss: Boolean) {
        mIsNeedDismiss = isNeedDismiss
    }

    fun setOutClickDismiss(outClickDismiss: Boolean) {
        mOutClickDismiss = outClickDismiss
    }

    override fun onClick(view: View) {
        if (mIsClickDismiss) {
            dismiss()
        }
        if (mOnMyDialogListener != null) {
            mOnMyDialogListener!!.onViewClick(view.id)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (mListener != null) {
            mListener!!.onDismiss()
        }
    }

    fun setOnDismiss(listener: IDismissListener?) {
        mListener = listener
    }


    interface OnMyDialogListener {
        fun initView(view: View?)
        fun onViewClick(viewId: Int)
    }

    interface IDismissListener {
        fun onDismiss()
    }
}