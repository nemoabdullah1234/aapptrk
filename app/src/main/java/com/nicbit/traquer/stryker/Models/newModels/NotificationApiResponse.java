package com.nicbit.traquer.stryker.Models.newModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nicbit.traquer.stryker.Models.ReaderGetNotificationsResponse;

import java.util.List;

/**
 * Created by rohitkumar on 10/5/17.
 */

public class NotificationApiResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private List<ReaderGetNotificationsResponse> readerGetNotificationsResponses;

    public List<ReaderGetNotificationsResponse> getReaderGetNotificationsResponses() {
        return readerGetNotificationsResponses;
    }

    public void setReaderGetNotificationsResponses(List<ReaderGetNotificationsResponse> readerGetNotificationsResponses) {
        this.readerGetNotificationsResponses = readerGetNotificationsResponses;
    }
}
