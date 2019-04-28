package com.mark.streamproject.categeory;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import com.mark.streamproject.R;

import java.lang.reflect.Field;


public class CategoryFragment extends Fragment implements CategoryContract.View {

    private SearchView mSearchView;

    private CategoryContract.Presenter mPresenter;

    public CategoryFragment() {}

    public static CategoryFragment newInstance() {
        return new CategoryFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_category, container, false);
        initSearchView(root);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void initSearchView(View view) {
        mSearchView = view.findViewById(R.id.search_category);
        int id = mSearchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);

        EditText textSearch = (EditText) mSearchView.findViewById(id);
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
        } catch (Throwable ignored) {
        }
    }

    @Override
    public void setPresenter(CategoryContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean isActive() {
        return !isHidden();
    }
}
