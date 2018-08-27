package com.nicbit.traquer.common.updatedevice;

import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.exception.NicbitException;

public interface DeviceInformationResponseListener {

    void onDeviceUpdate(ApiResponseModel response, NicbitException e);
}
