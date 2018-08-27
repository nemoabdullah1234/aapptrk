package com.nicbit.traquer.stryker.enums;

import android.widget.ImageView;

import com.nicbit.traquer.stryker.R;

public enum CaseStatus {
    OPEN(10), IN_TRANSIT(40), PARTIAL_shipped(25), PARTIAL_delivered(45), DELIVERED(60), CLOSED(90), SUBMITTED(80), CANCELED(70),
    DRAFT(5);
    private final int type;

    CaseStatus(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static void setCaseImage(int status, ImageView statusImageView) {
        //  CaseStatus caseStatus =CaseStatus.values()[status].;

        if (CaseStatus.OPEN.getType() == status) {
            statusImageView.setImageResource(R.drawable.icon_new);
        } else if (CaseStatus.IN_TRANSIT.getType() == status) {
            statusImageView.setImageResource(R.drawable.icon_transit);
        } else if (CaseStatus.PARTIAL_shipped.getType() == status) {
            statusImageView.setImageResource(R.drawable.partial_shipped);

        } else if (CaseStatus.PARTIAL_delivered.getType() == status) {
            statusImageView.setImageResource(R.drawable.partial_delivered);

        } else if (CaseStatus.DELIVERED.getType() == status) {
            statusImageView.setImageResource(R.drawable.icon_delivered);
        } else if (CaseStatus.CLOSED.getType() == status) {
            statusImageView.setImageResource(R.drawable.icon_completed);
        } else if (CaseStatus.SUBMITTED.getType() == status) {
            statusImageView.setImageResource(R.drawable.icon_completed);
        }

    }
}
