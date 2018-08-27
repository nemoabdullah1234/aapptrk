package com.nicbit.traquer.common.newInventory.listener;

import com.nicbit.traquer.common.newInventory.response.ZoneApiResponse;
import com.nicbit.traquer.stryker.exception.NicbitException;

/**
 * Created by rohitkumar on 7/14/17.
 */

public interface ZoneListListener {

    void onZoneListResponse(ZoneApiResponse response, NicbitException e);
}
