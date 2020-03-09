
package com.example.sofra.data.model.client.reviewnopagination;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewNoPagination {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private ReviewNoPaginationData data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ReviewNoPaginationData getData() {
        return data;
    }

    public void setData(ReviewNoPaginationData data) {
        this.data = data;
    }

}
