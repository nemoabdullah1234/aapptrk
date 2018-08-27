package com.nicbit.traquer.stryker.login;

public interface LoginContract {

    interface View {
        void onUserAuthenticationDone(String user, String password, String message);
    }

    interface UserActionsListener {

        void getUserAuthentication(String email, String password);
    }
}
