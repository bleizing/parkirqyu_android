package com.bleizing.parkirqyu.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseRequest {
    @SerializedName("user_id")
    @Expose
    private int userId;

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public BaseRequest(int userId) {
        this.userId = userId;
    }
}
