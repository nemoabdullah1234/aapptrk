package com.nicbit.traquer.stryker.listener;

import com.nicbit.traquer.stryker.Models.newModels.CountryApiResponse;
import com.nicbit.traquer.stryker.exception.NicbitException;

public interface CountriesListener {
    void onCountriesResponse(CountryApiResponse response, NicbitException e);
}
