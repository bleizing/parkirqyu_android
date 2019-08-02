package com.bleizing.parkirqyu.models;

import com.orm.SugarRecord;

public class KendaraanCheckIn extends SugarRecord {
    private String nomorRegistrasi;
    private int vehicleType;
    private String timeStart;

    public KendaraanCheckIn() {

    }

    public KendaraanCheckIn(String nomorRegistrasi, int vehicleType, String timeStart) {
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
