package com.elephant.loan.activity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.RealInfoBean;
import com.common.lib.dialog.MyDialogFragment;
import com.common.lib.interfaces.OnClickCallback;
import com.common.lib.manager.DataManager;
import com.common.lib.network.HttpMethods;
import com.common.lib.utils.BaseUtils;
import com.common.lib.view.DrawView;
import com.elephant.loan.R;
import com.elephant.loan.contract.HandWriteContract;
import com.elephant.loan.presenter.HandWritePresenter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HandWriteSignatureActivity extends BaseActivity<HandWriteContract.Presenter> implements HandWriteContract.View {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_hand_write_signature;
    }

    @Override
    protected void updateUIText() {

    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tv_title, R.string.app_hand_mark);
        final TextView tvSubmit = findViewById(R.id.tvSubmit);
        tvSubmit.setBackgroundResource(R.drawable.app_shape_879cf5_25);
        tvSubmit.setEnabled(false);
        RealInfoBean realInfoBean = DataManager.Companion.getInstance().getMyInfo();
        if (TextUtils.isEmpty(realInfoBean.getSign())) {
            DrawView drawView = findViewById(R.id.drawView);
            drawView.setOnDrawListener(new DrawView.OnDrawListener() {
                @Override
                public void onDraw(boolean isDrawed) {
                    if (isDrawed) {
                        tvSubmit.setBackgroundResource(R.drawable.app_shape_4968ea_25);
                        tvSubmit.setEnabled(true);
                    } else {
                        tvSubmit.setBackgroundResource(R.drawable.app_shape_879cf5_25);
                        tvSubmit.setEnabled(false);
                    }
                }
            });
            setViewVisible(R.id.tv_right);
            setText(R.id.tv_right, R.string.app_write_again);
            setViewsOnClickListener(R.id.tvSubmit, R.id.tv_right);
        } else {
            setViewGone(R.id.ll1, R.id.tvTip);
            setViewVisible(R.id.ll2, R.id.tvYourSign);
            String sign = realInfoBean.getSign().replace("\\", "/");
            BaseUtils.StaticParams.loadImage(this, 0,
                    HttpMethods.Companion.getInstance().getBaseUrl() + sign,
                    findViewById(R.id.ivSign));
        }
    }

    @NotNull
    @Override
    protected HandWriteContract.Presenter onCreatePresenter() {
        return new HandWritePresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right:
                DrawView drawView = findViewById(R.id.drawView);
                drawView.reDraw();
                break;
            case R.id.tvSubmit:
                drawView = findViewById(R.id.drawView);
                Bitmap bitmap = Bitmap.createBitmap(drawView.getWidth(), drawView.getHeight(),
                        Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                drawView.draw(canvas);
                getPresenter().uploadHandWrite(BaseUtils.StaticParams.savePng(bitmap, this));
                bitmap.recycle();
                break;
        }
    }

    @Override
    public void uploadSuccess(RealInfoBean bean, String msg) {
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg);
        }
        RealInfoBean realInfoBean = DataManager.Companion.getInstance().getMyInfo();
        realInfoBean.setSign(bean.getSign());
        DataManager.Companion.getInstance().saveMyInfo(realInfoBean);
        finish();
    }

    @Override
    public void onBackClick(View view) {
        RealInfoBean realInfoBean = DataManager.Companion.getInstance().getMyInfo();
        if (TextUtils.isEmpty(realInfoBean.getSign())) {
            showTwoBtnDialog(null,
                    getString(R.string.app_sure_back),
                    ContextCompat.getColor(this, R.color.color_0d_0d_0d),
                    getString(R.string.app_cancel),
                    getString(R.string.app_ok),
                    new OnClickCallback() {
                        @Override
                        public void onClick(int viewId) {
                            if (viewId == R.id.btn2) {
                                finish();
                            }
                        }
                    });
        } else {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            onBackClick(null);
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    @Override
    public void uploadFailed(String msg) {
        showUploadFailedDialog();
    }

    private int mTotalTime;

    private void showUploadFailedDialog() {
        final MyDialogFragment dialogFragment = new MyDialogFragment(R.layout.layout_loan_status_dialog);
        dialogFragment.setOutClickDismiss(false);
        dialogFragment.setOnMyDialogListener(new MyDialogFragment.OnMyDialogListener() {
            @Override
            public void initView(View view) {
                ((ImageView) view.findViewById(R.id.ivStatus)).setImageResource(R.drawable.jd_home_t_zhu);
                ((TextView) view.findViewById(R.id.tvStatus)).setText(getString(R.string.app_failed));
                ((TextView) view.findViewById(R.id.tvTip)).setText(getString(R.string.app_failed_reason));
                mTotalTime = 3;
                countTime(view.findViewById(R.id.tvCloseTip), dialogFragment);
            }

            @Override
            public void onViewClick(int viewId) {

            }
        });
        dialogFragment.show(getSupportFragmentManager(), "MyDialogFragment");
    }

    private void countTime(final TextView tv, final MyDialogFragment dialogFragment) {
        if (isDestroyed()) {
            return;
        }
        tv.setText(getString(R.string.app_the_window_will_close_xxx_second, String.valueOf(mTotalTime)));
        tv.postDelayed(new Runnable() {
            @Override
            public void run() {
                --mTotalTime;
                if (mTotalTime == 0) {
                    dialogFragment.dismiss();
                } else {
                    countTime(tv, dialogFragment);
                }
            }
        }, 1000);
    }
}
