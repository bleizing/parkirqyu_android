package com.bleizing.parkirqyu.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetAllEmployeeResponse extends BaseResponse<ArrayList<GetAllEmployeeResponse.Data>> {

    @Override
    public void setData(ArrayList<Data> data) {
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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getNama() {
            return nama;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }
    }
}
