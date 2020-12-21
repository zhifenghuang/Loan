package com.elephant.loan.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.RealInfoBean;
import com.common.lib.constant.Constants;
import com.common.lib.manager.DataManager;
import com.common.lib.utils.BaseUtils;
import com.common.lib.utils.BitmapUtil;
import com.common.lib.utils.ChooseImageUtils;
import com.common.lib.utils.CompressionBitmapUtil;
import com.elephant.loan.R;
import com.elephant.loan.contract.IdentityInfoContract;
import com.elephant.loan.presenter.IdentityInfoPresenter;
import com.jakewharton.rxbinding3.widget.RxTextView;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;

import org.greenrobot.eventbus.EventBus;
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
        initInputListener();
        mIsAllBlank = false;
        setViewsOnClickListener(R.id.tvSubmit, R.id.rlCerFront, R.id.rlCerBack);
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
                getPresenter().uploadIdentityInfo(getTextById(R.id.etRealName), getTextById(R.id.etCerNo), mCerFrontFile, mCerBackFile);
                break;
            case R.id.rlCerFront:
                ChooseImageUtils.chooseImage(this, CER_FRONT_PHOTO);
                break;
            case R.id.rlCerBack:
                ChooseImageUtils.chooseImage(this, CER_BACK_PHOTO);
                break;
        }
    }

    @Override
    public void uploadSuccess(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg);
        }
        RealInfoBean bean = DataManager.Companion.getInstance().getMyInfo();
        bean.setName(getTextById(R.id.etRealName));
        bean.setId_card(getTextById(R.id.etCerNo));
        DataManager.Companion.getInstance().saveMyInfo(bean);
        finish();
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
                            File file = new File(CompressionBitmapUtil.compressImage(imageItems.get(0).path));
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
