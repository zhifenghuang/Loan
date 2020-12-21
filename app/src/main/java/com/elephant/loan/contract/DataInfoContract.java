package com.elephant.loan.contract;

import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

import java.util.ArrayList;

public class DataInfoContract {
    public interface View extends IView {
        public void uploadSuccess(String msg);
    }

    public interface Presenter extends IPresenter {
        public void uploadDataInfo(String education, String income, String purpose, int house, int car);
    }
}
