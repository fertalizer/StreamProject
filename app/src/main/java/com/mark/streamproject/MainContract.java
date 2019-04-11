package com.mark.streamproject;

import com.mark.streamproject.base.BasePresenter;
import com.mark.streamproject.base.BaseView;

/**
 * Created by Mark Ho on April 2019.
 *
 * This specifies the contract between the view and the presenter.
 */
public interface MainContract {
    interface View extends BaseView<Presenter> {

        void openHotsUi();

        void openCategoryUi();

        void openFollowUi();

    }

    interface Presenter extends BasePresenter {

        void openHots();

        void openCategory();

        void openFollow();

    }
}
