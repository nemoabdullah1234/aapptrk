package com.nicbit.traquer.stryker.inventory.frigdoor;

import com.nicbit.traquer.stryker.Models.newModels.TempApiResponse;
import com.nicbit.traquer.stryker.exception.NicbitException;

/**
 * Created by niteshgoel on 10/26/17.
 */

public interface TemperatureInfoContract {
    interface View {
        void onTempInfoResponse(TempApiResponse response, NicbitException e);
    }

    interface UserActionsListener {
        void getTempInfo(String skuId);
    }
}
