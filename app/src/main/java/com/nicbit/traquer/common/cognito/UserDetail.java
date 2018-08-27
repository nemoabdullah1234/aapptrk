package com.nicbit.traquer.common.cognito;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.nicbit.traquer.stryker.Models.ReaderGetProfileResponse;

import java.util.Map;

/**
 * Created by rohitkumar on 7/6/17.
 */

public class UserDetail implements GetDetailsHandler {
    OnUserDetails onUserDetails;

    public UserDetail(OnUserDetails onUserDetails) {
        this.onUserDetails = onUserDetails;
    }

    public void getCognitoUser(String username) {
        AppHelper.getPool().getUser(username).getDetailsInBackground(this);

    }

    @Override
    public void onSuccess(CognitoUserDetails cognitoUserDetails) {
        if (onUserDetails != null) {
            AppHelper.setUserDetails(cognitoUserDetails);
            onUserDetails.onUserDetails(getUserDetails(cognitoUserDetails), null);
        }

    }

    @Override
    public void onFailure(Exception exception) {
        onUserDetails.onUserDetails(null, exception);

    }

    public static ReaderGetProfileResponse getUserDetails(CognitoUserDetails cognitoUserDetails) {


        ReaderGetProfileResponse readerGetProfileResponse = new ReaderGetProfileResponse();
        CognitoUserAttributes cognitoUserAttributes = cognitoUserDetails.getAttributes();
        Map<String, String> attributes = cognitoUserAttributes.getAttributes();
        if (attributes != null) {
            if (attributes.containsKey("email")) {
                readerGetProfileResponse.setEmail(attributes.get("email"));
            }
            if (attributes.containsKey("given_name")) {
                readerGetProfileResponse.setFirstName(attributes.get("given_name"));
            }

            if (attributes.containsKey("family_name")) {
                readerGetProfileResponse.setLastName(attributes.get("family_name"));
            }

            if (attributes.containsKey("picture")) {
                readerGetProfileResponse.setProfileImage(attributes.get("picture"));
            }

            if (attributes.containsKey("custom:MobileNumber")) {
                readerGetProfileResponse.setMobile(attributes.get("custom:MobileNumber"));
            }

            if (attributes.containsKey("custom:MobileCode")) {
                readerGetProfileResponse.setCountryCode(attributes.get("custom:MobileCode"));
            }


        }


        return readerGetProfileResponse;

    }


}
