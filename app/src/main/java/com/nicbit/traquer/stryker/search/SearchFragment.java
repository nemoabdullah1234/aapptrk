package com.nicbit.traquer.stryker.search;

import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.nicbit.traquer.common.EventsLog;
import com.nicbit.traquer.common.search.BaseSearchFragment;
import com.nicbit.traquer.common.search.SearchTabClass;
import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.Models.ReaderSearchCasesResponse;
import com.nicbit.traquer.stryker.R;
import com.nicbit.traquer.stryker.exception.ErrorMessageHandler;
import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.listener.SessionTokenListener;
import com.nicbit.traquer.stryker.network.SessionToken;
import com.nicbit.traquer.stryker.util.DialogClass;
import com.nicbit.traquer.stryker.util.StringUtils;

import java.util.List;

public class SearchFragment extends BaseSearchFragment implements SearchContract.View {

    public SearchContract.UserActionsListener mActionsListener;
    private SearchTabClass tabClass;
    String mSearchText = "";

    public void setTabs() {
        mActionsListener = new SearchPresenter(this);
        tabClass = new SearchTabClass(getActivity(), new String[]{"Orders", "Products"});

        for (int i = 0; i < 2; i++) {
            mTabLayout.addTab(mTabLayout.newTab());
            mTabLayout.getTabAt(i).setCustomView(tabClass.getTabView(i));
        }
        mTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getContext(), R.color.traquer_yellow));
        TabLayout.Tab tab = mTabLayout.getTabAt(0);
        tab.select();
        tabClass.getSelectedTabView(0, tab);


        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentSelectedTab = tab.getPosition();
                filterList(currentSelectedTab, searchData);
                tabClass.getSelectedTabView(tab.getPosition(), tab);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tabClass.getUnSelectedTabView(tab.getPosition(), tab);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    protected void doSearch(String searchText) {
        DialogClass.showDialog(getActivity(), getActivity().getString(R.string.please_wait));
        mActionsListener.searchCases(searchText);
        mSearchText = searchText;
    }


    @Override
    public void onSearchCasesResponse(ApiResponseModel response, NicbitException e) {
        DialogClass.dismissDialog(getActivity());
        if (e == null) {
            recentHistoryLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            if (response.getStatus() == StringUtils.SUCCESS_STATUS) {
                List<ReaderSearchCasesResponse> data = response.getData().getReaderSearchCasesResponse();
                searchData.clear();
                searchData.addAll(data);
                filterList(currentSelectedTab, searchData);
                EventsLog.customEvent("SEARCH SCREEN", "SUCCESS", "YES");
            } else if (response.getCode() == 209) {
                SessionToken.get(new SessionTokenListener() {
                    @Override
                    public void onSessionTokenUpdate(boolean isUpdate) {
                        if (isUpdate) {
                            doSearch(mSearchText);
                        }
                    }
                }, getActivity());
            } else {
                EventsLog.customEvent("SEARCH SCREEN", "SUCCESS", response.getMessage());
                ErrorMessageHandler.handleErrorMessage(response.getCode(), getActivity());
            }
        } else {
            DialogClass.alerDialog(getActivity(), getResources().getString(R.string.check_internet_connection));
        }
    }


    @Override
    public void onFlagUpdated(ApiResponseModel response, NicbitException e) {
        DialogClass.dismissDialog(getActivity());
    }


}
