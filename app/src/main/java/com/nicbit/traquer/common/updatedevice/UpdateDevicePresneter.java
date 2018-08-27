package com.nicbit.traquer.common.updatedevice;

import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.exception.NicbitException;

public class UpdateDevicePresneter {


    public interface View {
        void onDeviceUpdate(ApiResponseModel response, NicbitException e);
    }

    public interface UserActionsListener {
        void deviceUpdate(DeviceInfo deviceInfor);
    }
}
