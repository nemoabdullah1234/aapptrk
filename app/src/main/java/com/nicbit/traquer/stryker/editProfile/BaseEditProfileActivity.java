package com.nicbit.traquer.stryker.editProfile;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.nicbit.traquer.common.BaseActivity;
import com.nicbit.traquer.common.cognito.AppHelper;
import com.nicbit.traquer.common.cognito.UserDetail;
import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.Models.ReaderGetCountriesResponse;
import com.nicbit.traquer.stryker.Models.ReaderGetProfileResponse;
import com.nicbit.traquer.stryker.Models.newModels.CountryApiResponse;
import com.nicbit.traquer.stryker.R;
import com.nicbit.traquer.stryker.exception.ErrorMessageHandler;
import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.util.DialogClass;
import com.nicbit.traquer.stryker.util.PrefUtils;
import com.nicbit.traquer.stryker.util.StringUtils;

import java.io.File;
import java.util.List;

public abstract class BaseEditProfileActivity extends BaseActivity implements EditProfileContract.View {


    private CognitoUserSession session;
    private CognitoUserDetails details;

    // User details
    private String username;

    public EditProfileContract.UserActionsListener mActionsListener;

    protected abstract void setValuesInPreference(String profileImageUrl);

    protected abstract void setCountries(List<ReaderGetCountriesResponse> readerGetCountriesResponse);

    protected abstract void setProfile(ReaderGetProfileResponse profileResponse);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionsListener = new EditProfilePresenter(this);
    }

    public void editProfile(Integer isImageRemove, String city, String password, String firstName, String lastName, String mobileNo, String countryCode, File profileImage) {
        DialogClass.showDialog(this, getString(R.string.msg_wait));
        mActionsListener.doEditProfile(isImageRemove, city, password, firstName, lastName, mobileNo, countryCode, profileImage);
    }

    public void getCountriesList(boolean isDialogDisplay) {
        DialogClass.showDialog(this, getString(R.string.msg_wait));
        mActionsListener.doCountries();
    }

    public void getProfile() {
        DialogClass.showDialog(this, getString(R.string.msg_wait));
        getDetails();
        // mActionsListener.getProfile();


    }

    @Override
    public void onEditProfileDone(ApiResponseModel editResponse, NicbitException e) {
        DialogClass.dismissDialog(this);
        if (e == null) {
            if (editResponse.getCode() == 200) {
              /*  ReaderUpdateProfileResponse response= editResponse.getData().getReaderUpdateProfileResponse();
                setValuesInPreference(response.getProfileImageUrl());*/
                DialogClass.alerDialog(this, getResources().getString(R.string.profile_edit_successfully));
            } else {
                ErrorMessageHandler.handleErrorMessage(editResponse.getCode(), this);
            }
        } else {
            DialogClass.alerDialog(this, getResources().getString(R.string.check_internet_connection));
        }
    }

    @Override
    public void onCountriesDone(CountryApiResponse responseModel, NicbitException e) {
        DialogClass.dismissDialog(this);
        if (e == null) {
            if (responseModel.getCode() == StringUtils.SUCCESS_CODE) {
                setCountries(responseModel.getReaderGetCountriesResponse());
            } else {
                ErrorMessageHandler.handleErrorMessage(responseModel.getCode(), this);
            }
        } else {
            DialogClass.alerDialog(this, getResources().getString(R.string.check_internet_connection));
        }
    }

    @Override
    public void onProfileFetch(ApiResponseModel responseModel, NicbitException e) {

        DialogClass.dismissDialog(this);
        if (e == null) {
            if (responseModel.getStatus() == StringUtils.SUCCESS_STATUS) {
                setProfile(responseModel.getData().getReaderGetProfileResponse());
            } else {
                ErrorMessageHandler.handleErrorMessage(responseModel.getCode(), this);
            }
        } else {
            DialogClass.alerDialog(this, getResources().getString(R.string.check_internet_connection));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_search:
                Intent intent = new Intent(BaseEditProfileActivity.this, getSearchActivity());
                startActivity(intent);
                break;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    // Get user details from CIP service
    private void getDetails() {

        AppHelper.getPool().getUser(PrefUtils.getEmail()).getDetailsInBackground(detailsHandler);
    }


    GetDetailsHandler detailsHandler = new GetDetailsHandler() {
        @Override
        public void onSuccess(CognitoUserDetails cognitoUserDetails) {

            DialogClass.dismissDialog(BaseEditProfileActivity.this);
            // Store details in the AppHandler
            AppHelper.setUserDetails(cognitoUserDetails);
            setProfile(UserDetail.getUserDetails(cognitoUserDetails));

        }

        @Override
        public void onFailure(Exception exception) {
            DialogClass.dismissDialog(BaseEditProfileActivity.this);
            DialogClass.alerDialog(BaseEditProfileActivity.this, AppHelper.formatException(exception));
        }
    };


}
