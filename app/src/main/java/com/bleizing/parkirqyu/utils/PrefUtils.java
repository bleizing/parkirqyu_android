package com.bleizing.parkirqyu.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.bleizing.parkirqyu.Constants;
import com.bleizing.parkirqyu.models.User;

public class PrefUtils {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    // shared pref mode
    private int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "parkirqyu";

    public PrefUtils(Context context) {
        this.context = context;
        this.pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLoggedIn(boolean loggedIn) {
        editor.putBoolean(Constants.PREF_LOGGED_IN, loggedIn);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(Constants.PREF_LOGGED_IN, false);
    }

    public void saveUser(User user) {
        this.setUserId(user.getUserId());
        this.setNama(user.getNama());
        this.setEmail(user.getEmail());
        this.setJenisKelamin(user.getJenisKelamin());
        this.setTempatLahir(user.getTempatLahir());
        this.setTanggalLahir(user.getTanggalLahir());
        this.setAlamat(user.getAlamat());
        this.setSaldo(user.getSaldo());
        this.setUserType(user.getUserType());
    }

    public User getUser() {
        int userId = this.getUserId();
        String nama = this.getNama();
        String email = this.getEmail();
        String jenisKelamin = this.getJenisKelamin();
        String tempatLahir = this.getTempatLahir();
        String tanggalLahir = this.getTanggalLahir();
        String alamat = this.getAlamat();
        String saldo = this.getSaldo();
        int userType = this.getUserType();

        return new User(userId, nama, email, jenisKelamin, tempatLahir, tanggalLahir, alamat, saldo, userType);
    }

    public void clearUser() {
        this.setLoggedIn(false);
        this.setUserId(0);
        this.setNama("");
        this.setEmail("");
        this.setJenisKelamin("");
        this.setTempatLahir("");
        this.setTanggalLahir("");
        this.setAlamat("");
        this.setSaldo("");
        this.setUserType(0);
    }

    public void setUserId(int userId) {
        editor.putInt(Constants.PREF_USER_ID, userId);
        editor.commit();
    }

    public int getUserId() {
        return pref.getInt(Constants.PREF_USER_ID, 0);
    }

    public void setNama(String nama) {
        editor.putString(Constants.PREF_NAMA, nama);
        editor.commit();
    }

    public String getNama() {
        return pref.getString(Constants.PREF_NAMA, "");
    }

    public void setEmail(String email) {
        editor.putString(Constants.PREF_EMAIL, email);
        editor.commit();
    }

    public String getEmail() {
        return pref.getString(Constants.PREF_EMAIL, "");
    }

    public void setJenisKelamin(String jenisKelamin) {
        editor.putString(Constants.PREF_JENIS_KELAMIN, jenisKelamin);
        editor.commit();
    }

    public String getJenisKelamin() {
        return pref.getString(Constants.PREF_JENIS_KELAMIN, "");
    }

    public void setTempatLahir(String tempatLahir) {
        editor.putString(Constants.PREF_TEMPAT_LAHIR, tempatLahir);
        editor.commit();
    }

    public String getTempatLahir() {
        return pref.getString(Constants.PREF_TEMPAT_LAHIR, "");
    }

    public void setTanggalLahir(String tanggalLahir) {
        editor.putString(Constants.PREF_TANGGAL_LAHIR, tanggalLahir);
        editor.commit();
    }

    public String getTanggalLahir() {
        return pref.getString(Constants.PREF_TANGGAL_LAHIR, "");
    }

    public void setAlamat(String alamat) {
        editor.putString(Constants.PREF_ALAMAT, alamat);
        editor.commit();
    }

    public String getAlamat() {
        return pref.getString(Constants.PREF_ALAMAT, "");
    }

    public void setSaldo(String saldo) {
        editor.putString(Constants.PREF_SALDO, saldo);
        editor.commit();
    }

    public String getSaldo() {
        return pref.getString(Constants.PREF_SALDO, "");
    }

    public void setUserType(int userType) {
        editor.putInt(Constants.PREF_USER_TYPE, userType);
        editor.commit();
    }

    public int getUserType() {
        return pref.getInt(Constants.PREF_USER_TYPE,0);
    }
}
