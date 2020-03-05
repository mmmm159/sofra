
package com.example.sofra.data.model.general.reset;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Reset {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private ResetData data;

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

    public ResetData getData() {
        return data;
    }

    public void setData(ResetData data) {
        this.data = data;
    }

}
