package com.bleizing.parkirqyu.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProcessTopupResponse extends BaseResponse<ProcessTopupResponse.Data> {

    @Override
    public void setData(Data data) {
        super.setData(data);
    }

    public class Data extends DataResponse {
        @SerializedName("invoice_code")
        @Expose
        private String invoiceCode;

        public void setInvoiceCode(String invoiceCode) {
            this.invoiceCode = invoiceCode;
        }

        public String getInvoiceCode() {
            return invoiceCode;
        }
    }
}
