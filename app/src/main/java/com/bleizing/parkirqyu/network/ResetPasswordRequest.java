package com.bleizing.parkirqyu.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResetPasswordRequest extends BaseRequest {

    @SerializedName("employee_id")
    @Expose
    private int employeeId;

    public ResetPasswordRequest(int userId, int employeeId) {
        super(userId);
        this.employeeId = employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getEmployeeId() {
        return employeeId;
    }
}
