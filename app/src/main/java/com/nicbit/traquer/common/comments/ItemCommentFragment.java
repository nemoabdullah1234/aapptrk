package com.nicbit.traquer.common.comments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nicbit.traquer.common.issueDetail.AddNewCommentActivity;
import com.nicbit.traquer.common.issueDetail.ReportIssueContract;
import com.nicbit.traquer.common.issueDetail.ReportIssuePresenter;
import com.nicbit.traquer.common.issueDetail.UploadImageDialog;
import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.Models.Comments;
import com.nicbit.traquer.stryker.Models.GetItemCommentsResponse;
import com.nicbit.traquer.stryker.Models.Item;
import com.nicbit.traquer.stryker.Models.ItemComments;
import com.nicbit.traquer.stryker.Models.ReaderReportShippingIssueResponse;
import com.nicbit.traquer.stryker.Models.ReportIssueRequest;
import com.nicbit.traquer.stryker.R;
import com.nicbit.traquer.stryker.adapter.DividerDecoration;
import com.nicbit.traquer.stryker.adapter.IssueStickyAdapter;
import com.nicbit.traquer.stryker.adapter.NewCommentAdapter;
import com.nicbit.traquer.stryker.exception.ErrorMessageHandler;
import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.listener.SessionTokenListener;
import com.nicbit.traquer.stryker.network.SessionToken;
import com.nicbit.traquer.stryker.util.DialogClass;
import com.nicbit.traquer.stryker.util.PhotoUtility;
import com.nicbit.traquer.stryker.util.ShowLargeImageActivity;
import com.nicbit.traquer.stryker.util.StringUtils;
import com.nicbit.traquer.stryker.view.EmptyRecyclerView;
import com.nicbit.traquer.stryker.view.SpaceItemDecoration;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ItemCommentFragment extends Fragment implements CaseItemCommentsContract.View, ReportIssueContract.View, NewCommentAdapter.CommentImageClickListener, PhotoUtility.OnImageSelectListener, UploadImageDialog.OnImageUploadListener {

    @BindView(R.id.recyclerView)
    EmptyRecyclerView mRecyclerView;

    @BindView(R.id.tv_empty_view)
    LinearLayout mEmptyView;

    @BindView(R.id.btn_new_comment)
    Button mNewComment;
    boolean isCompleted = false;

    private IssueStickyAdapter mAdapter;
    private List<ItemComments> mDataList;
    private CaseItemCommentsPresenter mCaseItemCommentsPresenter;
    private ReportIssuePresenter mReportIssuePresenter;
    private String mCaseNumber;
    private String skuId;
    private PhotoUtility mPhotoUtils;
    private String mComment = "";
    File mSelectedImageFile = null;
    private String itemId;
    private String l2;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_issue_detail_new, container, false);
        ButterKnife.bind(this, view);
        mCaseItemCommentsPresenter = new CaseItemCommentsPresenter(this);
        mReportIssuePresenter = new ReportIssuePresenter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(10));
        mDataList = new ArrayList<>();
        mAdapter = new IssueStickyAdapter(getActivity(), true);
        mAdapter.addAll(mDataList);
        mRecyclerView.setAdapter(mAdapter);
        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(mAdapter);
        mRecyclerView.addItemDecoration(headersDecor);
        mRecyclerView.addItemDecoration(new DividerDecoration(getActivity()));
        mRecyclerView.setEmptyView(mEmptyView);
        skuId = getArguments().getString(StringUtils.SKU_ID);
        mCaseNumber = getArguments().getString(StringUtils.CASE_NUMBER);
        l2 = getArguments().getString(StringUtils.IntentKey.L2);
        isCompleted = getArguments().getBoolean(StringUtils.IS_COMPLETED);
        if (isCompleted) {
            mNewComment.setVisibility(View.GONE);
            isCompleted = false;
        } else {
            mNewComment.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getItemComments();
    }

    private void getItemComments() {
        DialogClass.showDialog(getActivity(), getString(R.string.please_wait));
        mCaseItemCommentsPresenter.getItemComments(mCaseNumber, skuId);
    }

    void addCommentInList(ItemComments comment) {
        mDataList.add(comment);
        mComment = "";
        if (mPhotoUtils != null) {
            mPhotoUtils.deleteImage();
        }
        mAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(mDataList.size() - 1);
    }


    @Override
    public void onItemCommentsResponseReceive(ApiResponseModel response, NicbitException e) {
        DialogClass.dismissDialog(getActivity());
        if (e == null) {
            if (response.getStatus() == StringUtils.SUCCESS_STATUS) {
                GetItemCommentsResponse responseData = response.getData().getGetItemCommentsResponse();
                if (responseData != null) {
                    setData(responseData);
                }
            } else if (response.getCode() == 209) {
                SessionToken.get(new SessionTokenListener() {
                    @Override
                    public void onSessionTokenUpdate(boolean isUpdate) {
                        if (isUpdate) {
                            getItemComments();
                        }
                    }
                }, getActivity());
            } else {
                ErrorMessageHandler.handleErrorMessage(response.getCode(), getActivity());
            }
        } else {
            DialogClass.alerDialog(getActivity(), getResources().getString(R.string.check_internet_connection));
        }
    }

    private void setData(GetItemCommentsResponse responseData) {
        Item itemDetails = responseData.getItemDetails();
        if (itemDetails != null) {
            itemId = itemDetails.getL1();
            if (itemDetails.getIsCompleted() == 0) {

            } else {

            }
        }


        List<Comments> comments = responseData.getComments();
        if (comments != null && comments.size() > 0) {
            mDataList.clear();
            mDataList = filterList(comments);
            mAdapter.addAll(mDataList);

        }
    }

    @OnClick(R.id.btn_new_comment)
    void onNewCommentClick() {
        Intent intent = new Intent(getActivity(), AddNewCommentActivity.class);

        intent.putExtra("caseNo", mCaseNumber);
        intent.putExtra("skuId", skuId);
        intent.putExtra("itemId", itemId);
        intent.putExtra(StringUtils.IntentKey.L2, l2);
        getActivity().startActivity(intent);
    }

    private ReportIssueRequest getReportIssueData() {
        ReportIssueRequest request = new ReportIssueRequest();
        request.setCaseNo(mCaseNumber);
        request.setSkuId(skuId);
        request.setComment(mComment);
        return request;
    }


    @Override
    public void onPostCommentResponseReceive(ApiResponseModel response, NicbitException e) {

    }

    @Override
    public void onReportIssueResponseReceive(ApiResponseModel response, NicbitException e) {
        DialogClass.dismissDialog(getActivity());
        if (e == null) {
            if (response.getStatus() == StringUtils.SUCCESS_STATUS) {
                mSelectedImageFile = null;
                Toast.makeText(getContext(), R.string.issue_submitted, Toast.LENGTH_SHORT).show();
                ReaderReportShippingIssueResponse responseData = response.getData().getReaderPostCaseItemCommentResponse();
                if (responseData.getComment() != null) {
                    addCommentInList(responseData.getComment());
                }

            } else if (response.getCode() == 209) {
                SessionToken.get(new SessionTokenListener() {
                    @Override
                    public void onSessionTokenUpdate(boolean isUpdate) {
                        if (isUpdate) {
//
                        }
                    }
                }, getActivity());
            } else {
                ErrorMessageHandler.handleErrorMessage(response.getCode(), getActivity());
            }
        } else {
            DialogClass.alerDialog(getActivity(), getResources().getString(R.string.check_internet_connection));
        }
    }


    @Override
    public void onImageClick(String url) {
        Intent intent = new Intent(getActivity(), ShowLargeImageActivity.class);
        intent.putExtra(StringUtils.IntentKey.IMAGE_URL, url);
        startActivity(intent);
    }


    @Override
    public void onImageSelect(File file) {
        mSelectedImageFile = file;
        Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        showUploadImageDialog(myBitmap);
    }

    private void showUploadImageDialog(Bitmap bitmap) {
        UploadImageDialog dialog = UploadImageDialog.newInstance(this, bitmap, mComment);
        dialog.show(getActivity().getFragmentManager(), "dialog");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mPhotoUtils != null)
            mPhotoUtils.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onUploadClick(String comment) {
        mComment = comment;
    }

    @Override
    public void onUploadCancel() {
        mComment = "";
        mSelectedImageFile = null;
        if (mPhotoUtils != null) {
            mPhotoUtils.deleteImage();
        }
    }

    public List<ItemComments> filterList(List<Comments> commentses) {

        List<ItemComments> itemCommentsList = new ArrayList<>();
        String previousDate = "";
        long id = 1;

        for (Comments comments : commentses) {
            if (!comments.getCommentDate().equalsIgnoreCase(previousDate)) {
                previousDate = comments.getCommentDate();
                id++;
            }
            List<ItemComments> itemCommentses = comments.getItemComments();
            for (ItemComments itemComments : itemCommentses) {
                itemComments.setItemId(id);
                itemCommentsList.add(itemComments);
            }

        }

        return itemCommentsList;
    }
}
