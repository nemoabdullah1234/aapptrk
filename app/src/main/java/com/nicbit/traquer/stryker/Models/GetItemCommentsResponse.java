package com.nicbit.traquer.stryker.Models;

import java.util.ArrayList;
import java.util.List;

public class GetItemCommentsResponse {
    private Item itemDetails;
    private List<Comments> comments = new ArrayList<>();

    public Item getItemDetails() {
        return itemDetails;
    }

    public void setItemDetails(Item itemDetails) {
        this.itemDetails = itemDetails;
    }

    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }
}
