package com.bleizing.parkirqyu.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetParkirRateResponse extends BaseResponse<ArrayList<GetParkirRateResponse.Data>> {

    @Override
    public void setData(ArrayList<Data> data) {
        super.setData(data);
    }

    @Override
    public ArrayList<Data> getData() {
        return super.getData();
    }

    public class Data extends DataResponse {

        @SerializedName("id")
        @Expose
        private int id;

        @SerializedName("satu_jam_pertama")
        @Expose
        private int satuJam;

        @SerializedName("tiap_jam")
        @Expose
        private int tiapJam;

        @SerializedName("per_hari")
        @Expose
        private int perHari;

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setPerHari(int perHari) {
            this.perHari = perHari;
        }

        public int getPerHari() {
            return perHari;
        }

        public void setSatuJam(int satuJam) {
            this.satuJam = satuJam;
        }

        public int getSatuJam() {
            return satuJam;
        }

        public void setTiapJam(int tiapJam) {
            this.tiapJam = tiapJam;
        }

        public int getTiapJam() {
            return tiapJam;
        }
    }
}
