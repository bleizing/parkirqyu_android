package com.bleizing.parkirqyu.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIService {
    @POST("user/login")
    Call<LoginResponse> processLogin(@Body LoginRequest loginRequest);

    @POST("user/get_user_info")
    Call<LoginResponse> getUserInfo(@Body BaseRequest request);

    @POST("user/get_user_vehicle")
    Call<GetUserVehicleResponse> getUserVehicle(@Body BaseRequest baseRequest);

    @POST("user/change_password")
    Call<ChangePasswordResponse> processChangePassword(@Body ChangePasswordRequest request);

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

    @POST("admin/employee/reset_password")
    Call<ResetPasswordResponse> resetPassword(@Body ResetPasswordRequest request);

    @POST("admin/vehicle/get_by_user_id")
    Call<GetUserVehicleResponse> getVehicleByUserId(@Body GetKendaraanByUserRequest request);

    @POST("admin/vehicle/create")
    Call<AddVehicleResponse> addVehicle(@Body AddVehicleRequest request);

    @POST("admin/vehicle/edit")
    Call<EditVehicleResponse> editVehicle(@Body EditVehicleRequest request);

    @POST("admin/vehicle/delete")
    Call<DeleteVehicleResponse> deleteVehicle(@Body DeleteVehicleRequest request);

    @POST("admin/parkir_rate/edit")
    Call<ChangeTarifParkirResponse> changeParkirRate(@Body ChangeParkirRateRequest request);

    @POST("admin/transaction/get_log_transaction")
    Call<GetLogTransactionResponse> getAllLogTransaction(@Body BaseRequest baseRequest);

    @POST("admin/transaction/in_parkir")
    Call<GetVehicleInParkirResponse> getAllVehicleParkir(@Body BaseRequest baseRequest);

    @GET("parkir/get_parkir_rate")
    Call<GetParkirRateResponse> getParkirRate();

    @POST("parkir/check_in")
    Call<ProcessCheckInResponse> processCheckIn(@Body ProcessCheckInRequest request);

    @POST("parkir/pre_check_out")
    Call<ProcessPreCheckOutResponse> processPreCheckOut(@Body ProcessPreCheckOutRequest request);

    @POST("parkir/check_out")
    Call<ProcessCheckOutResponse> processCheckOut(@Body ProcessCheckOutRequest request);

    @POST("topup/topup")
    Call<ProcessTopupResponse> processTopup(@Body ProcessTopupRequest request);

    @POST("parkir/in_parkir")
    Call<GetVehicleInParkirResponse> getVehicleParkir(@Body BaseRequest baseRequest);

    @POST("parkir/get_user_log_transaction")
    Call<GetLogTransactionResponse> getUserLogTransaction(@Body BaseRequest baseRequest);



}
