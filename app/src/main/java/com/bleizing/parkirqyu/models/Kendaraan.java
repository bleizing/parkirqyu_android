package com.bleizing.parkirqyu.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Kendaraan implements Parcelable {
    private int kendaraanId;
    private String nomorRegistrasi;
    private String nama;
    private String alamat;
    private String merk;
    private String type;
    private String tahunPembuatan;
    private String nomorRangka;
    private String nomorMesin;
    private String vehicleType;

    public Kendaraan(int kendaraanId, String nomorRegistrasi, String nama, String alamat, String merk, String type, String tahunPembuatan, String nomorRangka, String nomorMesin, String vehicleType) {
        this.kendaraanId = kendaraanId;
        this.nomorRegistrasi = nomorRegistrasi;
        this.nama = nama;
        this.alamat = alamat;
        this.merk = merk;
        this.type = type;
        this.tahunPembuatan = tahunPembuatan;
        this.nomorRangka = nomorRangka;
        this.nomorMesin = nomorMesin;
        this.vehicleType = vehicleType;
    }

    protected Kendaraan(Parcel in) {
        this.kendaraanId = in.readInt();
        this.nomorRegistrasi = in.readString();
        this.nama = in.readString();
        this.alamat = in.readString();
        this.merk = in.readString();
        this.type = in.readString();
        this.tahunPembuatan = in.readString();
        this.nomorRangka = in.readString();
        this.nomorMesin = in.readString();
        this.vehicleType = in.readString();
    }

    public static final Creator<Kendaraan> CREATOR = new Creator<Kendaraan>() {
        @Override
        public Kendaraan createFromParcel(Parcel in) {
            return new Kendaraan(in);
        }

        @Override
        public Kendaraan[] newArray(int size) {
            return new Kendaraan[size];
        }
    };

    public void setNomorRegistrasi(String nomorRegistrasi) {
        this.nomorRegistrasi = nomorRegistrasi;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
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

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(kendaraanId);
        dest.writeString(nomorRegistrasi);
        dest.writeString(nama);
        dest.writeString(alamat);
        dest.writeString(merk);
        dest.writeString(type);
        dest.writeString(tahunPembuatan);
        dest.writeString(nomorRangka);
        dest.writeString(nomorMesin);
        dest.writeString(vehicleType);
    }
}
