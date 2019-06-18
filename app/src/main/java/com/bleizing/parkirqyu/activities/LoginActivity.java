package com.bleizing.parkirqyu.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bleizing.parkirqyu.Constants;
import com.bleizing.parkirqyu.R;
import com.bleizing.parkirqyu.models.Model;
import com.bleizing.parkirqyu.models.User;
import com.bleizing.parkirqyu.network.APIService;
import com.bleizing.parkirqyu.network.DataResponse;
import com.bleizing.parkirqyu.network.HTTPClient;
import com.bleizing.parkirqyu.network.LoginRequest;
import com.bleizing.parkirqyu.network.LoginResponse;
import com.bleizing.parkirqyu.utils.PrefUtils;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private EditText editEmail;
    private EditText editPassword;

    private String email;
    private String password;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = (EditText) findViewById(R.id.edit_email);
        editPassword = (EditText) findViewById(R.id.edit_password);

        Button btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editEmail.getText().toString();
                password = editPassword.getText().toString();

                if (!email.equals("") && !password.equals("")) {
                    progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setMessage("Mohon Tunggu...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    LoginRequest loginRequest = new LoginRequest(email, password);

                    APIService apiService = HTTPClient.getClient().create(APIService.class);
                    Call<LoginResponse> call = apiService.processLogin(loginRequest);
                    call.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            if (response.isSuccessful()) {
                                switch (response.body().getStatusCode()) {
                                    case Constants.STATUS_CODE_SUCCESS :
                                        loginSuccess(response.body().getData());
                                        break;
                                    case Constants.STATUS_CODE_BAD_REQUEST :
                                        if (response.body().getData().getErrorList() != null) {
                                            loginFailed(response.body().getData().getErrorList());
                                        } else {
                                            loginFailed(response.body().getData().getMessage());
                                        }
                                        break;
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            t.printStackTrace();
                            loginFailed(getString(R.string.connection_error));
                        }
                    });
                } else {
                    loginFailed(getString(R.string.data_incompleted));
                }
            }
        });
    }

    private void loginSuccess(LoginResponse.Data data) {
        int userId = data.getUserId();
        String nama = data.getNama();
        String jenisKelamin = data.getJenisKelamin();
        String tempatLahir = data.getTempatLahir();
        String tanggalLahir = data.getTanggalLahir();
        String alamat = data.getAlamat();
        String saldo = data.getSaldo();
        int userType = data.getUserType();

        User user = new User(userId, nama, jenisKelamin, tempatLahir, tanggalLahir, alamat, saldo, userType);
        Model.setUser(user);

        PrefUtils prefUtils = new PrefUtils(this);

        prefUtils.setLoggedIn(true);
        prefUtils.setUserId(userId);
        prefUtils.setNama(nama);
        prefUtils.setJenisKelamin(jenisKelamin);
        prefUtils.setTempatLahir(tempatLahir);
        prefUtils.setTanggalLahir(tanggalLahir);
        prefUtils.setAlamat(alamat);
        prefUtils.setSaldo(saldo);
        prefUtils.setUserType(userType);

        progressDialog.dismiss();

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void loginFailed(ArrayList<DataResponse.Error> errorList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (DataResponse.Error error : errorList) {
            stringBuilder.append(error.getMessage());
            stringBuilder.append("\n");
        }
        loginFailed(stringBuilder.toString());
    }

    private void loginFailed(String message) {
        progressDialog.dismiss();
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
    }
}
