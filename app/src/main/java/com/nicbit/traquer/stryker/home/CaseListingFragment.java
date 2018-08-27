package com.nicbit.traquer.stryker.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.Models.ReaderGetCasesResponse;
import com.nicbit.traquer.stryker.R;
import com.nicbit.traquer.stryker.adapter.SwipeRecyclerViewAdapter;
import com.nicbit.traquer.stryker.caseDetail.CaseDetailActivity;
import com.nicbit.traquer.stryker.enums.CaseType;
import com.nicbit.traquer.stryker.exception.ErrorMessageHandler;
import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.listener.SessionTokenListener;
import com.nicbit.traquer.stryker.network.SessionToken;
import com.nicbit.traquer.stryker.util.DialogClass;
import com.nicbit.traquer.stryker.util.SimpleDividerItemDecoration;
import com.nicbit.traquer.stryker.util.StringUtils;
import com.nicbit.traquer.stryker.view.EmptyRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CaseListingFragment extends Fragment implements DashBoardContract.View, SwipeRefreshLayout.OnRefreshListener, SwipeRecyclerViewAdapter.CaseAdapterViewClickListener {

    @BindView(R.id.listView)
    EmptyRecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.tv_empty_view)
    LinearLayout mEmptyView;
    private DashBoardContract.UserActionsListener mActionsListener;
    private SwipeRecyclerViewAdapter mCaseAdapter;
    private ArrayList<ReaderGetCasesResponse> casesResponseList = new ArrayList<>();
    public int currentSelectedTab;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionsListener = new DashboardPresenter(this);
    }

    public void getCases() {
        DialogClass.showDialog(getActivity(), getActivity().getString(R.string.please_wait));
        mActionsListener.getCases();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mCaseAdapter = new SwipeRecyclerViewAdapter(this, new ArrayList<ReaderGetCasesResponse>(), true);
        mRecyclerView.setAdapter(mCaseAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                mSwipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        getCases();
        return view;
    }

    @Override
    public void onGetCasesDone(ApiResponseModel response, NicbitException e) {
        DialogClass.dismissDialog(getActivity());
        if (e == null) {
            if (response.getStatus() == StringUtils.SUCCESS_STATUS) {
                casesResponseList = response.getData().getReaderGetCasesResponse();
                filterList(currentSelectedTab);
            } else if (response.getCode() != null && response.getCode() == 209) {
                SessionToken.get(new SessionTokenListener() {
                    @Override
                    public void onSessionTokenUpdate(boolean isUpdate) {
                        if (isUpdate) {
                            getCases();
                        }
                    }
                }, getActivity());
            } else {
                stopRefresh();
                ErrorMessageHandler.handleErrorMessage(response.getCode(), getActivity());
            }
        } else {
            stopRefresh();
            DialogClass.alerDialog(getActivity(), getResources().getString(R.string.check_internet_connection));
        }
    }

    @Override
    public void CasesFromCacheReceived(List<ReaderGetCasesResponse> caseList) {
        DialogClass.dismissDialog(getActivity());
        updateAdapter(caseList);

    }

    @Override
    public void onCaseFlagUpdated(ApiResponseModel response, NicbitException e) {
        DialogClass.dismissDialog(getActivity());
    }

    private void updateAdapter(List<ReaderGetCasesResponse> list) {
        if (list.size() == 0) {
            mRecyclerView.setEmptyView(mEmptyView);
        }
        mSwipeRefreshLayout.setRefreshing(false);
        mCaseAdapter.addAll(true, list);
        mCaseAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        getCases();
    }

    public void filterList(int position) {
        currentSelectedTab = position;
        CaseType caseType = CaseType.values()[position];
        switch (caseType) {
            case ALL:
                updateAdapter(casesResponseList);
                break;
            case WATCH:
                ArrayList<ReaderGetCasesResponse> filterList = new ArrayList<>();
                for (ReaderGetCasesResponse item : casesResponseList) {
                    if (item.getIsWatched() == 1) {
                        filterList.add(item);
                    }
                    updateAdapter(filterList);
                }
                break;
            case EXCEPTION:
                ArrayList<ReaderGetCasesResponse> exceptionList = new ArrayList<>();
                for (ReaderGetCasesResponse item : casesResponseList) {
                    if (item.getIsReported() == 1) {
                        exceptionList.add(item);
                    }
                    updateAdapter(exceptionList);
                }
                break;
        }


    }

    @Override
    public void onReportCaseClicked(ReaderGetCasesResponse readerCase) {

    }

    @Override
    public void onItemClick(ReaderGetCasesResponse readerCase) {
        Intent i = new Intent(getActivity(), CaseDetailActivity.class);
        i.putExtra(StringUtils.CASE_NUMBER, readerCase.getCaseId());
        i.putExtra(StringUtils.ID, readerCase.getId());
        i.putExtra(StringUtils.IS_DASHBOARD, true);
        getActivity().startActivity(i);
    }

    @Override
    public void onWatchCaseClicked(ReaderGetCasesResponse readerCase) {
        int isWatched;
        if (readerCase.getIsWatched() == 0) {
            isWatched = 1;
        } else {
            isWatched = 0;
        }
        DialogClass.showDialog(getActivity(), getActivity().getString(R.string.please_wait));
        mActionsListener.updateCaseFlag(isWatched, readerCase.getId());
        for (ReaderGetCasesResponse caseItem : casesResponseList) {
            if (caseItem.getId().equals(readerCase.getId())) {
                caseItem.setIsWatched(isWatched);
            }
            filterList(currentSelectedTab);
        }
    }

    private void stopRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
        mRecyclerView.setEmptyView(mEmptyView);
    }

    public void scrollToPosition(int i) {
        mRecyclerView.smoothScrollToPosition(0);
    }
}
