package com.nicbit.traquer.stryker.notification;

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
import android.widget.Toast;

import com.nicbit.traquer.common.issueDetail.IssueDetailActivity;
import com.nicbit.traquer.common.utils.Constant;
import com.nicbit.traquer.stryker.Models.ReaderGetNotificationsResponse;
import com.nicbit.traquer.stryker.Models.RemoveNotificationRequest;
import com.nicbit.traquer.stryker.Models.newModels.NewParams;
import com.nicbit.traquer.stryker.Models.newModels.NotificationApiResponse;
import com.nicbit.traquer.stryker.R;
import com.nicbit.traquer.stryker.adapter.NotificationListAdapter;
import com.nicbit.traquer.stryker.caseDetail.CaseDetailActivity;
import com.nicbit.traquer.stryker.exception.ErrorMessageHandler;
import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.listener.SessionTokenListener;
import com.nicbit.traquer.stryker.network.SessionToken;
import com.nicbit.traquer.stryker.util.DialogClass;
import com.nicbit.traquer.stryker.util.LocationBluetoothPermissionUtility;
import com.nicbit.traquer.stryker.util.SimpleDividerItemDecoration;
import com.nicbit.traquer.stryker.util.StringUtils;
import com.nicbit.traquer.stryker.view.EmptyRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationFragment extends Fragment implements NotificationContract.View, SwipeRefreshLayout.OnRefreshListener, NotificationListAdapter.NotificationListClickListener {

    @BindView(R.id.notification_list)
    EmptyRecyclerView mRecyclerView;

    @BindView(R.id.tv_empty_view)
    LinearLayout mEmptyView;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private NotificationContract.UserActionsListener mActionsListener;
    private NotificationListAdapter mNotificationListAdapter;
    private LocationBluetoothPermissionUtility locationBluetoothPermissionUtility;
    private String notificationId;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void getNotifications() {
        DialogClass.showDialog(getActivity(), getActivity().getString(R.string.please_wait));
        mActionsListener.getNotificationList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mActionsListener = new NotificationPresenter(this);
        mNotificationListAdapter = new NotificationListAdapter(this, getActivity(), new ArrayList<ReaderGetNotificationsResponse>());
        mRecyclerView.setAdapter(mNotificationListAdapter);
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
        getNotifications();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (locationBluetoothPermissionUtility != null) {
            locationBluetoothPermissionUtility.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateAdapter(List<ReaderGetNotificationsResponse> list) {
        if (list.size() == 0) {
            mRecyclerView.setEmptyView(mEmptyView);
        } else {
            mNotificationListAdapter.addAll(list);
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        getNotifications();
    }

    @Override
    public void onNotificationReceive(NotificationApiResponse response, NicbitException e) {
        DialogClass.dismissDialog(getActivity());
        if (e == null) {
            if (response.getCode() == 200) {
                List<ReaderGetNotificationsResponse> data = response.getReaderGetNotificationsResponses();
                if (data != null) {
                    updateAdapter(data);
                }
            } else if (response.getCode() == 209) {
                SessionToken.get(new SessionTokenListener() {
                    @Override
                    public void onSessionTokenUpdate(boolean isUpdate) {
                        if (isUpdate) {
                            getNotifications();
                        } else {
                            stopRefresh();
                            ErrorMessageHandler.handleErrorMessage(209, getActivity());
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
    public void onNotificationRemoved(NotificationApiResponse response, NicbitException e) {
        DialogClass.dismissDialog(getActivity());
        stopRefresh();
        if (e == null) {
            if (response.getCode() == 200) {
                mNotificationListAdapter.notifyDataSetChanged();
            } else if (response.getCode() == 209) {
                SessionToken.get(new SessionTokenListener() {
                    @Override
                    public void onSessionTokenUpdate(boolean isUpdate) {

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
    public void onNotificationClicked(ReaderGetNotificationsResponse data) {

        final NewParams params = data.getParams();
        String notificationType = data.getNotificationType();
        locationBluetoothPermissionUtility = new LocationBluetoothPermissionUtility(getActivity());
        locationBluetoothPermissionUtility.setLocationListener(new LocationBluetoothPermissionUtility.LocationBluetoothListener() {
            @Override
            public void onLocationON() {
                if (params.getServiceType() == 2)
                    locationBluetoothPermissionUtility.checkBluetoothOnOff();
            }

            @Override
            public void onLocationOFF() {
                if (params.getServiceType() == 2)
                    locationBluetoothPermissionUtility.checkBluetoothOnOff();
            }

            @Override
            public void onBluetoothON() {

            }

            @Override
            public void onBluetoothOFF() {

            }
        });


        switch (notificationType) {
            case Constant.NotificationType.OrderCreation:
            case Constant.NotificationType.OrderAssignedFromSalesRep:
            case Constant.NotificationType.OrderAssignedToSalesRep:
                openCaseDetails(params.getCaseId());
                break;
            case Constant.NotificationType.ShipmentHardDeliveredCR:
            case Constant.NotificationType.ShipmentHardDeliveredSR:
            case Constant.NotificationType.ShipmentHardShippedSR:
            case Constant.NotificationType.ShipmentHardShippedCR:
            case Constant.NotificationType.ShipmentPartialDeliveredCR:
            case Constant.NotificationType.ShipmentPartialDeliveredSR:
            case Constant.NotificationType.ShipmentPartialShippedSR:
            case Constant.NotificationType.ShipmentScheduledCR:
            case Constant.NotificationType.ShipmentScheduledSR:
            case Constant.NotificationType.ShipmentSoftDeliveredCR:
            case Constant.NotificationType.ShipmentSoftDeliveredSR:
            case Constant.NotificationType.ShipmentSoftShippedSR:
            case Constant.NotificationType.ShipmentSoftShippedCR:
            case Constant.NotificationType.ShipmentPartialShippedCR:
            case Constant.NotificationType.ShipmentDelayedSR:
            case Constant.NotificationType.ShipmentDelayedCR:
                openCaseDetails(params.getCaseId());
                break;

            case Constant.NotificationType.IssueCreatedCR:
            case Constant.NotificationType.IssueCreatedSR:
            case Constant.NotificationType.IssueRespondedCR:
            case Constant.NotificationType.IssueRespondedSR:
                openIssueDetails(params);
                break;
            case Constant.NotificationType.GPSBluetoothDown:
                if (params.getServiceType() == 0) {
                    locationBluetoothPermissionUtility.checkBluetoothOnOff();
                } else if (params.getServiceType() == 1) {
                    locationBluetoothPermissionUtility.checkLocationOnOff();

                } else {
                    locationBluetoothPermissionUtility.checkLocationOnOff();


                }
                break;

        }
    }

    @Override
    public void removeNotificationClicked(String notification) {
        this.notificationId = notification;
      /*  NotificationData notificationData = new NotificationData();
        notificationData.setNotificationId(notification);*/
        ArrayList<String> notificationDatas = new ArrayList<>();
        notificationDatas.add(notification);
        removeNotification(notificationDatas, false);
    }

    public void removeNotification(ArrayList<String> notificationIdList, boolean removeAll) {
        RemoveNotificationRequest removeNotificationRequest = new RemoveNotificationRequest();
        removeNotificationRequest.setDeleteAll(removeAll);
        removeNotificationRequest.setNotifications(notificationIdList);
        DialogClass.showDialog(getActivity(), getActivity().getString(R.string.please_wait));
        mActionsListener.removeNotifications(removeNotificationRequest);
    }

    public void removeAll() {
        removeNotification(null, true);
        mNotificationListAdapter.clearAll();
    }

    public void openCaseDetails(String caseID) {
        if (!caseID.isEmpty()) {
            Intent i = new Intent(getActivity(), CaseDetailActivity.class);
            i.putExtra(StringUtils.ID, caseID);
            i.putExtra(StringUtils.CASE_NUMBER, "");
            getActivity().startActivity(i);
        } else {
            Toast.makeText(getActivity(), "Case Id not found", Toast.LENGTH_LONG).show();
        }
    }

    public void openIssueDetails(NewParams params) {
        if (params.getCaseId().isEmpty() || params.getIssueId().isEmpty()) {
            Toast.makeText(getActivity(), "Case Id not found", Toast.LENGTH_LONG).show();
        } else {
            Intent i = new Intent(getActivity(), IssueDetailActivity.class);
            i.putExtra(StringUtils.CASE_NUMBER, params.getCaseId());
            i.putExtra(StringUtils.CASE_ID, params.getCaseId());
            i.putExtra(StringUtils.ISSUE_ID, "" + params.getIssueId());
            i.putExtra(StringUtils.SHIPPING_NUMBER, params.getShippingNo());
            i.putExtra(StringUtils.SHIPMENT_ID, params.getShippingNo());
            getActivity().startActivity(i);
        }
    }
}