package com.nicbit.traquer.stryker.inventory.details;

import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.Models.UpdateInventoryRequestModel;
import com.nicbit.traquer.stryker.exception.NicbitException;

public interface InventoryDetailContract {
    interface View {
        void onItemInventoryResponse(ApiResponseModel response, NicbitException e);

        void onItemUpdated(ApiResponseModel response, NicbitException e);
    }

    interface UserActionsListener {
        void getItemInventory(String skuId);

        void getItemInventoryByUid(String uid);

        void updateItemInventory(UpdateInventoryRequestModel updateInventoryRequestModel);
    }
}
