package com.nicbit.traquer.stryker.Models.inventory;

import java.util.ArrayList;

public class Locations {
    ArrayList<LocationData> current;
    ArrayList<LocationData> near;
    ArrayList<LocationData> other;


    public ArrayList<LocationData> getCurrent() {
        return current;
    }

    public void setCurrent(ArrayList<LocationData> current) {
        this.current = current;
    }

    public ArrayList<LocationData> getNear() {
        return near;
    }

    public void setNear(ArrayList<LocationData> near) {
        this.near = near;
    }

    public ArrayList<LocationData> getOther() {
        return other;
    }

    public void setOther(ArrayList<LocationData> other) {
        this.other = other;
    }
}
