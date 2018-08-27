package com.nicbit.traquer.common.newInventory.floor;

import com.nicbit.traquer.common.newInventory.response.FloorApiResponse;
import com.nicbit.traquer.stryker.exception.NicbitException;

/**
 * Created by rohitkumar on 7/14/17.
 */

public class FloorView {
    interface View {
        void onFloorList(FloorApiResponse response, NicbitException e);
    }

    interface UserActionsListener {
        void getFloorList(String locationId);
    }


}
