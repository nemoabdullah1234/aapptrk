package com.nicbit.traquer.common.search;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nicbit.traquer.common.BaseActivity;
import com.nicbit.traquer.common.EventsLog;
import com.nicbit.traquer.stryker.Models.ReaderSearchCasesResponse;
import com.nicbit.traquer.stryker.R;
import com.nicbit.traquer.stryker.adapter.NewSearchAdapter;
import com.nicbit.traquer.stryker.adapter.RecentHistoryAdapter;
import com.nicbit.traquer.stryker.search.SearchType;
import com.nicbit.traquer.stryker.util.PrefUtils;
import com.nicbit.traquer.stryker.util.SimpleDividerItemDecoration;
import com.nicbit.traquer.stryker.util.ZbarActivity;
import com.nicbit.traquer.stryker.view.EmptyRecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;

public abstract class BaseSearchFragment extends Fragment implements BaseActivity.CheckRecordAudio, RecentHistoryAdapter.ItemClickListener {

    @BindView(R.id.searchListView)
    public EmptyRecyclerView mRecyclerView;

    @BindView(R.id.tv_empty_view)
    LinearLayout mEmptyView;

    @BindView(R.id.searchEdTxt)
    EditText mSearchEditText;

    @BindView(R.id.voiceBtn)
    ImageView mVoiceButton;

    @BindView(R.id.cameraBtn)
    ImageView mCameraButton;

    @BindView(R.id.tab_layout)
    public TabLayout mTabLayout;

    @BindView(R.id.cancelBtn)
    public ImageView mCancelBtn;

    @BindView(R.id.rl_main)
    public RelativeLayout mRelative;

    @BindView(R.id.recentHistoryContainer)
    public View recentHistoryLayout;

    @BindView(R.id.recentHistoryListView)
    EmptyRecyclerView mRecentHistoryRecyclerView;

    RecentHistoryAdapter recentHistoryAdapter;

    @BindView(R.id.tvRecentEmptyView)
    public TextView mTvRecentEmptyView;

    public int currentSelectedTab = 0;
    protected static final int RESULT_SPEECH = 101;
    protected static final int RESULT_BARCODE = 100;


    private List<ReaderSearchCasesResponse> mSearchResponseList = new ArrayList<>();
    private NewSearchAdapter mSearchAdapter;
    public List<ReaderSearchCasesResponse> searchData = new ArrayList<>();


    private SpeechRecognizer sr;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        setTabs();
        mSearchAdapter = new NewSearchAdapter(getActivity(), mSearchResponseList);
        mRecyclerView.setAdapter(mSearchAdapter);
        showHistoryList();

        return view;
    }

    private void showHistoryList() {
        recentHistoryLayout.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
        setRecentData();
    }

    protected abstract void setTabs();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((BaseActivity) getActivity()).setCheckRecordAudio(this);

    }


    @OnClick(R.id.cameraBtn)
    void onCameraClicked() {
        Intent intent = new Intent(getActivity(), ZbarActivity.class);
        startActivityForResult(intent, RESULT_BARCODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == Activity.RESULT_OK && null != data) {
                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String q = text.get(0);
                    if (!q.equals("")) {
                        mSearchEditText.setText(q);
                        EventsLog.customEvent("SEARCH SCREEN", "SPEECH", q);
                        onSearchClicked();
                    }
                }
                break;
            }
            case RESULT_BARCODE: {
                if (resultCode == Activity.RESULT_OK) {
                    String searchData = data.getStringExtra("result");
                    mSearchEditText.setText(searchData);
                    EventsLog.customEvent("SEARCH SCREEN", "BARCODE", searchData);
                    onSearchClicked();
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    //Write your code if there's no result
                }
            }

        }

    }

    @OnClick(R.id.cancelBtn)
    void onCancelPressed() {
        getActivity().finish();
    }

    @OnClick(R.id.voiceBtn)
    void onVoiceClicked() {
        ((BaseActivity) getActivity()).checkRecordAudio();
    }

    public void openVoiceRecorder() {
        Intent intent = new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
        try {
            startActivityForResult(intent, RESULT_SPEECH);
        } catch (ActivityNotFoundException a) {
            Toast t = Toast.makeText(getActivity(),
                    "Ops! Your device doesn't support Speech to Text",
                    Toast.LENGTH_SHORT);
            t.show();
        }
    }

    void onSearchClicked() {
        String q = mSearchEditText.getText().toString().trim();
        if (!q.isEmpty()) {
            String recentSearchData;
            String recentSearch = PrefUtils.getRecentSearch();
            if (recentSearch.isEmpty())
                recentSearchData = q;
            else
                recentSearchData = q + "," + recentSearch;
            PrefUtils.setRecentSearch(recentSearchData);
            hideKeyBoard();
            doSearch(mSearchEditText.getText().toString().trim());
        } else {
            Toast.makeText(getActivity(), "Please enter search text", Toast.LENGTH_LONG).show();
        }
    }

    private void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mSearchEditText.getWindowToken(), 0);
        mSearchEditText.clearFocus();
        mCameraButton.requestFocus();

    }

    protected abstract void doSearch(String searchText);

    @OnClick(R.id.searchEdTxt)
    public void onSearchFieldClick() {
        showHistoryList();
    }

    @OnEditorAction(R.id.searchEdTxt)
    boolean onSearchClick(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            onSearchClicked();
            return true;
        }
        return false;

    }

    private void updateAdapter(List<ReaderSearchCasesResponse> list) {
        if (list.size() == 0) {
            mRecyclerView.setEmptyView(mEmptyView);
        }
        mSearchResponseList.clear();
        mSearchResponseList.addAll(list);
        mSearchAdapter.notifyDataSetChanged();
    }

    public void filterList(int position, List<ReaderSearchCasesResponse> data) {
        currentSelectedTab = position;
        SearchType searchType = SearchType.values()[position];
        switch (searchType) {
            case CASE:
                List<ReaderSearchCasesResponse> caseList = new ArrayList<>();
                for (ReaderSearchCasesResponse item : data) {
                    if (item.getType() == 0) {
                        caseList.add(item);
                    }

                }
                updateAdapter(caseList);
                break;
            case ITEM:
                List<ReaderSearchCasesResponse> itemList = new ArrayList<>();
                for (ReaderSearchCasesResponse item : data) {
                    if (item.getType() == 1) {
                        itemList.add(item);
                    }

                }
                updateAdapter(itemList);
                break;
        }
    }

    @Override
    public void onRecordAudioGranted(boolean isGranted) {
        if (isGranted)
            openVoiceRecorder();
    }

    private void setRecentData() {

        mRecentHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecentHistoryRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        String recentSearch = PrefUtils.getRecentSearch();
        List<String> recentHisoryList = null;
        if (!recentSearch.equals("")) {
            String[] split = recentSearch.split(",");
            recentHisoryList = Arrays.asList(split);
        }
        recentHistoryAdapter = new RecentHistoryAdapter(getActivity(), recentHisoryList, BaseSearchFragment.this);
        mRecentHistoryRecyclerView.setAdapter(recentHistoryAdapter);
        mRecentHistoryRecyclerView.setEmptyView(mTvRecentEmptyView);
    }

    @Override
    public void onItemClicked(String data) {
        hideKeyBoard();
        mSearchEditText.setText(data);
        EventsLog.customEvent("SEARCH SCREEN", "RECENT", data);
        doSearch(data);
    }


}