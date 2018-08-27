package com.nicbit.traquer.common.cognito;

import com.nicbit.traquer.stryker.Models.ReaderGetProfileResponse;

/**
 * Created by rohitkumar on 7/6/17.
 */

public interface OnUserDetails {
    public void onUserDetails(ReaderGetProfileResponse data, Exception exception);

}
