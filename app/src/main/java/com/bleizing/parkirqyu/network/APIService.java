package com.bleizing.parkirqyu.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIService {
    @POST("user/login")
    Call<LoginResponse> processLogin(@Body LoginRequest loginRequest);

    @POST("user/get_user_vehicle")
    Call<GetUserVehicleResponse> getUserVehicle(@Body BaseRequest baseRequest);

    @POST("admin/employee/get_all")
    Call<GetAllEmployeeResponse> getAllEmployee(@Body BaseRequest baseRequest);

    @POST("admin/employee/get_by_user_id")
    Call<GetEmployeeByUserIdResponse> getEmployeeByUserId(@Body GetEmployeeByUserIdRequest request);

    @POST("admin/employee/create")
    Call<AddEmployeeResponse> addEmployee(@Body AddEmployeeRequest request);

    @POST("admin/employee/edit")
    Call<EditEmployeeResponse> editEmployee(@Body EditEmployeeRequest request);

    @POST("admin/employee/delete")
    Call<DeleteEmployeeResponse> deleteEmployee(@Body DeleteEmployeeRequest request);

    @POST("admin/vehicle/get_by_user_id")
    Call<GetUserVehicleResponse> getVehicleByUserId(@Body GetKendaraanByUserRequest request);

    @POST("admin/vehicle/create")
    Call<AddVehicleResponse> addVehicle(@Body AddVehicleRequest request);

    @POST("admin/vehicle/edit")
    Call<EditVehicleResponse> editVehicle(@Body EditVehicleRequest request);

    @POST("admin/vehicle/delete")
    Call<DeleteVehicleResponse> deleteVehicle(@Body DeleteVehicleRequest request);

    @POST("parkir/check_in")
    Call<ProcessCheckInResponse> processCheckIn(@Body ProcessCheckInRequest request);
}
