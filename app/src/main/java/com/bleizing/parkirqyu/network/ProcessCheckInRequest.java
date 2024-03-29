package com.bleizing.parkirqyu.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProcessCheckInRequest extends BaseRequest {

    @SerializedName("nomor_registrasi")
    @Expose
    private String nomorRegistrasi;

    @SerializedName("vehicle_type")
    @Expose
    private int vehicleType;

    @SerializedName("time_start")
    @Expose
    private String timeStart;

    public ProcessCheckInRequest(int userId, String nomorRegistrasi, int vehicleType) {
        super(userId);
        this.nomorRegistrasi = nomorRegistrasi;
        this.vehicleType = vehicleType;
    }

    public ProcessCheckInRequest(int userId, String nomorRegistrasi, int vehicleType, String timeStart) {
        super(userId);
        this.nomorRegistrasi = nomorRegistrasi;
        this.vehicleType = vehicleType;
        this.timeStart = timeStart;
    }

    public void setNomorRegistrasi(String nomorRegistrasi) {
        this.nomorRegistrasi = nomorRegistrasi;
    }

    public String getNomorRegistrasi() {
        return nomorRegistrasi;
    }

    public void setVehicleType(int vehicleType) {
        this.vehicleType = vehicleType;
    }

    public int getVehicleType() {
        return vehicleType;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeStart() {
        return timeStart;
    }
}
