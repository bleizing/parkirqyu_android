package com.bleizing.parkirqyu.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bleizing.parkirqyu.Constants;
import com.bleizing.parkirqyu.R;
import com.bleizing.parkirqyu.models.Karyawan;
import com.bleizing.parkirqyu.models.Model;
import com.bleizing.parkirqyu.network.APIService;
import com.bleizing.parkirqyu.network.DataResponse;
import com.bleizing.parkirqyu.network.DeleteEmployeeRequest;
import com.bleizing.parkirqyu.network.DeleteEmployeeResponse;
import com.bleizing.parkirqyu.network.GetEmployeeByUserIdRequest;
import com.bleizing.parkirqyu.network.GetEmployeeByUserIdResponse;
import com.bleizing.parkirqyu.network.HTTPClient;
import com.bleizing.parkirqyu.network.ResetPasswordRequest;
import com.bleizing.parkirqyu.network.ResetPasswordResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KaryawanDetailActivity extends AppCompatActivity {

    private TextView tvNama;
    private TextView tvEmail;
    private TextView tvJenisKelamin;
    private TextView tvAlamat;
    private TextView tvTempatLahir;
    private TextView tvTanggalLahir;
    private TextView tvSebagai;

    private Karyawan karyawan;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karyawan_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        karyawan = null;

        Intent intent = getIntent();
        if (intent.getParcelableExtra("karyawan") != null) {
            karyawan = intent.getParcelableExtra("karyawan");
        }

        tvNama = (TextView) findViewById(R.id.tv_nama);
        tvEmail = (TextView) findViewById(R.id.tv_email);
        tvJenisKelamin = (TextView) findViewById(R.id.tv_jenis_kelamin);
        tvAlamat = (TextView) findViewById(R.id.tv_alamat);
        tvTempatLahir = (TextView) findViewById(R.id.tv_tempat_lahir);
        tvTanggalLahir = (TextView) findViewById(R.id.tv_tanggal_lahir);
        tvSebagai = (TextView) findViewById(R.id.tv_sebagai);

        getKaryawanDetail();

        Button btnUbah = (Button) findViewById(R.id.btn_ubah);
        btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KaryawanDetailActivity.this, KaryawanFormActivity.class);
                intent.putExtra("karyawan", karyawan);
                startActivity(intent);
                finish();
            }
        });

        Button btnHapus = (Button) findViewById(R.id.btn_hapus);
        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(KaryawanDetailActivity.this)
                        .setTitle(KaryawanDetailActivity.this.getString(R.string.konfirmasi))
                        .setMessage(KaryawanDetailActivity.this.getString(R.string.konfirmasi_hapus))
                        .setPositiveButton(KaryawanDetailActivity.this.getString(R.string.konfirmasi_yakin), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                hapusEmployee(karyawan.getUserId());
                            }
                        })
                        .setNegativeButton(KaryawanDetailActivity.this.getString(R.string.konfirmasi_batal), null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setCancelable(false)
                        .show();
            }
        });

        Button btnResetPassword = (Button) findViewById(R.id.btn_reset_password);
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(KaryawanDetailActivity.this)
                        .setTitle(KaryawanDetailActivity.this.getString(R.string.konfirmasi))
                        .setMessage(KaryawanDetailActivity.this.getString(R.string.konfirmas_reset_password))
                        .setPositiveButton(KaryawanDetailActivity.this.getString(R.string.konfirmasi_yakin), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                resetPassword(karyawan.getUserId());
                            }
                        })
                        .setNegativeButton(KaryawanDetailActivity.this.getString(R.string.konfirmasi_batal), null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setCancelable(false)
                        .show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(KaryawanDetailActivity.this, KaryawanActivity.class);
        startActivity(intent);
        finish();
    }

    private void getKaryawanDetail() {
        progressDialog = new ProgressDialog(KaryawanDetailActivity.this);
        progressDialog.setMessage("Mohon Tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        GetEmployeeByUserIdRequest request = new GetEmployeeByUserIdRequest(Model.getUser().getUserId(), karyawan.getUserId());

        APIService apiService = HTTPClient.getClient().create(APIService.class);
        Call<GetEmployeeByUserIdResponse> call = apiService.getEmployeeByUserId(request);
        call.enqueue(new Callback<GetEmployeeByUserIdResponse>() {
            @Override
            public void onResponse(Call<GetEmployeeByUserIdResponse> call, Response<GetEmployeeByUserIdResponse> response) {
                if (response.isSuccessful()) {
                    switch (response.body().getStatusCode()) {
                        case Constants.STATUS_CODE_SUCCESS :
                            getEmployeeSuccess(response.body().getData());
                            break;
                        case Constants.STATUS_CODE_BAD_REQUEST :
                            Toast.makeText(KaryawanDetailActivity.this, getString(R.string.data_empty), Toast.LENGTH_LONG).show();
                            onBackPressed();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<GetEmployeeByUserIdResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(KaryawanDetailActivity.this, getString(R.string.connection_error), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                onBackPressed();
            }
        });
    }

    private void getEmployeeSuccess(GetEmployeeByUserIdResponse.Data data) {
        int userId = data.getUserId();
        String nama = data.getNama();
        String email = data.getEmail();
        String jenisKelamin = data.getJenisKelamin();
        String alamat = data.getAlamat();
        String tempatLahir = data.getTempatLahir();
        String tanggalLahir = data.getTanggalLahir();
        String userType = data.getUserType();

        karyawan = new Karyawan(userId, email, nama, jenisKelamin, alamat, tempatLahir, tanggalLahir, userType);
        setData();

        progressDialog.dismiss();
    }

    private void setData() {
        tvNama.setText(karyawan.getNama());
        tvEmail.setText(karyawan.getEmail());
        tvJenisKelamin.setText(karyawan.getJenisKelamin());
        tvAlamat.setText(karyawan.getAlamat());
        tvTempatLahir.setText(karyawan.getTempatLahir());
        tvTanggalLahir.setText(karyawan.getTanggalLahir());
        tvSebagai.setText(karyawan.getUserType());
    }

    private void hapusEmployee(int employeeId) {
        progressDialog = new ProgressDialog(KaryawanDetailActivity.this);
        progressDialog.setMessage("Sedang Diproses...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        DeleteEmployeeRequest request = new DeleteEmployeeRequest(Model.getUser().getUserId(), employeeId);

        APIService apiService = HTTPClient.getClient().create(APIService.class);
        Call<DeleteEmployeeResponse> call = apiService.deleteEmployee(request);
        call.enqueue(new Callback<DeleteEmployeeResponse>() {
            @Override
            public void onResponse(Call<DeleteEmployeeResponse> call, Response<DeleteEmployeeResponse> response) {
                if (response.isSuccessful()) {
                    switch (response.body().getStatusCode()) {
                        case Constants.STATUS_CODE_DELETED :
                            showToast(getString(R.string.delete_data_success));
                            onBackPressed();
                            break;
                        case Constants.STATUS_CODE_BAD_REQUEST :
                            if (response.body().getData().getErrorList() != null) {
                                failedResponse(response.body().getData().getErrorList());
                            } else {
                                showToast(response.body().getData().getMessage());
                            }
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<DeleteEmployeeResponse> call, Throwable t) {
                t.printStackTrace();
                showToast(getString(R.string.connection_error));
            }
        });
    }

    private void resetPassword(int employeeId) {
        progressDialog = new ProgressDialog(KaryawanDetailActivity.this);
        progressDialog.setMessage("Sedang Diproses...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ResetPasswordRequest request = new ResetPasswordRequest(Model.getUser().getUserId(), employeeId);

        APIService apiService = HTTPClient.getClient().create(APIService.class);
        Call<ResetPasswordResponse> call = apiService.resetPassword(request);
        call.enqueue(new Callback<ResetPasswordResponse>() {
            @Override
            public void onResponse(Call<ResetPasswordResponse> call, Response<ResetPasswordResponse> response) {
                if (response.isSuccessful()) {
                    switch (response.body().getStatusCode()) {
                        case Constants.STATUS_CODE_UPDATED :
                            showToast(getString(R.string.reset_password_success));
                            break;
                        case Constants.STATUS_CODE_BAD_REQUEST :
                            if (response.body().getData().getErrorList() != null) {
                                failedResponse(response.body().getData().getErrorList());
                            } else {
                                showToast(response.body().getData().getMessage());
                            }
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<ResetPasswordResponse> call, Throwable t) {
                t.printStackTrace();
                showToast(getString(R.string.connection_error));
            }
        });
    }

    private void failedResponse(ArrayList<DataResponse.Error> errorList) {
        StringBuilder stringBuilder = new StringBuilder();
        String prefix = "";
        for (DataResponse.Error error : errorList) {
            stringBuilder.append(prefix);
            stringBuilder.append(error.getMessage());
            prefix = ", ";
        }
        showToast(stringBuilder.toString());
    }

    private void showToast(String message) {
        progressDialog.dismiss();
        Toast.makeText(KaryawanDetailActivity.this, message, Toast.LENGTH_LONG).show();
    }
}
