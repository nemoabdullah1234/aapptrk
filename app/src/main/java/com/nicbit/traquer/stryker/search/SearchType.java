package com.nicbit.traquer.stryker.search;

public enum SearchType {
    CASE(0), ITEM(1);

    private final int type;

    SearchType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}

