package com.bleizing.parkirqyu.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse extends BaseResponse<LoginResponse.Data> {

    @Override
    public void setData(LoginResponse.Data data) {
        super.setData(data);
    }

    public class Data extends DataResponse {
        @SerializedName("user_id")
        @Expose
        private int userId;

        @SerializedName("nama")
        @Expose
        private String nama;

        @SerializedName("email")
        @Expose
        private String email;

        @SerializedName("jenis_kelamin")
        @Expose
        private String jenisKelamin;

        @SerializedName("tempat_lahir")
        @Expose
        private String tempatLahir;

        @SerializedName("tanggal_lahir")
        @Expose
        private String tanggalLahir;

        @SerializedName("alamat")
        @Expose
        private String alamat;

        @SerializedName("saldo")
        @Expose
        private String saldo;

        @SerializedName("user_type")
        @Expose
        private int userType;

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
}
