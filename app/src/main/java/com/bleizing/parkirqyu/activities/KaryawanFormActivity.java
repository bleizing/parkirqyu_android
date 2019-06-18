package com.bleizing.parkirqyu.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bleizing.parkirqyu.Constants;
import com.bleizing.parkirqyu.R;
import com.bleizing.parkirqyu.models.Karyawan;
import com.bleizing.parkirqyu.models.Model;
import com.bleizing.parkirqyu.network.APIService;
import com.bleizing.parkirqyu.network.AddEmployeeRequest;
import com.bleizing.parkirqyu.network.AddEmployeeResponse;
import com.bleizing.parkirqyu.network.BaseResponse;
import com.bleizing.parkirqyu.network.DataResponse;
import com.bleizing.parkirqyu.network.EditEmployeeRequest;
import com.bleizing.parkirqyu.network.EditEmployeeResponse;
import com.bleizing.parkirqyu.network.GetEmployeeByUserIdRequest;
import com.bleizing.parkirqyu.network.GetEmployeeByUserIdResponse;
import com.bleizing.parkirqyu.network.HTTPClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KaryawanFormActivity extends AppCompatActivity {
    private static final String TAG = "KaryawanFormActivity";

    private ProgressDialog progressDialog;

    private Karyawan karyawan;

    private EditText editNama;
    private EditText editEmail;
    private EditText editAlamat;
    private EditText editTempatLahir;
    private EditText editTanggalLahir;

    private RadioGroup rgJenisKelamin;
    private RadioGroup rgSebagai;

    private RadioButton rbJenisKelamin;
    private RadioButton rbSebagai;

    private String nama;
    private String email;
    private String alamat;
    private String tempatLahir;
    private String tanggalLahir;

    private int jenisKelamin;
    private int sebagai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karyawan_form);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editNama = (EditText) findViewById(R.id.edit_nama);
        editEmail = (EditText) findViewById(R.id.edit_email);
        editAlamat = (EditText) findViewById(R.id.edit_alamat);
        editTempatLahir = (EditText) findViewById(R.id.edit_tempat_lahir);
        editTanggalLahir = (EditText) findViewById(R.id.edit_tanggal_lahir);

        rgJenisKelamin = (RadioGroup) findViewById(R.id.rg_jenis_kelamin);
        rgSebagai = (RadioGroup) findViewById(R.id.rg_sebagai);

        Button btnSimpan = (Button) findViewById(R.id.btn_simpan);
        Button btnBatal = (Button) findViewById(R.id.btn_batal);

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nama = editNama.getText().toString();
                email = editEmail.getText().toString();
                alamat = editAlamat.getText().toString();
                tempatLahir = editTempatLahir.getText().toString();
                tanggalLahir = editTanggalLahir.getText().toString();

                String[] tanggalLahirArr = tanggalLahir.split("/");
                tanggalLahir = tanggalLahirArr[2] + "-" + tanggalLahirArr[1] + "-" + tanggalLahirArr[0];

                int selectedJenisKelamin = rgJenisKelamin.getCheckedRadioButtonId();
                rbJenisKelamin = (RadioButton) findViewById(selectedJenisKelamin);

                jenisKelamin = 2;

                if (rbJenisKelamin.getText().equals("Laki-Laki")) {
                    jenisKelamin = 1;
                }

                int selectedSebagai = rgSebagai.getCheckedRadioButtonId();
                rbSebagai = (RadioButton) findViewById(selectedSebagai);

                sebagai = 3;

                if (rbSebagai.getText().equals("Admin")) {
                    sebagai = 1;
                } else if (rbSebagai.getText().equals("Petugas Parkir")) {
                    sebagai = 2;
                }

                if (!nama.equals("") || !email.equals("") || !alamat.equals("") || !tempatLahir.equals("") || !tanggalLahir.equals("")) {
                    if (karyawan != null) {
                        prosesUbah();
                    } else {
                        prosesTambah();
                    }
                } else {
                    Toast.makeText(KaryawanFormActivity.this, getString(R.string.data_incompleted), Toast.LENGTH_LONG).show();
                }
            }
        });

        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        karyawan = null;

        TextView tvHeaderInput = (TextView) findViewById(R.id.tv_header_input);

        Intent intent = getIntent();
        if (intent.getParcelableExtra("karyawan") != null) {
            karyawan = intent.getParcelableExtra("karyawan");
        }

        if (karyawan != null) {
            tvHeaderInput.setText(getString(R.string.ubah));
            getKaryawanDetail();
        } else {
            tvHeaderInput.setText(getString(R.string.tambah));
        }
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
        Intent intent = new Intent(KaryawanFormActivity.this, KaryawanActivity.class);
        startActivity(intent);
        finish();
    }

    private void getKaryawanDetail() {
        progressDialog = new ProgressDialog(KaryawanFormActivity.this);
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
                            Toast.makeText(KaryawanFormActivity.this, getString(R.string.data_empty), Toast.LENGTH_LONG).show();
                            onBackPressed();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<GetEmployeeByUserIdResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(KaryawanFormActivity.this, getString(R.string.connection_error), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                onBackPressed();
            }
        });
    }

    private void setData() {
        editNama.setText(karyawan.getNama());
        editEmail.setText(karyawan.getEmail());
        editAlamat.setText(karyawan.getAlamat());
        editTempatLahir.setText(karyawan.getTempatLahir());
        editTanggalLahir.setText(karyawan.getTanggalLahir());

        rgJenisKelamin.check(R.id.rb_laki);

        if (karyawan.getJenisKelamin().equals("Perempuan")) {
            rgJenisKelamin.check(R.id.rb_perempuan);
        }

        rgSebagai.check(R.id.rb_admin);

        if (karyawan.getUserType().equals("Petugas Parkir")) {
            rgSebagai.check(R.id.rb_petugas);
        } else if (karyawan.getUserType().equals("Karyawan")) {
            rgSebagai.check(R.id.rb_karyawan);
        }
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

    private void prosesUbah() {
        progressDialog = new ProgressDialog(KaryawanFormActivity.this);
        progressDialog.setMessage("Sedang Diproses...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        EditEmployeeRequest request = new EditEmployeeRequest(Model.getUser().getUserId(), karyawan.getUserId(), nama, email, String.valueOf(jenisKelamin), alamat, tempatLahir, tanggalLahir, String.valueOf(sebagai));

        APIService apiService = HTTPClient.getClient().create(APIService.class);
        Call<EditEmployeeResponse> call = apiService.editEmployee(request);
        call.enqueue(new Callback<EditEmployeeResponse>() {
            @Override
            public void onResponse(Call<EditEmployeeResponse> call, Response<EditEmployeeResponse> response) {
                if (response.isSuccessful()) {
                    switch (response.body().getStatusCode()) {
                        case Constants.STATUS_CODE_UPDATED :
                            showToast(getString(R.string.edit_data_success));
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
            public void onFailure(Call<EditEmployeeResponse> call, Throwable t) {
                t.printStackTrace();
                showToast(getString(R.string.connection_error));
            }
        });
    }

    private void prosesTambah() {
        progressDialog = new ProgressDialog(KaryawanFormActivity.this);
        progressDialog.setMessage("Sedang Diproses...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        AddEmployeeRequest request = new AddEmployeeRequest(Model.getUser().getUserId(), nama, email, String.valueOf(jenisKelamin), alamat, tempatLahir, tanggalLahir, String.valueOf(sebagai));

        APIService apiService = HTTPClient.getClient().create(APIService.class);
        Call<AddEmployeeResponse> call = apiService.addEmployee(request);
        call.enqueue(new Callback<AddEmployeeResponse>() {
            @Override
            public void onResponse(Call<AddEmployeeResponse> call, Response<AddEmployeeResponse> response) {
                if (response.isSuccessful()) {
                    switch (response.body().getStatusCode()) {
                        case Constants.STATUS_CODE_CREATED :
                            showToast(getString(R.string.add_data_success));
                            finish();
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
            public void onFailure(Call<AddEmployeeResponse> call, Throwable t) {
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
        Toast.makeText(KaryawanFormActivity.this, message, Toast.LENGTH_LONG).show();
    }
}
