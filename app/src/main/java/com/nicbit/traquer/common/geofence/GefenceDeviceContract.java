package com.nicbit.traquer.common.geofence;


import com.nicbit.traquer.stryker.exception.NicbitException;

public interface GefenceDeviceContract {

    interface View {
        void onGeofencesReceived(GeofenceApiResponse loginResponse, NicbitException e);
    }

    interface UserActionsListener {
        void getGeofences();
    }
}
