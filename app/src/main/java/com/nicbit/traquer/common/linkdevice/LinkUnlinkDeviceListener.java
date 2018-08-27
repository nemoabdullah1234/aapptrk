package com.nicbit.traquer.common.linkdevice;

import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.exception.NicbitException;

/**
 * Created by rohitkumar on 9/14/17.
 */

public interface LinkUnlinkDeviceListener {

    void onDeviceLink(ApiResponseModel response, NicbitException e);

    void onDeviceUnlink(ApiResponseModel response, NicbitException e);
}
