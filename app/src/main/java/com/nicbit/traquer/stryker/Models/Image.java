package com.nicbit.traquer.stryker.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Image {

    @SerializedName("full")
    @Expose
    private String full;
    @SerializedName("thumb")
    @Expose
    private String thumb;

    /**
     * @return The full
     */
    public String getFull() {
        return full;
    }

    /**
     * @param full The full
     */
    public void setFull(String full) {
        this.full = full;
    }

    /**
     * @return The thumb
     */
    public String getThumb() {
        return thumb;
    }

    /**
     * @param thumb The thumb
     */
    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

}

