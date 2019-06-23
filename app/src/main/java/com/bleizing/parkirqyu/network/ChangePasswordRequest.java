package com.bleizing.parkirqyu.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangePasswordRequest extends BaseRequest {

    @SerializedName("old_password")
    @Expose
    private String oldPassword;

    @SerializedName("new_password")
    @Expose
    private String newPassword;

    public ChangePasswordRequest(int userId, String oldPassword, String newPassword) {
        super(userId);
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }
}
