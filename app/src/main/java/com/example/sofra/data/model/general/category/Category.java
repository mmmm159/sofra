
package com.example.sofra.data.model.general.category;

import java.util.List;

import com.example.sofra.data.model.general.itemrestaurant.ItemRestaurantData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Category {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<ItemRestaurantData> data = null;

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

    public List<ItemRestaurantData> getData() {
        return data;
    }

    public void setData(List<ItemRestaurantData> data) {
        this.data = data;
    }

}
