package com.nicbit.traquer.stryker.enums;

public enum ItemStatus {
    UNUSED(1, "Unused", false), USED(2, "Used", true);

    private final int status;
    private final String name;
    private final boolean state;

    ItemStatus(int type, String name, boolean state) {
        this.status = type;
        this.name = name;
        this.state = state;
    }

    public int getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public static String getNameByStatus(int status) {
        String string = "";
        switch (status) {
            case 1:
                string = UNUSED.getName();
                break;
            case 2:
                string = USED.getName();
                break;

        }
        return string;

    }

    public static boolean getStateByStatus(int status) {
        boolean state = false;
        switch (status) {
            case 1:
                state = UNUSED.isState();
                break;
            case 2:
                state = USED.isState();
                break;

        }
        return state;

    }

    public static int getStatusByState(boolean state) {
        int status;
        if (state)
            status = USED.getStatus();
        else
            status = UNUSED.getStatus();
        return status;

    }

    public static String getStatusName(int statusCode) {
        String statusName = "";
        switch (statusCode) {
            case 0:
                statusName = "Open";
                break;
            case 1:
                statusName = "Open";
                break;
            case 2:
                statusName = "In Transit";
                break;
            case 3:
                statusName = "Delivered";
                break;


        }
        return statusName;

    }

    public boolean isState() {
        return state;
    }
}
