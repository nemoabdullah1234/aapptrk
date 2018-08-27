package com.nicbit.traquer.stryker.forgotPassword;


import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.exception.NicbitException;

public interface ForgotPasswordContract {
    interface View {
        void onForgotPasswordDone(ApiResponseModel responseModel, NicbitException e);

        void onValidatePasswordDone(ApiResponseModel responseModel, NicbitException e);

        void onResetPasswordDone(ApiResponseModel responseModel, NicbitException e);
    }

    interface UserActionsListener {
        void doForgotPassword(String username);

        void doValidatePassword(String email, String token);

        void doResetPassword(String email, String token, String newPAssword);
    }
}
