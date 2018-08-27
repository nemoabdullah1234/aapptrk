package com.nicbit.traquer.warehouse.updatedevice;

import com.nicbit.traquer.common.newInventory.response.ApiBaseResponse;
import com.nicbit.traquer.common.updatedevice.DeviceInfo;
import com.nicbit.traquer.stryker.exception.NicbitException;

public class RegisterUpdateDevicePresneter {


    public interface View {
        void onDeviceUpdate(ApiBaseResponse response, NicbitException e);
    }

    public interface UserActionsListener {
        void deviceUpdate(DeviceInfo deviceInfor);
    }
}
