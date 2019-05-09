package com.mark.streamproject.categeory;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.mark.streamproject.R;
import com.mark.streamproject.data.Room;
import com.mark.streamproject.util.Constants;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class CategoryFragment extends Fragment implements CategoryContract.View {
    private CategoryAdapter mCategoryAdapter;

    private SearchView mSearchView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private CategoryContract.Presenter mPresenter;

    private static final String SEARCH_SOURCE_TEXT = "android:id/search_src_text";

    public CategoryFragment() {}

    public static CategoryFragment newInstance() {
        return new CategoryFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCategoryAdapter = new CategoryAdapter(mPresenter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_category, container, false);
        initSearchView(root);
        RecyclerView recyclerView = root.findViewById(R.id.recycler_category);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(mCategoryAdapter);

        mSwipeRefreshLayout = root.findViewById(R.id.refresh_category);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.loadCategoryData();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if ("".equals(mSearchView.getQuery().toString())) {
                    mPresenter.loadCategoryData();
                } else {
                    mPresenter.searchRoomData(mSearchView.getQuery().toString());
                }
            }
        });

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!"".equals(newText)) {
                    mPresenter.searchRoomData(newText);
                } else {
                    mPresenter.loadCategoryData();
                }
                return false;
            }
        });
    }



    @Override
    public void setPresenter(CategoryContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean isActive() {
        return !isHidden();
    }

    @Override
    public void showCategoryUi(ArrayList<Room> rooms) {
        mCategoryAdapter.updateData(rooms);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public boolean isRefreshing() {
        return mSwipeRefreshLayout.isRefreshing();
    }

    private void initSearchView(View view) {
        mSearchView = view.findViewById(R.id.search_category);

        int id = mSearchView.getContext().getResources().getIdentifier(SEARCH_SOURCE_TEXT, null, null);

        EditText textSearch = mSearchView.findViewById(id);
        textSearch.setTextColor(getResources().getColor(R.color.white));
        textSearch.setHintTextColor(getResources().getColor(R.color.gray));
        textSearch.setHint(getResources().getString(R.string.search_hint));

        try {
            Field fCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            fCursorDrawableRes.setAccessible(true);

            int mCursorDrawableRes = fCursorDrawableRes.getInt(textSearch);
            Field fEditor = TextView.class.getDeclaredField("mEditor");
            fEditor.setAccessible(true);

            Object editor = fEditor.get(textSearch);
            Class<?> clazz = editor.getClass();
            Field fCursorDrawable = clazz.getDeclaredField("mCursorDrawable");
            fCursorDrawable.setAccessible(true);

            if (mCursorDrawableRes <= 0) {
                return;
            }

            Drawable cursorDrawable = ContextCompat.getDrawable(mSearchView.getContext(), mCursorDrawableRes);
            if (cursorDrawable == null) {
                return;
            }

            Drawable tintDrawable = DrawableCompat.wrap(cursorDrawable);
            //custom cursor color
            DrawableCompat.setTintList(tintDrawable, ColorStateList.valueOf(getResources().getColor(R.color.yellow)));
            Drawable[] drawables = new Drawable[] {tintDrawable, tintDrawable};
            fCursorDrawable.set(editor, drawables);
        } catch (Throwable throwable) {
            Log.d(Constants.TAG, "" + throwable);
        }
    }
}
