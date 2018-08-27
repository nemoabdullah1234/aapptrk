package com.nicbit.traquer.stryker.editProfile;

import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.Models.newModels.CountryApiResponse;
import com.nicbit.traquer.stryker.exception.NicbitException;

import java.io.File;

public interface EditProfileContract {
    interface View {
        void onEditProfileDone(ApiResponseModel loginResponse, NicbitException e);

        void onCountriesDone(CountryApiResponse responseModel, NicbitException e);

        void onProfileFetch(ApiResponseModel responseModel, NicbitException e);
    }

    interface UserActionsListener {
        void doEditProfile(Integer isImageRemove, String city, String password, String firstName, String lastName, String mobileNo, String countryCode, File profileImage);

        void doCountries();

        void getProfile();
    }
}
