package com.bleizing.parkirqyu.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProcessPreCheckOutRequest extends BaseRequest {

    @SerializedName("nomor_registrasi")
    @Expose
    private String nomorRegistrasi;

    public ProcessPreCheckOutRequest(int userId, String nomorRegistrasi) {
        super(userId);
        this.nomorRegistrasi = nomorRegistrasi;
    }

    public void setNomorRegistrasi(String nomorRegistrasi) {
        this.nomorRegistrasi = nomorRegistrasi;
    }

    public String getNomorRegistrasi() {
        return nomorRegistrasi;
    }
}
