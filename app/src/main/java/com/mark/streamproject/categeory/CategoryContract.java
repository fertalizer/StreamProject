package com.mark.streamproject.categeory;

import com.mark.streamproject.base.BasePresenter;
import com.mark.streamproject.base.BaseView;
import com.mark.streamproject.data.Room;

import java.util.ArrayList;

/**
 * Created by Mark Ho on April 2019.
 *
 * This specifies the contract between the view and the presenter.
 */
public interface CategoryContract {

    interface View extends BaseView<Presenter> {
        boolean isActive();

        void showCategoryUi(ArrayList<Room> rooms);

        boolean isRefreshing();
    }

    interface Presenter extends BasePresenter {

        void loadCategoryData();

        void setCategoryData(ArrayList<Room> rooms);

        void openRoom(Room room);

        boolean isCategoryRefreshing();

        void searchRoomData(String string);

    }
}