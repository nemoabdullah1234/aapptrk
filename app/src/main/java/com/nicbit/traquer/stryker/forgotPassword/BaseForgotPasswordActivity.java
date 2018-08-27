package com.nicbit.traquer.stryker.forgotPassword;

import android.os.Bundle;

import com.nicbit.traquer.common.BaseActivity;
import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.R;
import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.util.DialogClass;
import com.nicbit.traquer.stryker.util.StringUtils;

public abstract class BaseForgotPasswordActivity extends BaseActivity implements ForgotPasswordContract.View {

    public ForgotPasswordContract.UserActionsListener mActionsListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionsListener = new ForgotPasswordPresenter(this);
    }

    public void forgotPassword(String userName) {
        DialogClass.showDialog(this, getString(R.string.msg_wait));
        mActionsListener.doForgotPassword(userName);
    }

    public void validateToken(String email, String token) {
        DialogClass.showDialog(this, getString(R.string.msg_wait));
        mActionsListener.doValidatePassword(email, token);
    }

    @Override
    public void onValidatePasswordDone(ApiResponseModel responseModel, NicbitException e) {
        DialogClass.dismissDialog(this);
        if (e == null) {
            if (responseModel.getStatus() == StringUtils.SUCCESS_STATUS) {
                goToResetPAsswordActivity();

            } else {
                DialogClass.alerDialog(this, responseModel.getMessage());
            }
        } else {
            DialogClass.alerDialog(this, getResources().getString(R.string.check_internet_connection));
        }
    }

    abstract void goToResetPAsswordActivity();


    @Override
    public void onResetPasswordDone(ApiResponseModel responseModel, NicbitException e) {

    }


}
