package com.nicbit.traquer.stryker.network;

import android.app.Activity;
import android.text.TextUtils;

import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.exception.ErrorMessageHandler;
import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.listener.ApiListener;
import com.nicbit.traquer.stryker.listener.SessionTokenListener;
import com.nicbit.traquer.stryker.util.DialogClass;
import com.nicbit.traquer.stryker.util.PrefUtils;
import com.nicbit.traquer.stryker.util.StringUtils;

public class SessionToken {

    public static void get(final SessionTokenListener sessionTokenListener, final Activity context) {

        ApiHandler apiHandler = new ApiHandler();
        apiHandler.setApiListener(new ApiListener() {
            @Override
            public void onApiResponse(ApiResponseModel response, NicbitException e) {
                DialogClass.dismissDialog(context);
                if (e == null) {
                    if (response.getStatus() == StringUtils.SUCCESS_STATUS) {
                        PrefUtils.setSessionToken(response.getData().getReaderGenerateSessionResponse().getToken());
                        sessionTokenListener.onSessionTokenUpdate(true);
                    } else {
                        ErrorMessageHandler.handleErrorMessage(response.getCode(), context);
                    }
                } else {
                }
            }
        });
        String appToken = PrefUtils.getAccessToken();
        if (!TextUtils.isEmpty(appToken)) {
            apiHandler.getSessionToken(appToken);
        } else {
            sessionTokenListener.onSessionTokenUpdate(false);
        }
    }

}
