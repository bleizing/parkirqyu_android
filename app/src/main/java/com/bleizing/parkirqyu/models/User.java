package com.bleizing.parkirqyu.models;

public class User {
    private int userId;
    private String nama;
    private String email;
    private String jenisKelamin;
    private String tempatLahir;
    private String tanggalLahir;
    private String alamat;
    private String saldo;
    private int userType;

    public User(int userId, String nama, String email, String jenisKelamin, String tempatLahir, String tanggalLahir, String alamat, String saldo, int userType) {
        this.userId = userId;
        this.nama = nama;
        this.email = email;
        this.jenisKelamin = jenisKelamin;
        this.tempatLahir = tempatLahir;
        this.tanggalLahir = tanggalLahir;
        this.alamat = alamat;
        this.saldo = saldo;
        this.userType = userType;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNama() {
        return nama;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public void setTempatLahir(String tempatLahir) {
        this.tempatLahir = tempatLahir;
    }

    public String getTempatLahir() {
        return tempatLahir;
    }

    public void setTanggalLahir(String tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }

    public String getTanggalLahir() {
        return tanggalLahir;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getUserType() {
        return userType;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
