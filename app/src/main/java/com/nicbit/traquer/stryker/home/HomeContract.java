package com.nicbit.traquer.stryker.home;


import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.exception.NicbitException;

public interface HomeContract {
    interface View {
        void onLogoutDone(ApiResponseModel loginResponse, NicbitException e);
    }

    interface UserActionsListener {
        void doLogout();
    }
}
