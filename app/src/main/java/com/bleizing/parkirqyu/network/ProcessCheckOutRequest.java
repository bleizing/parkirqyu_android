package com.bleizing.parkirqyu.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProcessCheckOutRequest extends BaseRequest {

    @SerializedName("invoice_id")
    @Expose
    private int invoiceId;

    @SerializedName("payment_type")
    @Expose
    private int paymentType;

    public ProcessCheckOutRequest(int userId, int invoiceId, int paymentType) {
        super(userId);
        this.invoiceId = invoiceId;
        this.paymentType = paymentType;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public int getPaymentType() {
        return paymentType;
    }
}
