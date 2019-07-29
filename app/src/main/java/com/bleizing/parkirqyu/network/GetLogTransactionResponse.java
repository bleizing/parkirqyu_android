package com.bleizing.parkirqyu.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetLogTransactionResponse extends BaseResponse<ArrayList<GetLogTransactionResponse.Data>> {

    @Override
    public void setData(ArrayList<Data> data) {
        super.setData(data);
    }

    @Override
    public ArrayList<Data> getData() {
        return super.getData();
    }

    public class Data extends DataResponse {
        @SerializedName("nomor_registrasi")
        @Expose
        private String nomorRegistrasi;

        @SerializedName("invoice_code")
        @Expose
        private String invoiceCode;

        @SerializedName("invoice_type")
        @Expose
        private String invoiceType;

        @SerializedName("nominal")
        @Expose
        private String nominal;

        @SerializedName("transaction_type")
        @Expose
        private String transactionType;

        @SerializedName("time")
        @Expose
        private String time;

        @SerializedName("nama_petugas")
        @Expose
        private String namaPetugas;

        @SerializedName("jenis_pelanggan")
        @Expose
        private String jenisPelanggan;

        public void setTransactionType(String transactionType) {
            this.transactionType = transactionType;
        }

        public String getTransactionType() {
            return transactionType;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTime() {
            return time;
        }

        public void setNamaPetugas(String namaPetugas) {
            this.namaPetugas = namaPetugas;
        }

        public String getNamaPetugas() {
            return namaPetugas;
        }

        public void setJenisPelanggan(String jenisPelanggan) {
            this.jenisPelanggan = jenisPelanggan;
        }

        public String getJenisPelanggan() {
            return jenisPelanggan;
        }

        public void setInvoiceType(String invoiceType) {
            this.invoiceType = invoiceType;
        }

        public String getInvoiceType() {
            return invoiceType;
        }

        public void setInvoiceCode(String invoiceCode) {
            this.invoiceCode = invoiceCode;
        }

        public String getNominal() {
            return nominal;
        }

        public void setNominal(String nominal) {
            this.nominal = nominal;
        }

        public String getInvoiceCode() {
            return invoiceCode;
        }

        public void setNomorRegistrasi(String nomorRegistrasi) {
            this.nomorRegistrasi = nomorRegistrasi;
        }

        public String getNomorRegistrasi() {
            return nomorRegistrasi;
        }
    }
}
