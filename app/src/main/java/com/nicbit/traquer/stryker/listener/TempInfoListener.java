package com.nicbit.traquer.stryker.listener;

import com.nicbit.traquer.stryker.Models.newModels.TempApiResponse;
import com.nicbit.traquer.stryker.exception.NicbitException;

public interface TempInfoListener {
    void onTempInfoResponse(TempApiResponse response, NicbitException e);
}
