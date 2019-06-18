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
}
