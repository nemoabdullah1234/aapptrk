package com.nicbit.traquer.stryker.inventory.frigdoor;

import com.nicbit.traquer.stryker.Models.newModels.TempApiResponse;
import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.listener.TempInfoListener;
import com.nicbit.traquer.stryker.network.ApiHandler;

/**
 * Created by niteshgoel on 10/26/17.
 */

public class TemperatureInfoPresenter implements TemperatureInfoContract.UserActionsListener, TempInfoListener {

    private final TemperatureInfoContract.View mView;

    public TemperatureInfoPresenter(TemperatureInfoContract.View mView) {
        this.mView = mView;
    }


    @Override
    public void onTempInfoResponse(TempApiResponse response, NicbitException e) {
        mView.onTempInfoResponse(response, e);
    }

    @Override
    public void getTempInfo(String skuId) {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setTempInfoListener(this);
        apiHandler.getTempInfo(skuId);
    }
}
