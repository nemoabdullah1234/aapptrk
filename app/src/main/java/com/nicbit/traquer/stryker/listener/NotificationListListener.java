package com.nicbit.traquer.stryker.listener;

import com.nicbit.traquer.stryker.Models.newModels.NotificationApiResponse;
import com.nicbit.traquer.stryker.exception.NicbitException;

public interface NotificationListListener {
    void onNotificationListReceive(NotificationApiResponse response, NicbitException e);

    void onNotificationRemove(NotificationApiResponse response, NicbitException e);
}
