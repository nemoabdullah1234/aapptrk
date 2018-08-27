package com.nicbit.traquer.common.newInventory.location;

import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.network.ApiHandler;

/**
 * Created by rohitkumar on 7/14/17.
 */

public class LocationPresenter implements LocationContract.UserActionsListener, LocationListListener {

    private final LocationContract.View floorView;

    public LocationPresenter(LocationContract.View floorView) {
        this.floorView = floorView;
    }

    @Override
    public void onLocationListResponse(ApiResponseModel response, NicbitException e) {
        floorView.onLocationList(response, e);
    }

    @Override
    public void getLocationList(double lat, double lang, String locationId) {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setLocationListListener(this);
        apiHandler.getLocation(lat, lang, locationId);
    }
}
