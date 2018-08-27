package com.nicbit.traquer.common.newInventory.zone;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by rohitkumar on 7/14/17.
 */

public class ZoneGroup extends ExpandableGroup<Product> {
    String zoneId;

    public ZoneGroup(String title, List<Product> items, String zoneId) {
        super(title, items);
        this.zoneId = zoneId;
    }

    public String getZoneId() {
        return zoneId;
    }
}
