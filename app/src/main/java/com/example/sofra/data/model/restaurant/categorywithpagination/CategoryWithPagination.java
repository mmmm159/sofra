
package com.example.sofra.data.model.restaurant.categorywithpagination;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryWithPagination {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private PaginationCategory data;

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

    public PaginationCategory getData() {
        return data;
    }

    public void setData(PaginationCategory data) {
        this.data = data;
    }

}
