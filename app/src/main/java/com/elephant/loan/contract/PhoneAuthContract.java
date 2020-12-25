package com.elephant.loan.contract;

import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

public class PhoneAuthContract {
    public interface View extends IView {
        public void phoneAuthSuccess(String msg);

        public void uploadFailed(String msg);
    }

    public interface Presenter extends IPresenter {
        public void phoneAuth(String phone);
    }
}
