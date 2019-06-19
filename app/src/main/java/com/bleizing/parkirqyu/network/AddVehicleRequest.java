package com.bleizing.parkirqyu.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddVehicleRequest extends BaseRequest {

    @SerializedName("employee_id")
    @Expose
    private int employeeId;

    @SerializedName("nomor_registrasi")
    @Expose
    private String nomorRegistrasi;

    @SerializedName("nama_pemilik")
    @Expose
    private String namaPemilik;

    @SerializedName("alamat")
    @Expose
    private String alamat;

    @SerializedName("merk")
    @Expose
    private String merk;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("tahun_pembuatan")
    @Expose
    private String tahunPembuatan;

    @SerializedName("nomor_rangka")
    @Expose
    private String nomorRangka;

    @SerializedName("nomor_mesin")
    @Expose
    private String nomorMesin;

    @SerializedName("vehicle_type")
    @Expose
    private String vehilceType;

    public AddVehicleRequest(int userId, int employeeId, String nomorRegistrasi, String namaPemilik, String alamat, String merk, String type, String tahunPembuatan, String nomorRangka, String nomorMesin, String vehilceType) {
        super(userId);
        this.employeeId = employeeId;
        this.nomorRegistrasi = nomorRegistrasi;
        this.namaPemilik = namaPemilik;
        this.alamat = alamat;
        this.merk = merk;
        this.type = type;
        this.tahunPembuatan = tahunPembuatan;
        this.nomorRangka = nomorRangka;
        this.nomorMesin = nomorMesin;
        this.vehilceType = vehilceType;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setNomorRegistrasi(String nomorRegistrasi) {
        this.nomorRegistrasi = nomorRegistrasi;
    }

    public String getMerk() {
        return merk;
    }

    public void setMerk(String merk) {
        this.merk = merk;
    }

    public String getNomorMesin() {
        return nomorMesin;
    }

    public void setNomorMesin(String nomorMesin) {
        this.nomorMesin = nomorMesin;
    }

    public String getNomorRangka() {
        return nomorRangka;
    }

    public void setNomorRangka(String nomorRangka) {
        this.nomorRangka = nomorRangka;
    }

    public String getNomorRegistrasi() {
        return nomorRegistrasi;
    }

    public void setTahunPembuatan(String tahunPembuatan) {
        this.tahunPembuatan = tahunPembuatan;
    }

    public String getTahunPembuatan() {
        return tahunPembuatan;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setNamaPemilik(String namaPemilik) {
        this.namaPemilik = namaPemilik;
    }

    public String getNamaPemilik() {
        return namaPemilik;
    }

    public void setVehilceType(String vehilceType) {
        this.vehilceType = vehilceType;
    }

    public String getVehilceType() {
        return vehilceType;
    }
}
