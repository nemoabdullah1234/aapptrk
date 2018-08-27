package com.nicbit.traquer.common.newInventory.zone;

import com.nicbit.traquer.common.newInventory.listener.ZoneListListener;
import com.nicbit.traquer.common.newInventory.response.ZoneApiResponse;
import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.network.ApiHandler;

/**
 * Created by rohitkumar on 7/14/17.
 */

public class ZonePresenter implements ZoneView.UserActionsListener, ZoneListListener {

    private final ZoneView.View floorView;

    public ZonePresenter(ZoneView.View floorView) {
        this.floorView = floorView;
    }


    @Override
    public void onZoneListResponse(ZoneApiResponse response, NicbitException e) {
        floorView.onZoneList(response, e);
    }

    @Override
    public void getZoneList(String floorID) {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setZoneListListener(this);
        apiHandler.getZones(floorID);
    }
}
