package com.nicbit.traquer.stryker.inventory.list;

import com.nicbit.traquer.stryker.Models.inventory.Product;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class ZoneGroup extends ExpandableGroup<Product> {
    Integer zoneId;

    public ZoneGroup(String title, List<Product> items, Integer zoneId) {
        super(title, items);
        this.zoneId = zoneId;
    }

    public Integer getZoneId() {
        return zoneId;
    }
}
