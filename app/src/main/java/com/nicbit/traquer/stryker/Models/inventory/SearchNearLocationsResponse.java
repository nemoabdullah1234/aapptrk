package com.nicbit.traquer.stryker.Models.inventory;

import java.util.ArrayList;

public class SearchNearLocationsResponse {
    Locations location;

    ArrayList<Zone> zones;

    public Locations getLocation() {
        return location;
    }

    public void setLocation(Locations location) {
        this.location = location;
    }

    public ArrayList<Zone> getZones() {
        return zones;
    }

    public void setZones(ArrayList<Zone> zones) {
        this.zones = zones;
    }
}
