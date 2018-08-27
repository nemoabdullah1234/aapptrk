package com.nicbit.traquer.stryker.notification;

import com.nicbit.traquer.stryker.Models.RemoveNotificationRequest;
import com.nicbit.traquer.stryker.Models.newModels.NotificationApiResponse;
import com.nicbit.traquer.stryker.exception.NicbitException;

public interface NotificationContract {

    interface View {
        void onNotificationReceive(NotificationApiResponse response, NicbitException e);

        void onNotificationRemoved(NotificationApiResponse response, NicbitException e);
    }

    interface UserActionsListener {
        void removeNotifications(RemoveNotificationRequest removeNotificationRequest);

        void getNotificationList();
    }
}
