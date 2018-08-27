package com.nicbit.traquer.common.newInventory.response;

import com.nicbit.traquer.stryker.Models.inventory.Locations;
import com.nicbit.traquer.stryker.Models.newModels.BaseResponse;


public class LocationApiResponse extends BaseResponse {
    Locations location;

    public Locations getLocation() {
        return location;
    }

    public void setLocation(Locations location) {
        this.location = location;
    }
}
