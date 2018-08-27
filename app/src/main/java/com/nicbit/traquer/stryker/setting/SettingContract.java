package com.nicbit.traquer.stryker.setting;

import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.Models.UpdateSettingsRequest;
import com.nicbit.traquer.stryker.exception.NicbitException;

public interface SettingContract {

    interface View {
        void onLogoutDone(ApiResponseModel loginResponse, NicbitException e);

        void onGetSettingsResponseReceive(ApiResponseModel response, NicbitException e);

        void onUpdateSettingsResponseReceive(ApiResponseModel response, NicbitException e);
    }

    interface UserActionsListener {

        void doLogout();

        void getSettings();

        void updateSettings(UpdateSettingsRequest updateSettingsRequest);
    }
}
