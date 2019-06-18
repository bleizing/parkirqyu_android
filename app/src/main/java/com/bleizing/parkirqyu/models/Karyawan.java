package com.bleizing.parkirqyu.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Karyawan implements Parcelable {
    private int userId;
    private String email;
    private String nama;
    private String jenisKelamin;
    private String alamat;
    private String tempatLahir;
    private String tanggalLahir;
    private String userType;

    public Karyawan(int userId, String email, String nama) {
        this.userId = userId;
        this.email = email;
        this.nama = nama;
    }

    public Karyawan(int userId, String email, String nama, String jenisKelamin, String alamat, String tempatLahir, String tanggalLahir, String userType) {
        this.userId = userId;
        this.email = email;
        this.nama = nama;
        this.jenisKelamin = jenisKelamin;
        this.alamat = alamat;
        this.tempatLahir = tempatLahir;
        this.tanggalLahir = tanggalLahir;
        this.userType = userType;
    }

    protected Karyawan(Parcel in) {
        userId = in.readInt();
        email = in.readString();
        nama = in.readString();
        jenisKelamin = in.readString();
        alamat = in.readString();
        tempatLahir = in.readString();
        tanggalLahir = in.readString();
        userType = in.readString();
    }

    public static final Creator<Karyawan> CREATOR = new Creator<Karyawan>() {
        @Override
        public Karyawan createFromParcel(Parcel in) {
            return new Karyawan(in);
        }

        @Override
        public Karyawan[] newArray(int size) {
            return new Karyawan[size];
        }
    };

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNama() {
        return nama;
    }

    public void setTanggalLahir(String tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }

    public int getUserId() {
        return userId;
    }

    public void setTempatLahir(String tempatLahir) {
        this.tempatLahir = tempatLahir;
    }

    public String getTanggalLahir() {
        return tanggalLahir;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public String getTempatLahir() {
        return tempatLahir;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserType() {
        return userType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeString(email);
        dest.writeString(nama);
        dest.writeString(jenisKelamin);
        dest.writeString(alamat);
        dest.writeString(tempatLahir);
        dest.writeString(tanggalLahir);
        dest.writeString(userType);
    }
}
