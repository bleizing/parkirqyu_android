package com.bleizing.parkirqyu.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteVehicleRequest extends BaseRequest {

    @SerializedName("vehicle_id")
    @Expose
    private int vehicleId;

    public DeleteVehicleRequest(int userId, int vehicleId) {
        super(userId);
        this.vehicleId = vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public int getVehicleId() {
        return vehicleId;
    }
}
