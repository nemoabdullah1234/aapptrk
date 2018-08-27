package com.nicbit.traquer.stryker.home;

import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.listener.LogoutResponseListener;
import com.nicbit.traquer.stryker.network.ApiHandler;


public class HomePresenter implements HomeContract.UserActionsListener, LogoutResponseListener {

    private final HomeContract.View mHomeView;

    public HomePresenter(HomeContract.View mHomeView) {
        this.mHomeView = mHomeView;
    }

    @Override
    public void onLogoutResponse(ApiResponseModel response, NicbitException e) {
        mHomeView.onLogoutDone(response, e);
    }

    @Override
    public void doLogout() {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setLogoutResponseListener(this);
        apiHandler.logout();
    }
}
