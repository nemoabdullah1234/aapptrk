package com.nicbit.traquer.common.newInventory.zone;

import com.nicbit.traquer.common.newInventory.response.ZoneApiResponse;
import com.nicbit.traquer.stryker.exception.NicbitException;

/**
 * Created by rohitkumar on 7/14/17.
 */

public class ZoneView {

    interface View {
        void onZoneList(ZoneApiResponse response, NicbitException e);
    }

    interface UserActionsListener {
        void getZoneList(String floorID);
    }
}
