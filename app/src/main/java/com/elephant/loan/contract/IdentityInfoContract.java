package com.elephant.loan.contract;

import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

import java.io.File;

public class IdentityInfoContract {
    public interface View extends IView {
        public void uploadSuccess(String msg);
    }

    public interface Presenter extends IPresenter {
        public void uploadIdentityInfo(String name, String id_card, File card_img1, File card_img2);
    }
}
