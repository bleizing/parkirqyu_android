package com.bleizing.parkirqyu.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetUserVehicleResponse extends BaseResponse<ArrayList<GetUserVehicleResponse.Data>> {

    @Override
    public void setData(ArrayList<Data> data) {
        super.setData(data);
    }

    public class Data extends DataResponse {
        @SerializedName("id")
        @Expose
        private int kendaraanId;

        @SerializedName("nomor_registrasi")
        @Expose
        private String nomorRegistrasi;

        @SerializedName("nama_pemilik")
        @Expose
        private String nama;

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
        private String vehicleType;

        public void setVehicleType(String vehicleType) {
            this.vehicleType = vehicleType;
        }

        public String getVehicleType() {
            return vehicleType;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public void setTahunPembuatan(String tahunPembuatan) {
            this.tahunPembuatan = tahunPembuatan;
        }

        public String getTahunPembuatan() {
            return tahunPembuatan;
        }

        public void setNomorRangka(String nomorRangka) {
            this.nomorRangka = nomorRangka;
        }

        public String getNomorRegistrasi() {
            return nomorRegistrasi;
        }

        public void setNomorMesin(String nomorMesin) {
            this.nomorMesin = nomorMesin;
        }

        public String getNomorRangka() {
            return nomorRangka;
        }

        public void setMerk(String merk) {
            this.merk = merk;
        }

        public String getNomorMesin() {
            return nomorMesin;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }

        public String getMerk() {
            return merk;
        }

        public void setAlamat(String alamat) {
            this.alamat = alamat;
        }

        public String getNama() {
            return nama;
        }

        public void setNomorRegistrasi(String nomorRegistrasi) {
            this.nomorRegistrasi = nomorRegistrasi;
        }

        public String getAlamat() {
            return alamat;
        }

        public void setKendaraanId(int kendaraanId) {
            this.kendaraanId = kendaraanId;
        }

        public int getKendaraanId() {
            return kendaraanId;
        }
    }
}
