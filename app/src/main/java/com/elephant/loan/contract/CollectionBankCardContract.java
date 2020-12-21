package com.elephant.loan.contract;

import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

import java.io.File;

public class CollectionBankCardContract {
    public interface View extends IView {
        public void uploadSuccess(String msg);
    }

    public interface Presenter extends IPresenter {
        public void uploadBankInfo(String bank_user, String bank_id_card, String bank_name, String bank_card);
    }
}
