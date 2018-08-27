package com.nicbit.traquer.stryker.notification;

import com.nicbit.traquer.stryker.Models.RemoveNotificationRequest;
import com.nicbit.traquer.stryker.Models.newModels.NotificationApiResponse;
import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.listener.NotificationListListener;
import com.nicbit.traquer.stryker.network.ApiHandler;

public class NotificationPresenter implements NotificationContract.UserActionsListener, NotificationListListener {
    private final NotificationContract.View mNotificationView;

    public NotificationPresenter(NotificationContract.View mNotificationView) {
        this.mNotificationView = mNotificationView;
    }

    @Override
    public void removeNotifications(RemoveNotificationRequest removeNotificationRequest) {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setNotificationListListener(this);
        apiHandler.removeNotification(removeNotificationRequest);
    }

    @Override
    public void getNotificationList() {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setNotificationListListener(this);
        apiHandler.getNotifications();
    }

    @Override
    public void onNotificationListReceive(NotificationApiResponse response, NicbitException e) {
        mNotificationView.onNotificationReceive(response, e);
    }

    @Override
    public void onNotificationRemove(NotificationApiResponse response, NicbitException e) {
        mNotificationView.onNotificationRemoved(response, e);

    }
}
