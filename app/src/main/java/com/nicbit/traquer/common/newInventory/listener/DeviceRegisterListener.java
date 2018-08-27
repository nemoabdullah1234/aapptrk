package com.nicbit.traquer.common.newInventory.listener;

import com.nicbit.traquer.common.newInventory.response.ApiBaseResponse;
import com.nicbit.traquer.stryker.exception.NicbitException;

/**
 * Created by rohitkumar on 7/12/17.
 */

public interface DeviceRegisterListener {

    void onDeviceUpdate(ApiBaseResponse response, NicbitException e);

}
