package com.nicbit.traquer.stryker.enums;

public enum CommentType {
    WEB_ADMIN(1), SALES_REP(2);

    private final int type;

    CommentType(int type) {
        this.type = type;
    }


    public int getType() {
        return type;
    }
}
