package com.nicbit.traquer.common.updatedevice;

import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.network.ApiHandler;

public class DeviceApiHandler implements UpdateDevicePresneter.UserActionsListener, DeviceInformationResponseListener {
    private final UpdateDevicePresneter.View mCaseView;

    public DeviceApiHandler(UpdateDevicePresneter.View mCaseView) {
        this.mCaseView = mCaseView;
    }


    @Override
    public void deviceUpdate(DeviceInfo deviceInfor) {

        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setDeviceInformationResponseListener(this);
        apiHandler.updateDeviceInformation(deviceInfor);

    }

    @Override
    public void onDeviceUpdate(ApiResponseModel response, NicbitException e) {

        mCaseView.onDeviceUpdate(response, e);

    }
}

