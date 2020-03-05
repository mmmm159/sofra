package com.example.sofra.data.model.general.reviewnopagination;

import com.example.sofra.data.model.general.review.ReviewData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewNoPaginationData {

    @SerializedName("review")
    @Expose
    private ReviewData review;

    public ReviewData getReview() {
        return review;
    }

    public void setReview(ReviewData review) {
        this.review = review;
    }
}
