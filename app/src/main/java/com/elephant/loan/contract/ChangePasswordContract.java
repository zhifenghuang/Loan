package com.elephant.loan.contract;

import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

public class ChangePasswordContract {
    public interface View extends IView {
        public void changePswSuccess(String msg);
    }

    public interface Presenter extends IPresenter {
        public void changePassword(String password, String new1, String new2);
    }
}
