package com.bleizing.parkirqyu.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bleizing.parkirqyu.Constants;
import com.bleizing.parkirqyu.R;
import com.bleizing.parkirqyu.models.Karyawan;
import com.bleizing.parkirqyu.models.Kendaraan;
import com.bleizing.parkirqyu.models.Model;
import com.bleizing.parkirqyu.network.APIService;
import com.bleizing.parkirqyu.network.AddVehicleRequest;
import com.bleizing.parkirqyu.network.AddVehicleResponse;
import com.bleizing.parkirqyu.network.DataResponse;
import com.bleizing.parkirqyu.network.EditVehicleRequest;
import com.bleizing.parkirqyu.network.EditVehicleResponse;
import com.bleizing.parkirqyu.network.HTTPClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KendaraanFormActivity extends AppCompatActivity {
    private static final String TAG = "KaryawanFormActivity";

    private ProgressDialog progressDialog;

    private Kendaraan kendaraan;
    private Karyawan karyawan;

    private LinearLayout llNomorRegistrasi;

    private TextInputLayout tilNomorRegistrasi;

    private EditText editNomorRegistrasi;
    private EditText editNamaPemilik;
    private EditText editAlamat;
    private EditText editMerk;
    private EditText editType;
    private EditText editTahunPembuatan;
    private EditText editNomorRangka;
    private EditText editNomorMesin;

    private RadioGroup rgJenisKendaraan;

    private RadioButton rbJenisKendaraan;

    private int jenisKendaraan;

    private String nomorRegistrasi;
    private String namaPemilik;
    private String alamat;
    private String merk;
    private String type;
    private String tahunPembuatan;
    private String nomorRangka;
    private String nomorMesin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kendaraan_form);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        llNomorRegistrasi = (LinearLayout) findViewById(R.id.ll_nomor_registrasi);

        tilNomorRegistrasi = (TextInputLayout) findViewById(R.id.til_nomor_registrasi);

        editNomorRegistrasi = (EditText) findViewById(R.id.edit_nomor_registrasi);
        editNamaPemilik = (EditText) findViewById(R.id.edit_nama_pemilik);
        editAlamat = (EditText) findViewById(R.id.edit_alamat);
        editMerk = (EditText) findViewById(R.id.edit_merk);
        editType = (EditText) findViewById(R.id.edit_type);
        editTahunPembuatan = (EditText) findViewById(R.id.edit_tahun_pembuatan);
        editNomorRangka = (EditText) findViewById(R.id.edit_nomor_rangka);
        editNomorMesin = (EditText) findViewById(R.id.edit_nomor_mesin);

        rgJenisKendaraan = (RadioGroup) findViewById(R.id.rg_jenis_kendaraan);

        nomorRegistrasi = "nomor_registrasi";

        Button btnSimpan = (Button) findViewById(R.id.btn_simpan);
        Button btnBatal = (Button) findViewById(R.id.btn_batal);

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tilNomorRegistrasi.getVisibility() == View.VISIBLE) {
                    nomorRegistrasi = editNomorRegistrasi.getText().toString();
                }
                namaPemilik = editNamaPemilik.getText().toString();
                alamat = editAlamat.getText().toString();
                merk = editMerk.getText().toString();
                type = editType.getText().toString();
                tahunPembuatan = editTahunPembuatan.getText().toString();
                nomorRangka = editNomorRangka.getText().toString();
                nomorMesin = editNomorMesin.getText().toString();

                int selectedJenisKendaraan = rgJenisKendaraan.getCheckedRadioButtonId();
                rbJenisKendaraan = (RadioButton) findViewById(selectedJenisKendaraan);

                jenisKendaraan = 1;

                if (rbJenisKendaraan.getText().equals("Motor")) {
                    jenisKendaraan = 2;
                }

                if (!nomorRegistrasi.equals("") || !namaPemilik.equals("") || !alamat.equals("") || !merk.equals("") || !type.equals("") || !tahunPembuatan.equals("") || !nomorRangka.equals("") || !nomorMesin.equals("")) {
                    if (kendaraan != null) {
                        prosesUbah();
                    } else {
                        prosesTambah();
                    }
                } else {
                    Toast.makeText(KendaraanFormActivity.this, getString(R.string.data_incompleted), Toast.LENGTH_LONG).show();
                }
            }
        });

        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        kendaraan = null;
        karyawan = null;

        Intent intent = getIntent();
        if (intent.getParcelableExtra("kendaraan") != null) {
            kendaraan = intent.getParcelableExtra("kendaraan");
        }

        if (intent.getParcelableExtra("karyawan") != null) {
            karyawan = intent.getParcelableExtra("karyawan");
        }

        TextView tvNomorRegistrasi = (TextView) findViewById(R.id.tv_nomor_registrasi);

        TextView tvHeaderInput = (TextView) findViewById(R.id.tv_header_input);

        if (kendaraan != null) {
            tvHeaderInput.setText(getString(R.string.ubah));
            tvNomorRegistrasi.setText(kendaraan.getNomorRegistrasi());
            llNomorRegistrasi.setVisibility(View.VISIBLE);

            setData();
        } else {
            tvHeaderInput.setText(getString(R.string.tambah));
            tilNomorRegistrasi.setVisibility(View.VISIBLE);
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
        Intent intent = new Intent(KendaraanFormActivity.this, KendaraanActivity.class);
        intent.putExtra("karyawan", karyawan);
        startActivity(intent);
        finish();
    }

    private void setData() {
        editNamaPemilik.setText(kendaraan.getNama());
        editAlamat.setText(kendaraan.getAlamat());
        editMerk.setText(kendaraan.getMerk());
        editType.setText(kendaraan.getType());
        editTahunPembuatan.setText(kendaraan.getTahunPembuatan());
        editNomorRangka.setText(kendaraan.getNomorRangka());
        editNomorMesin.setText(kendaraan.getNomorMesin());

        rgJenisKendaraan.check(R.id.rb_mobil);

        if (kendaraan.getVehicleType().equals("Motor")) {
            rgJenisKendaraan.check(R.id.rb_motor);
        }
    }

    private void prosesUbah() {
        progressDialog = new ProgressDialog(KendaraanFormActivity.this);
        progressDialog.setMessage("Sedang Diproses...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        EditVehicleRequest request = new EditVehicleRequest(Model.getUser().getUserId(), kendaraan.getKendaraanId(), namaPemilik, alamat, merk, type, tahunPembuatan, nomorRangka, nomorMesin, String.valueOf(jenisKendaraan));

        APIService apiService = HTTPClient.getClient().create(APIService.class);
        Call<EditVehicleResponse> call = apiService.editVehicle(request);
        call.enqueue(new Callback<EditVehicleResponse>() {
            @Override
            public void onResponse(Call<EditVehicleResponse> call, Response<EditVehicleResponse> response) {
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
            public void onFailure(Call<EditVehicleResponse> call, Throwable t) {
                t.printStackTrace();
                showToast(getString(R.string.connection_error));
            }
        });
    }

    private void prosesTambah() {
        progressDialog = new ProgressDialog(KendaraanFormActivity.this);
        progressDialog.setMessage("Sedang Diproses...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        AddVehicleRequest request = new AddVehicleRequest(Model.getUser().getUserId(), karyawan.getUserId(), nomorRegistrasi, namaPemilik, alamat, merk, type, tahunPembuatan, nomorRangka, nomorMesin, String.valueOf(jenisKendaraan));

        APIService apiService = HTTPClient.getClient().create(APIService.class);
        Call<AddVehicleResponse> call = apiService.addVehicle(request);
        call.enqueue(new Callback<AddVehicleResponse>() {
            @Override
            public void onResponse(Call<AddVehicleResponse> call, Response<AddVehicleResponse> response) {
                if (response.isSuccessful()) {
                    switch (response.body().getStatusCode()) {
                        case Constants.STATUS_CODE_CREATED :
                            showToast(getString(R.string.add_data_success));
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
            public void onFailure(Call<AddVehicleResponse> call, Throwable t) {
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
        Toast.makeText(KendaraanFormActivity.this, message, Toast.LENGTH_LONG).show();
    }
}
