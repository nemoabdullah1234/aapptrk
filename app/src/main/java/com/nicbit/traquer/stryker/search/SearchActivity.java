package com.nicbit.traquer.stryker.search;

import android.support.v4.app.Fragment;

import com.nicbit.traquer.common.search.BaseSearchActivity;
import com.nicbit.traquer.stryker.R;

public class SearchActivity extends BaseSearchActivity {


    @Override
    protected int getLayout() {
        return R.layout.activity_search;
    }

    @Override
    protected Fragment getSearchFragment() {
        return new SearchFragment();
    }
}
