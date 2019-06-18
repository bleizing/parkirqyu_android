package com.bleizing.parkirqyu.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetEmployeeByUserIdResponse extends BaseResponse<GetEmployeeByUserIdResponse.Data> {

    @Override
    public void setData(Data data) {
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

        @SerializedName("alamat")
        @Expose
        private String alamat;

        @SerializedName("tempat_lahir")
        @Expose
        private String tempatLahir;

        @SerializedName("tanggal_lahir")
        @Expose
        private String tanggalLahir;

        @SerializedName("user_type")
        @Expose
        private String userType;

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getUserType() {
            return userType;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }

        public String getNama() {
            return nama;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getUserId() {
            return userId;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getEmail() {
            return email;
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
    }
}
