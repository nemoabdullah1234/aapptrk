package com.nicbit.traquer.common.newInventory.location;

import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.exception.NicbitException;

/**
 * Created by rohitkumar on 7/15/17.
 */

public interface LocationListListener {

    void onLocationListResponse(ApiResponseModel response, NicbitException e);

}
