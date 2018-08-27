package com.nicbit.traquer.stryker.Models.newModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nicbit.traquer.stryker.Models.ReaderGetCountriesResponse;

import java.util.List;

/**
 * Created by niteshgoel on 8/25/17.
 */

public class CountryApiResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private List<ReaderGetCountriesResponse> readerGetCountriesResponse;

    public List<ReaderGetCountriesResponse> getReaderGetCountriesResponse() {
        return readerGetCountriesResponse;
    }

    public void setReaderGetCountriesResponse(List<ReaderGetCountriesResponse> readerGetCountriesResponse) {
        this.readerGetCountriesResponse = readerGetCountriesResponse;
    }
}
