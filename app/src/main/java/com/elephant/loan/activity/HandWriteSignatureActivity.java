package com.elephant.loan.activity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.common.lib.activity.BaseActivity;
import com.common.lib.mvp.contract.EmptyContract;
import com.common.lib.mvp.presenter.EmptyPresenter;
import com.common.lib.utils.BaseUtils;
import com.common.lib.view.DrawView;
import com.elephant.loan.R;
import com.elephant.loan.contract.HandWriteContract;
import com.elephant.loan.presenter.HandWritePresenter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

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
        setViewsOnClickListener(R.id.tvSubmit);
    }

    @NotNull
    @Override
    protected HandWriteContract.Presenter onCreatePresenter() {
        return new HandWritePresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSubmit:
                DrawView drawView = findViewById(R.id.drawView);
                Bitmap bitmap = Bitmap.createBitmap(drawView.getWidth(), drawView.getHeight(),
                        Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                v.draw(canvas);
                getPresenter().uploadHandWrite(BaseUtils.StaticParams.savePng(bitmap, this));
                break;
        }
    }

    @Override
    public void uploadSuccess(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg);
        }
        finish();
    }
}
