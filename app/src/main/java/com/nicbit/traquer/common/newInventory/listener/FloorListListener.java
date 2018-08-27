package com.nicbit.traquer.common.newInventory.listener;

import com.nicbit.traquer.common.newInventory.response.FloorApiResponse;
import com.nicbit.traquer.stryker.exception.NicbitException;

/**
 * Created by rohitkumar on 7/14/17.
 */

public interface FloorListListener {
    void onFloorListResponse(FloorApiResponse response, NicbitException e);
}
