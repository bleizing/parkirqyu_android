package com.bleizing.parkirqyu.models;

public class KendaraanPakir {
    private String infoParkir;
    private String nominal;
    private String nomorRegistrasi;

    public KendaraanPakir (String infoParkir, String nominal, String nomorRegistrasi) {
        this.infoParkir = infoParkir;
        this.nominal = nominal;
        this.nomorRegistrasi = nomorRegistrasi;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getNominal() {
        return nominal;
    }

    public void setInfoParkir(String infoParkir) {
        this.infoParkir = infoParkir;
    }

    public String getNomorRegistrasi() {
        return nomorRegistrasi;
    }

    public void setNomorRegistrasi(String nomorRegistrasi) {
        this.nomorRegistrasi = nomorRegistrasi;
    }

    public String getInfoParkir() {
        return infoParkir;
    }
}
