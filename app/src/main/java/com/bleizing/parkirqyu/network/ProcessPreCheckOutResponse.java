package com.bleizing.parkirqyu.network;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProcessPreCheckOutResponse extends BaseResponse<ProcessPreCheckOutResponse.Data> {

    @Override
    public void setData(Data data) {
        super.setData(data);
    }

    public static class Data extends DataResponse implements Parcelable {

        @SerializedName("invoice_id")
        @Expose
        private int invoiceId;

        @SerializedName("invoice_code")
        @Expose
        private String invoiceCode;

        @SerializedName("nomor_registrasi")
        @Expose
        private String nomorRegistrasi;

        @SerializedName("durasi_parkir")
        @Expose
        private String durasiParkir;

        @SerializedName("nominal")
        @Expose
        private String nominal;

        @SerializedName("vehicle_type")
        @Expose
        private String vehicleType;

        @SerializedName("parkir_start")
        @Expose
        private String parkirStart;

        @SerializedName("parkir_end")
        @Expose
        private String parkirEnd;

        @SerializedName("saldo_enough")
        @Expose
        private int saldoEnough;

        protected Data(Parcel in) {
            invoiceId = in.readInt();
            invoiceCode = in.readString();
            nomorRegistrasi = in.readString();
            durasiParkir = in.readString();
            nominal = in.readString();
            vehicleType = in.readString();
            parkirStart = in.readString();
            parkirEnd = in.readString();
            saldoEnough = in.readInt();
        }

        public static final Creator<Data> CREATOR = new Creator<Data>() {
            @Override
            public Data createFromParcel(Parcel in) {
                return new Data(in);
            }

            @Override
            public Data[] newArray(int size) {
                return new Data[size];
            }
        };

        public void setNomorRegistrasi(String nomorRegistrasi) {
            this.nomorRegistrasi = nomorRegistrasi;
        }

        public String getNomorRegistrasi() {
            return nomorRegistrasi;
        }

        public void setVehicleType(String vehicleType) {
            this.vehicleType = vehicleType;
        }

        public String getVehicleType() {
            return vehicleType;
        }

        public void setInvoiceCode(String invoiceCode) {
            this.invoiceCode = invoiceCode;
        }

        public int getInvoiceId() {
            return invoiceId;
        }

        public void setInvoiceId(int invoiceId) {
            this.invoiceId = invoiceId;
        }

        public String getDurasiParkir() {
            return durasiParkir;
        }

        public void setDurasiParkir(String durasiParkir) {
            this.durasiParkir = durasiParkir;
        }

        public int getSaldoEnough() {
            return saldoEnough;
        }

        public void setNominal(String nominal) {
            this.nominal = nominal;
        }

        public String getInvoiceCode() {
            return invoiceCode;
        }

        public void setParkirEnd(String parkirEnd) {
            this.parkirEnd = parkirEnd;
        }

        public String getNominal() {
            return nominal;
        }

        public void setParkirStart(String parkirStart) {
            this.parkirStart = parkirStart;
        }

        public String getParkirEnd() {
            return parkirEnd;
        }

        public void setSaldoEnough(int saldoEnough) {
            this.saldoEnough = saldoEnough;
        }

        public String getParkirStart() {
            return parkirStart;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(invoiceId);
            dest.writeString(invoiceCode);
            dest.writeString(nomorRegistrasi);
            dest.writeString(durasiParkir);
            dest.writeString(nominal);
            dest.writeString(vehicleType);
            dest.writeString(parkirStart);
            dest.writeString(parkirEnd);
            dest.writeInt(saldoEnough);
        }
    }
}
