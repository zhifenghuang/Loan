package com.elephant.loan.contract;

import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

public class LoginContract {
    public interface View extends IView {
        public void getVerCodeSuccess();

        public void loginSuccess();
    }

    public interface Presenter extends IPresenter {
        public void getVerCode(String phone);

        public void login(String phone, String password);

        public void register(String phone, String password, String code);

        public void installIndex();
    }
}
