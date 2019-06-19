package com.bleizing.parkirqyu;

public class Constants {
    public static final String BASE_URL = "https://parkirqyu.bleizing.com/";
    public static final String BASE_URL_API = BASE_URL + "api/";
    public static final String BASE_URL_BARCODE = BASE_URL + "vehicles/";

    public static final int STATUS_CODE_SUCCESS = 200;
    public static final int STATUS_CODE_CREATED = 201;
    public static final int STATUS_CODE_UPDATED = 202;
    public static final int STATUS_CODE_DELETED = 203;
    public static final int STATUS_CODE_BAD_REQUEST = 400;

    public static final String PREF_LOGGED_IN = "IsLoggedIn";
    public static final String PREF_USER_ID = "user_id";
    public static final String PREF_NAMA = "nama";
    public static final String PREF_JENIS_KELAMIN = "jenis_kelamin";
    public static final String PREF_TEMPAT_LAHIR = "tempat_lahir";
    public static final String PREF_TANGGAL_LAHIR = "tanggal_lahir";
    public static final String PREF_ALAMAT = "alamat";
    public static final String PREF_SALDO = "saldo";
    public static final String PREF_USER_TYPE = "user_type";

    public static final int PERMISSION_WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 690;
}
