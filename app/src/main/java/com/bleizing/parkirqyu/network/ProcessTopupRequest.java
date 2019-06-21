package com.bleizing.parkirqyu.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProcessTopupRequest extends BaseRequest {

    @SerializedName("nominal")
    @Expose
    private int nominal;

    public ProcessTopupRequest(int userId, int nominal) {
        super(userId);
        this.nominal = nominal;
    }

    public void setNominal(int nominal) {
        this.nominal = nominal;
    }

    public int getNominal() {
        return nominal;
    }
}
