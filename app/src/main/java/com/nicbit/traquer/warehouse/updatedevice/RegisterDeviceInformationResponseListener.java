package com.nicbit.traquer.warehouse.updatedevice;

import com.nicbit.traquer.common.newInventory.response.ApiBaseResponse;
import com.nicbit.traquer.stryker.exception.NicbitException;

public interface RegisterDeviceInformationResponseListener {

    void onDeviceUpdate(ApiBaseResponse response, NicbitException e);
}
