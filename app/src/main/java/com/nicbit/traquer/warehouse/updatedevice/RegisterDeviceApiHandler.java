package com.nicbit.traquer.warehouse.updatedevice;

import com.nicbit.traquer.common.newInventory.listener.DeviceRegisterListener;
import com.nicbit.traquer.common.newInventory.response.ApiBaseResponse;
import com.nicbit.traquer.common.updatedevice.DeviceInfo;
import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.network.ApiHandler;


public class RegisterDeviceApiHandler implements RegisterUpdateDevicePresneter.UserActionsListener, DeviceRegisterListener {
    private final RegisterUpdateDevicePresneter.View mCaseView;

    public RegisterDeviceApiHandler(RegisterUpdateDevicePresneter.View mCaseView) {
        this.mCaseView = mCaseView;
    }


    @Override
    public void deviceUpdate(DeviceInfo deviceInfor) {

        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setDeviceRegisterListener(this);
        apiHandler.registerDevice(deviceInfor);

    }

    @Override
    public void onDeviceUpdate(ApiBaseResponse response, NicbitException e) {
        mCaseView.onDeviceUpdate(response, e);

    }

}

