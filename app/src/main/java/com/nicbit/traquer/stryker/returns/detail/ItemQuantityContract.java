package com.nicbit.traquer.stryker.returns.detail;

import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.Models.UpdateInventoryRequestModel;
import com.nicbit.traquer.stryker.exception.NicbitException;

public interface ItemQuantityContract {
    interface View {
        void onCaseItemQuantityReceive(ApiResponseModel response, NicbitException e);

        void onCaseItemQuantityUpdated(ApiResponseModel response, NicbitException e);
    }

    interface UserActionsListener {
        void getCaseItemQuantity(Integer skuId, String caseNo);

        void updateCaseItemQuantity(UpdateInventoryRequestModel updateInventoryRequestModel);
    }
}
