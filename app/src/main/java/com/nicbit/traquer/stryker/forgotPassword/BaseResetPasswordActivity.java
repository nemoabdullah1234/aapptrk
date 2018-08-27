package com.nicbit.traquer.stryker.forgotPassword;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.nicbit.traquer.common.BaseActivity;
import com.nicbit.traquer.common.EventsLog;
import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.R;
import com.nicbit.traquer.stryker.exception.ErrorMessageHandler;
import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.util.DialogClass;
import com.nicbit.traquer.stryker.util.StringUtils;

public abstract class BaseResetPasswordActivity extends BaseActivity implements ForgotPasswordContract.View {
    public ForgotPasswordContract.UserActionsListener mActionsListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionsListener = new ForgotPasswordPresenter(this);

    }

    public void resetPassword(String email, String token, String newPAssword) {
        DialogClass.showDialog(this, getString(R.string.msg_wait));
        mActionsListener.doResetPassword(email, token, newPAssword);
    }

    @Override
    public void onResetPasswordDone(ApiResponseModel responseModel, NicbitException e) {
        DialogClass.dismissDialog(this);
        if (e == null) {
            if (responseModel.getStatus() == StringUtils.SUCCESS_STATUS) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getResources().getString(R.string.password_changed_successfully))
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                gotoLoginActivity();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                EventsLog.customEvent("PASSWORD RESET", "SUCCESS", "YES");

            } else {
                EventsLog.customEvent("PASSWORD RESET", "SUCCESS", responseModel.getMessage());
                ErrorMessageHandler.handleErrorMessage(responseModel.getCode(), this);
            }
        } else {
            DialogClass.alerDialog(this, getResources().getString(R.string.check_internet_connection));
        }
    }

    @Override
    public void onForgotPasswordDone(ApiResponseModel responseModel, NicbitException e) {

    }

    @Override
    public void onValidatePasswordDone(ApiResponseModel responseModel, NicbitException e) {

    }


}
