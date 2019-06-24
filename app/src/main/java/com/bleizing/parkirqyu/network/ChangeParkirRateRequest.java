package com.bleizing.parkirqyu.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangeParkirRateRequest extends BaseRequest {

    @SerializedName("parkir_rate_id")
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

    public ChangeParkirRateRequest(int userId, int id, int satuJam, int tiapJam, int perHari) {
        super(userId);
        this.id = id;
        this.satuJam = satuJam;
        this.tiapJam = tiapJam;
        this.perHari = perHari;
    }

    public void setTiapJam(int tiapJam) {
        this.tiapJam = tiapJam;
    }

    public int getTiapJam() {
        return tiapJam;
    }

    public void setSatuJam(int satuJam) {
        this.satuJam = satuJam;
    }

    public int getSatuJam() {
        return satuJam;
    }

    public void setPerHari(int perHari) {
        this.perHari = perHari;
    }

    public int getPerHari() {
        return perHari;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
