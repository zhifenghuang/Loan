package com.elephant.loan.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
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
import com.common.lib.utils.BitmapUtil;
import com.common.lib.utils.ChooseImageUtils;
import com.common.lib.utils.CompressionBitmapUtil;
import com.common.lib.utils.LogUtil;
import com.elephant.loan.R;
import com.elephant.loan.contract.IdentityInfoContract;
import com.elephant.loan.presenter.IdentityInfoPresenter;
import com.google.gson.Gson;
import com.jakewharton.rxbinding3.widget.RxTextView;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

public class IdentityInfoActivity extends BaseActivity<IdentityInfoContract.Presenter> implements IdentityInfoContract.View {

    private static final int CER_FRONT_PHOTO = 1000;
    private static final int CER_BACK_PHOTO = 1001;

    private static final long PHOTO_MAX_SIZE = 2 * 1024 * 1024;  //图片最大size

    private File mCerFrontFile, mCerBackFile;
    private boolean mIsAllBlank = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_identity_info;
    }

    @Override
    protected void updateUIText() {

    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tv_title, R.string.app_identity_info);

        RealInfoBean realInfoBean = DataManager.Companion.getInstance().getMyInfo();
        if (TextUtils.isEmpty(realInfoBean.getName())) {
            initInputListener();
            mIsAllBlank = false;
            setViewsOnClickListener(R.id.tvSubmit, R.id.rlCerFront, R.id.rlCerBack);
        } else {
            setViewGone(R.id.etRealName, R.id.etCerNo, R.id.tvCerBack, R.id.tvCerFront);
            setViewVisible(R.id.tvRealName, R.id.tvCerNo);
            TextView tvSubmit = findViewById(R.id.tvSubmit);
            tvSubmit.setBackgroundResource(R.drawable.app_shape_879cf5_25);
            tvSubmit.setEnabled(false);
            tvSubmit.setText(getString(R.string.app_had_submit));
            setText(R.id.tvRealName, realInfoBean.getName());
            setText(R.id.tvCerNo, realInfoBean.getId_card());
            LogUtil.LogE(new Gson().toJson(realInfoBean));

            String cardImg = realInfoBean.getCard_img1().replace("\\", "/");
            BaseUtils.StaticParams.loadImage(this, 0,
                    HttpMethods.Companion.getInstance().getBaseUrl() + cardImg,
                    findViewById(R.id.ivCerFront));
            cardImg = realInfoBean.getCard_img2().replace("\\", "/");
            BaseUtils.StaticParams.loadImage(this, 0,
                    HttpMethods.Companion.getInstance().getBaseUrl() + cardImg,
                    findViewById(R.id.ivCerBack));
        }
    }

    @NotNull
    @Override
    protected IdentityInfoContract.Presenter onCreatePresenter() {
        return new IdentityInfoPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSubmit:
                String cerNo = getTextById(R.id.etCerNo);
                if (cerNo.length() != 13) {
                    showToast(R.string.app_please_input_cer_no);
                    return;
                }
                getPresenter().uploadIdentityInfo(getTextById(R.id.etRealName), cerNo, mCerFrontFile, mCerBackFile);
                break;
            case R.id.rlCerFront:
                showSelectPhotoDialog(CER_FRONT_PHOTO);
                break;
            case R.id.rlCerBack:
                showSelectPhotoDialog(CER_BACK_PHOTO);
                break;
        }
    }

    @Override
    public void uploadSuccess(RealInfoBean bean, String msg) {
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg);
        }
        RealInfoBean realInfoBean = DataManager.Companion.getInstance().getMyInfo();
        realInfoBean.setName(bean.getName());
        realInfoBean.setId_card(bean.getId_card());
        realInfoBean.setCard_img1(bean.getCard_img1());
        realInfoBean.setCard_img2(bean.getCard_img2());
        DataManager.Companion.getInstance().saveMyInfo(realInfoBean);
        finish();
    }

    @Override
    public void onBackClick(View view) {
        RealInfoBean realInfoBean = DataManager.Companion.getInstance().getMyInfo();
        if (TextUtils.isEmpty(realInfoBean.getName())) {
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

    private void showSelectPhotoDialog(final int code) {
        final MyDialogFragment dialogFragment = new MyDialogFragment(R.layout.layout_select_media_type);
        dialogFragment.setOutClickDismiss(false);
        dialogFragment.setOnMyDialogListener(new MyDialogFragment.OnMyDialogListener() {
            @Override
            public void initView(View view) {
                ((TextView) view.findViewById(R.id.btnTakePhoto)).setText(getString(R.string.app_take_photo));
                ((TextView) view.findViewById(R.id.btnAlbum)).setText(getString(R.string.app_album));
                ((TextView) view.findViewById(R.id.btnCancel)).setText(getString(R.string.app_cancel));
                dialogFragment.setDialogViewsOnClickListener(view, R.id.btnTakePhoto, R.id.btnAlbum, R.id.btnCancel);
            }

            @Override
            public void onViewClick(int viewId) {
                switch (viewId) {
                    case R.id.btnTakePhoto:
                        ChooseImageUtils.openCamera(IdentityInfoActivity.this, code);
                        break;
                    case R.id.btnAlbum:
                        ChooseImageUtils.chooseImage(IdentityInfoActivity.this, code);
                        break;
                }
            }
        });
        dialogFragment.show(getSupportFragmentManager(), "MyDialogFragment");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null) {
                ArrayList<ImageItem> imageItems = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                switch (requestCode) {
                    case CER_FRONT_PHOTO:
                    case CER_BACK_PHOTO:
                        if (imageItems != null && imageItems.size() > 0) {
                            File file = new File(imageItems.get(0).path);
                            if (file.length() > PHOTO_MAX_SIZE) {   //大于2M压缩处理
                                Bitmap bmp = BitmapUtil.INSTANCE.getBitmapFromFile(file, getDisplayMetrics().widthPixels, getDisplayMetrics().heightPixels);
                                bmp.recycle();
                                file = BaseUtils.StaticParams.saveJpeg(bmp, this);
                            } else {
                                String newPath = BaseUtils.StaticParams.getSaveFilePath(this, UUID.randomUUID().toString() + ".jpg");
                                BaseUtils.StaticParams.copyFile(file.getAbsolutePath(), newPath);
                                file = new File(newPath);
                            }
                            if (requestCode == CER_FRONT_PHOTO) {
                                mCerFrontFile = file;
                                BaseUtils.StaticParams.loadImage(this, 0, mCerFrontFile, findViewById(R.id.ivCerFront));
                            } else {
                                mCerBackFile = file;
                                BaseUtils.StaticParams.loadImage(this, 0, mCerBackFile, findViewById(R.id.ivCerBack));
                            }
                            TextView tvSubmit = findViewById(R.id.tvSubmit);
                            if (mIsAllBlank && mCerFrontFile != null && mCerBackFile != null) {
                                tvSubmit.setBackgroundResource(R.drawable.app_shape_4968ea_25);
                                tvSubmit.setEnabled(true);
                            } else {
                                tvSubmit.setBackgroundResource(R.drawable.app_shape_879cf5_25);
                                tvSubmit.setEnabled(false);
                            }
                        }
                        break;
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initInputListener() {

        final TextView tvSubmit = findViewById(R.id.tvSubmit);
        tvSubmit.setBackgroundResource(R.drawable.app_shape_879cf5_25);
        tvSubmit.setEnabled(false);

        final EditText etRealName = findViewById(R.id.etRealName);
        final EditText etCerNo = findViewById(R.id.etCerNo);

        Observable.combineLatest(RxTextView.textChanges(etRealName).skip(1),
                RxTextView.textChanges(etCerNo).skip(1),
                (BiFunction<CharSequence, CharSequence, Boolean>) (charSequence, charSequence2) -> {
                    boolean isRealNameValid = !TextUtils.isEmpty(getTextById(R.id.etRealName));
                    boolean isCerNoValid = !TextUtils.isEmpty(getTextById(R.id.etCerNo));
                    return isRealNameValid && isCerNoValid;
                }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                mIsAllBlank = aBoolean;
                if (mIsAllBlank && mCerFrontFile != null && mCerBackFile != null) {
                    tvSubmit.setBackgroundResource(R.drawable.app_shape_4968ea_25);
                    tvSubmit.setEnabled(true);
                } else {
                    tvSubmit.setBackgroundResource(R.drawable.app_shape_879cf5_25);
                    tvSubmit.setEnabled(false);
                }
            }
        });
    }
}
