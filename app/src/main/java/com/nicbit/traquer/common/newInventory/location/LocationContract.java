package com.nicbit.traquer.common.newInventory.location;

import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.exception.NicbitException;

/**
 * Created by rohitkumar on 7/14/17.
 */

public class LocationContract {

    public interface View {
        void onLocationList(ApiResponseModel response, NicbitException e);
    }

    public interface UserActionsListener {
        void getLocationList(double lat, double lang, String locationId);
    }
}
