package com.nicbit.traquer.common.linkdevice;

import com.google.gson.JsonObject;
import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.exception.NicbitException;


public interface LinkUnlinkDeviceContract {

    interface View {
        void onUnLinkDevice(ApiResponseModel loginResponse, NicbitException e);

        void onLinkDevice(ApiResponseModel loginResponse, NicbitException e);
    }

    interface UserActionsListener {
        void linkDevice(JsonObject code);

        void unLinkDevice(JsonObject code);
    }
}
