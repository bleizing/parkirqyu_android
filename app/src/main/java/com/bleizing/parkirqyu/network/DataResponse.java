package com.bleizing.parkirqyu.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DataResponse {
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("error")
    @Expose
    private ArrayList<Error> errorList;

    public class Error {
        @SerializedName("message")
        @Expose
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Error> getErrorList() {
        return errorList;
    }

    public void setErrorList(ArrayList<Error> errorList) {
        this.errorList = errorList;
    }
}
