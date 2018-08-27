package com.nicbit.traquer.common.geofence;


import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.network.ApiHandler;

public class GeofenceDevicePresenter implements GefenceDeviceContract.UserActionsListener, GeofencDeviceListener {

    private final GefenceDeviceContract.View mHomeView;

    public GeofenceDevicePresenter(GefenceDeviceContract.View mHomeView) {
        this.mHomeView = mHomeView;
    }


    @Override
    public void getGeofences() {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setGeofencDeviceListener(this);
        apiHandler.getGeofence();
    }


    @Override
    public void onGeofenceRecived(GeofenceApiResponse response, NicbitException e) {
        mHomeView.onGeofencesReceived(response, e);
    }
}
