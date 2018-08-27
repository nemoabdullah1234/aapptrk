package com.nicbit.traquer.stryker.listener;


import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.exception.NicbitException;

public interface CaseItemQuantityListener {
    void onCaseItemQuantityUpdated(ApiResponseModel response, NicbitException e);

    void onCaseItemQuantityReceive(ApiResponseModel response, NicbitException e);

}
