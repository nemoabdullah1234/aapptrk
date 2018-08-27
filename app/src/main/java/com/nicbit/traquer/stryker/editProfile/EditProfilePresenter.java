package com.nicbit.traquer.stryker.editProfile;

import android.support.annotation.NonNull;

import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.Models.newModels.CountryApiResponse;
import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.listener.CountriesListener;
import com.nicbit.traquer.stryker.listener.EditProfileResponseListener;
import com.nicbit.traquer.stryker.listener.UserProfileResponseListener;
import com.nicbit.traquer.stryker.network.ApiHandler;

import java.io.File;

public class EditProfilePresenter implements EditProfileContract.UserActionsListener, EditProfileResponseListener, CountriesListener, UserProfileResponseListener {

    private final EditProfileContract.View mEditProfileView;

    public EditProfilePresenter(@NonNull EditProfileContract.View mEditProfileView) {
        this.mEditProfileView = mEditProfileView;
    }


    @Override
    public void onEditProfileResponse(ApiResponseModel response, NicbitException e) {
        mEditProfileView.onEditProfileDone(response, e);

    }


    @Override
    public void onCountriesResponse(CountryApiResponse response, NicbitException e) {
        mEditProfileView.onCountriesDone(response, e);
    }


    @Override
    public void doEditProfile(Integer isImageRemove, String city, String password, String firstName, String lastName, String mobileNo, String countryCode, File profileImage) {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setEditProfileResponseListener(this);
        apiHandler.doEditRequest(isImageRemove, city, password, firstName, lastName, mobileNo, countryCode, profileImage);
    }

    @Override
    public void doCountries() {
        ApiHandler apiHandler = new ApiHandler();
        apiHandler.setCountriesListener(this);
        apiHandler.getCountries();
    }

    @Override
    public void getProfile() {
        ApiHandler apiHandler = new ApiHandler();
        apiHandler.setUserProfileResponseListener(this);
        apiHandler.getUserProfileDataFromApi();
    }

    @Override
    public void onUserProfileResponse(ApiResponseModel response, NicbitException e) {
        mEditProfileView.onProfileFetch(response, e);
    }
}
