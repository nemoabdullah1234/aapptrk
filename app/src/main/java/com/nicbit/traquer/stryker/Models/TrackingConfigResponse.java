package com.nicbit.traquer.stryker.Models;

public class TrackingConfigResponse {
    String mode;
    String beaconReScan;
    String locationScan;
    String beaconScan;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getBeaconReScan() {
        return beaconReScan;
    }

    public void setBeaconReScan(String beaconReScan) {
        this.beaconReScan = beaconReScan;
    }

    public String getLocationScan() {
        return locationScan;
    }

    public void setLocationScan(String locationScan) {
        this.locationScan = locationScan;
    }

    public String getBeaconScan() {
        return beaconScan;
    }

    public void setBeaconScan(String beaconScan) {
        this.beaconScan = beaconScan;
    }
}
