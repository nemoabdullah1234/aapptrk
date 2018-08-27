package com.nicbit.traquer.stryker.forgotPassword;

import android.support.annotation.NonNull;

import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.listener.ForgotPasswordResponseListener;
import com.nicbit.traquer.stryker.listener.ResetPasswordListener;
import com.nicbit.traquer.stryker.listener.ValidateTokenListener;
import com.nicbit.traquer.stryker.network.ApiHandler;

public class ForgotPasswordPresenter implements ForgotPasswordContract.UserActionsListener, ForgotPasswordResponseListener, ValidateTokenListener, ResetPasswordListener {

    private final ForgotPasswordContract.View mForgotPasswordView;

    public ForgotPasswordPresenter(@NonNull ForgotPasswordContract.View mForgotPasswordView) {
        this.mForgotPasswordView = mForgotPasswordView;
    }


    @Override
    public void doForgotPassword(String username) {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setForgotPasswordResponseListener(this);
        apiHandler.doForgotPasswordRequest(username);
    }

    @Override
    public void doValidatePassword(String email, String token) {
        ApiHandler apiHandler = new ApiHandler();
        apiHandler.setValidateTokenListener(this);
        apiHandler.doValidateTokenRequest(email, token);
    }

    @Override
    public void doResetPassword(String email, String token, String newPAssword) {
        ApiHandler apiHandler = new ApiHandler();
        apiHandler.setResetPasswordListener(this);
        apiHandler.doResetPasswordRequest(email, token, newPAssword);

    }

    @Override
    public void onForgotPasswordResponse(ApiResponseModel response, NicbitException e) {
        mForgotPasswordView.onForgotPasswordDone(response, e);
    }

    @Override
    public void onResetPasswordResponse(ApiResponseModel response, NicbitException e) {
        mForgotPasswordView.onResetPasswordDone(response, e);
    }

    @Override
    public void onValidateTokenResponse(ApiResponseModel response, NicbitException e) {
        mForgotPasswordView.onValidatePasswordDone(response, e);
    }
}
