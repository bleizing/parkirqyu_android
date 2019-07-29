package com.bleizing.parkirqyu.models;

public class LogTransaksi {
    private String nomorRegistrasi;
    private String invoiceCode;
    private String invoiceType;
    private String nominal;
    private String transactionType;
    private String time;
    private String namaPetugas;
    private String jenisPelanggan;

    public LogTransaksi(String nomorRegistrasi, String invoiceCode, String invoiceType, String nominal, String transactionType, String time, String namaPetugas, String jenisPelanggan) {
        this.nomorRegistrasi = nomorRegistrasi;
        this.invoiceCode = invoiceCode;
        this.invoiceType = invoiceType;
        this.nominal = nominal;
        this.transactionType = transactionType;
        this.time = time;
        this.namaPetugas = namaPetugas;
        this.jenisPelanggan = jenisPelanggan;
    }

    public void setNomorRegistrasi(String nomorRegistrasi) {
        this.nomorRegistrasi = nomorRegistrasi;
    }

    public String getNomorRegistrasi() {
        return nomorRegistrasi;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public String getNominal() {
        return nominal;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setJenisPelanggan(String jenisPelanggan) {
        this.jenisPelanggan = jenisPelanggan;
    }

    public String getJenisPelanggan() {
        return jenisPelanggan;
    }

    public void setNamaPetugas(String namaPetugas) {
        this.namaPetugas = namaPetugas;
    }

    public String getNamaPetugas() {
        return namaPetugas;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionType() {
        return transactionType;
    }
}
