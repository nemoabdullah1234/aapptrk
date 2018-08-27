package com.nicbit.traquer.common.geofence;


import com.nicbit.traquer.stryker.exception.NicbitException;

/**
 * Created by rohitkumar on 9/14/17.
 */

public interface GeofencDeviceListener {

    void onGeofenceRecived(GeofenceApiResponse response, NicbitException e);

}
