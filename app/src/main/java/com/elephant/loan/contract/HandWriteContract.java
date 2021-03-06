package com.elephant.loan.contract;

import com.common.lib.bean.RealInfoBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

import java.io.File;

public class HandWriteContract {
    public interface View extends IView {
        public void uploadSuccess(RealInfoBean bean, String msg);

        public void uploadFailed(String msg);
    }

    public interface Presenter extends IPresenter {
        public void uploadHandWrite(File sign);
    }
}
