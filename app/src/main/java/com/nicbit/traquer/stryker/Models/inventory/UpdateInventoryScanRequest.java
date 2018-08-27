package com.nicbit.traquer.stryker.Models.inventory;

import java.util.ArrayList;

public class UpdateInventoryScanRequest {

    int locationId;
    double currentLatitude;
    double currentLongitude;
    int isPresentOnLocation;
    long scanStartTime;
    long scanEndTime;
    ArrayList<ScanData> items;

    public long getScanStartTime() {
        return scanStartTime;
    }

    public void setScanStartTime(long scanStartTime) {
        this.scanStartTime = scanStartTime;
    }

    public long getScanEndTime() {
        return scanEndTime;
    }

    public void setScanEndTime(long scanEndTime) {
        this.scanEndTime = scanEndTime;
    }

    public double getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(double currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public double getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(double currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public int getIsPresentOnLocation() {
        return isPresentOnLocation;
    }

    public void setIsPresentOnLocation(int isPresentOnLocation) {
        this.isPresentOnLocation = isPresentOnLocation;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public ArrayList<ScanData> getItems() {
        return items;
    }

    public void setItems(ArrayList<ScanData> items) {
        this.items = items;
    }
}
