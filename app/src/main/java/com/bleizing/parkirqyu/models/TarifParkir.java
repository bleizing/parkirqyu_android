package com.bleizing.parkirqyu.models;

import android.os.Parcel;
import android.os.Parcelable;

public class TarifParkir implements Parcelable {
    private int id;
    private int satuJam;
    private int tiapJam;
    private int perHari;

    public TarifParkir(int id, int satuJam, int tiapJam, int perHari) {
        this.id = id;
        this.satuJam = satuJam;
        this.tiapJam = tiapJam;
        this.perHari = perHari;
    }

    protected TarifParkir(Parcel in) {
        id = in.readInt();
        satuJam = in.readInt();
        tiapJam = in.readInt();
        perHari = in.readInt();
    }

    public static final Creator<TarifParkir> CREATOR = new Creator<TarifParkir>() {
        @Override
        public TarifParkir createFromParcel(Parcel in) {
            return new TarifParkir(in);
        }

        @Override
        public TarifParkir[] newArray(int size) {
            return new TarifParkir[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(satuJam);
        dest.writeInt(tiapJam);
        dest.writeInt(perHari);
    }
}
