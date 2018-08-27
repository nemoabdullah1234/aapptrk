package com.nicbit.traquer.stryker.returns;

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
import com.nicbit.traquer.stryker.exception.ErrorMessageHandler;
import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.listener.SessionTokenListener;
import com.nicbit.traquer.stryker.network.SessionToken;
import com.nicbit.traquer.stryker.returns.detail.CaseHistoryDetailActivity;
import com.nicbit.traquer.stryker.util.DialogClass;
import com.nicbit.traquer.stryker.util.PrefUtils;
import com.nicbit.traquer.stryker.util.SimpleDividerItemDecoration;
import com.nicbit.traquer.stryker.util.StringUtils;
import com.nicbit.traquer.stryker.view.EmptyRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CaseHistoryFragment extends Fragment implements CaseHistoryContract.View, SwipeRefreshLayout.OnRefreshListener, SwipeRecyclerViewAdapter.CaseAdapterViewClickListener {

    @BindView(R.id.caseHistorylistView)
    EmptyRecyclerView mRecyclerView;

    @BindView(R.id.tv_empty_view)
    LinearLayout mEmptyView;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private CaseHistoryContract.UserActionsListener mActionsListener;
    private SwipeRecyclerViewAdapter mCaseHistoryAdapter;
    private ArrayList<ReaderGetCasesResponse> mCasesHistoryResponseList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void getCasesHistory() {
        DialogClass.showDialog(getActivity(), getActivity().getString(R.string.please_wait));
        mActionsListener.getCaseHistory(PrefUtils.getSortBy(), PrefUtils.getSortOrder());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_case_history, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));


        mSwipeRefreshLayout.setOnRefreshListener(this);
        mActionsListener = new CaseHistoryPresenter(this);
        mCasesHistoryResponseList = new ArrayList<>();
        mCaseHistoryAdapter = new SwipeRecyclerViewAdapter(this, mCasesHistoryResponseList, false);
        mRecyclerView.setAdapter(mCaseHistoryAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0) ?
                        0 : mRecyclerView.getChildAt(0).getTop();
                mSwipeRefreshLayout.setEnabled((topRowVerticalPosition >= 0));
            }
        });
        getCasesHistory();
    }

    private void updateAdapter(List<ReaderGetCasesResponse> list) {
        if (list.size() == 0) {
            mRecyclerView.setEmptyView(mEmptyView);
        }
        mSwipeRefreshLayout.setRefreshing(false);
        mCasesHistoryResponseList.clear();
        mCasesHistoryResponseList.addAll(list);
        mCaseHistoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        getCasesHistory();
    }

    @Override
    public void onCaseHistoryResponse(ApiResponseModel response, NicbitException e) {
        DialogClass.dismissDialog(getActivity());
        if (e == null) {
            if (response.getStatus() == StringUtils.SUCCESS_STATUS) {
                List<ReaderGetCasesResponse> data = response.getData().getReaderGetCasesHistoryResponse();
                if (data != null) {
                    updateAdapter(data);
                }
            } else if (response.getCode() == 209) {
                SessionToken.get(new SessionTokenListener() {
                    @Override
                    public void onSessionTokenUpdate(boolean isUpdate) {
                        if (isUpdate) {
                            getCasesHistory();
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

    private void stopRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
        mRecyclerView.setEmptyView(mEmptyView);
    }

    @Override
    public void onCaseHistoryFlagUpdated(ApiResponseModel response, NicbitException e) {
        DialogClass.dismissDialog(getActivity());

    }


    @Override
    public void onReportCaseClicked(ReaderGetCasesResponse readerCase) {

    }

    @Override
    public void onItemClick(ReaderGetCasesResponse readerCase) {
        Intent i = new Intent(getActivity(), CaseHistoryDetailActivity.class);
        i.putExtra(StringUtils.CASE_NUMBER, readerCase.getCaseId());
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
        mActionsListener.updateCaseHistoryFlag(isWatched, readerCase.getId());
        for (ReaderGetCasesResponse caseItem : mCasesHistoryResponseList) {
            if (caseItem.getId().equals(readerCase.getId())) {
                caseItem.setIsWatched(isWatched);
            }
            mCaseHistoryAdapter.notifyDataSetChanged();
        }
    }
}