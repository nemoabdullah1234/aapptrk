package com.nicbit.traquer.stryker.setting;

import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.Models.UpdateSettingsRequest;
import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.listener.LogoutResponseListener;
import com.nicbit.traquer.stryker.listener.SettingsResponseListener;
import com.nicbit.traquer.stryker.listener.UpdateSettingsResponseListener;
import com.nicbit.traquer.stryker.network.ApiHandler;

public class SettingPresenter implements SettingContract.UserActionsListener, LogoutResponseListener, UpdateSettingsResponseListener, SettingsResponseListener {
    private final SettingContract.View mSettingView;

    public SettingPresenter(SettingContract.View mSettingView) {
        this.mSettingView = mSettingView;
    }

    @Override
    public void onLogoutResponse(ApiResponseModel response, NicbitException e) {
        mSettingView.onLogoutDone(response, e);
    }

    @Override
    public void doLogout() {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setLogoutResponseListener(this);
        apiHandler.logout();

    }

    @Override
    public void getSettings() {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setSettingsResponseListener(this);
        apiHandler.getSettings();

    }

    @Override
    public void updateSettings(UpdateSettingsRequest updateSettingsRequest) {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setUpdateSettingsResponseListener(this);
        apiHandler.updateSettings(updateSettingsRequest);
    }

    @Override
    public void onUpdateSettingsResponseReceive(ApiResponseModel response, NicbitException e) {
        mSettingView.onUpdateSettingsResponseReceive(response, e);

    }

    @Override
    public void onSettingsResponseReceive(ApiResponseModel response, NicbitException e) {
        mSettingView.onGetSettingsResponseReceive(response, e);
    }
}
