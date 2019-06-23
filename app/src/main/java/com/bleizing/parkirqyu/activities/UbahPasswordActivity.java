package com.bleizing.parkirqyu.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bleizing.parkirqyu.Constants;
import com.bleizing.parkirqyu.R;
import com.bleizing.parkirqyu.models.Model;
import com.bleizing.parkirqyu.network.APIService;
import com.bleizing.parkirqyu.network.ChangePasswordRequest;
import com.bleizing.parkirqyu.network.ChangePasswordResponse;
import com.bleizing.parkirqyu.network.DataResponse;
import com.bleizing.parkirqyu.network.HTTPClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UbahPasswordActivity extends AppCompatActivity {

    private EditText editOldPassword;
    private EditText editNewPassword;
    private EditText editKonfirmasiPassword;

    private String oldPassword;
    private String newPassword;
    private String konfirmasiPassword;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_password);

        editOldPassword = (EditText) findViewById(R.id.edit_old_password);
        editNewPassword = (EditText) findViewById(R.id.edit_new_password);
        editKonfirmasiPassword = (EditText) findViewById(R.id.edit_konfirmasi_password);

        Button btnUbah = (Button) findViewById(R.id.btn_ubah);
        btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldPassword = editOldPassword.getText().toString();
                newPassword = editNewPassword.getText().toString();
                konfirmasiPassword = editKonfirmasiPassword.getText().toString();

                if (!oldPassword.equals("") && !newPassword.equals("") && !konfirmasiPassword.equals("")) {
                    if (newPassword.equals(konfirmasiPassword)) {
                        processUbahPassword(oldPassword, newPassword);
                    } else {
                        showToast("Konfirmasi Password harus sama dengan Password Baru");
                    }
                } else {
                    showToast(getString(R.string.data_incompleted));
                }
            }
        });
    }

    private void processUbahPassword(String oldPassword, String newPassword) {
        progressDialog = new ProgressDialog(UbahPasswordActivity.this);
        progressDialog.setMessage("Mohon Tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ChangePasswordRequest request = new ChangePasswordRequest(Model.getUser().getUserId(), oldPassword, newPassword);

        APIService apiService = HTTPClient.getClient().create(APIService.class);
        Call<ChangePasswordResponse> call = apiService.processChangePassword(request);
        call.enqueue(new Callback<ChangePasswordResponse>() {
            @Override
            public void onResponse(Call<ChangePasswordResponse> call, Response<ChangePasswordResponse> response) {
                if (response.isSuccessful()) {
                    switch (response.body().getStatusCode()) {
                        case Constants.STATUS_CODE_UPDATED :
                            showToast("Password berhasil diubah");

                            onBackPressed();
                            break;
                        case Constants.STATUS_CODE_BAD_REQUEST :
                            if (response.body().getData().getErrorList() != null) {
                                failed(response.body().getData().getErrorList());
                            } else {
                                showToast(response.body().getData().getMessage());
                            }
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<ChangePasswordResponse> call, Throwable t) {
                t.printStackTrace();
                showToast(getString(R.string.connection_error));
            }
        });
    }

    private void failed(ArrayList<DataResponse.Error> errorList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (DataResponse.Error error : errorList) {
            stringBuilder.append(error.getMessage());
            stringBuilder.append("\n");
        }
        showToast(stringBuilder.toString());
    }

    private void showToast(String message) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        Toast.makeText(UbahPasswordActivity.this, message, Toast.LENGTH_LONG).show();
    }
}
