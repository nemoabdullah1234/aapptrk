package com.nicbit.traquer.stryker.listener;

import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.exception.NicbitException;

public interface CaseHistoryDetailResponseListener {
    void onCaseHistoryDetailResponse(ApiResponseModel response, NicbitException e);

}
