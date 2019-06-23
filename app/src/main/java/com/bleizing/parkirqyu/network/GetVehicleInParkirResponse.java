package com.bleizing.parkirqyu.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetVehicleInParkirResponse extends BaseResponse<ArrayList<GetVehicleInParkirResponse.Data>> {

    @Override
    public void setData(ArrayList<Data> data) {
        super.setData(data);
    }

    public class Data extends DataResponse {

        @SerializedName("info_parkir")
        @Expose
        private String infoParkir;

        @SerializedName("nominal")
        @Expose
        private String nominal;

        @SerializedName("nomor_registrasi")
        @Expose
        private String nomorRegistrasi;

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
}
