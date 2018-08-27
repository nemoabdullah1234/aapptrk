package com.nicbit.traquer.common.newInventory.floor;

import com.nicbit.traquer.common.newInventory.listener.FloorListListener;
import com.nicbit.traquer.common.newInventory.response.FloorApiResponse;
import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.network.ApiHandler;

/**
 * Created by rohitkumar on 7/14/17.
 */

public class FloorPresenter implements FloorView.UserActionsListener, FloorListListener {

    private final FloorView.View floorView;

    public FloorPresenter(FloorView.View floorView) {
        this.floorView = floorView;
    }

    @Override
    public void getFloorList(String locationId) {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setFloorListListener(this);
        apiHandler.getFLoorList(locationId);
    }

    @Override
    public void onFloorListResponse(FloorApiResponse response, NicbitException e) {
        floorView.onFloorList(response, e);
    }
}
