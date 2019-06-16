package com.bleizing.parkirqyu.network;

public class BaseRequest {
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
