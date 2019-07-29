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
import com.bleizing.parkirqyu.models.Kendaraan;
import com.bleizing.parkirqyu.models.Model;
import com.bleizing.parkirqyu.network.APIService;
import com.bleizing.parkirqyu.network.DataResponse;
import com.bleizing.parkirqyu.network.DeleteVehicleRequest;
import com.bleizing.parkirqyu.network.DeleteVehicleResponse;
import com.bleizing.parkirqyu.network.HTTPClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KendaraanDetailActivity extends AppCompatActivity {

    private TextView tvNomorRegistrasi;
    private TextView tvNamaPemilik;
    private TextView tvAlamat;
    private TextView tvMerk;
    private TextView tvType;
    private TextView tvTahunPembuatan;
    private TextView tvNomorRangka;
    private TextView tvNomorMesin;
    private TextView tvJenisKendaraan;

    private Karyawan karyawan;
    private Kendaraan kendaraan;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kendaraan_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        karyawan = null;
        kendaraan = null;

        Intent intent = getIntent();
        if (intent.getParcelableExtra("karyawan") != null) {
            karyawan = intent.getParcelableExtra("karyawan");
        }

        if (intent.getParcelableExtra("kendaraan") != null) {
            kendaraan = intent.getParcelableExtra("kendaraan");
        }

        tvNomorRegistrasi = (TextView) findViewById(R.id.tv_nomor_registrasi);
        tvNamaPemilik = (TextView) findViewById(R.id.tv_nama_pemilik);
        tvAlamat = (TextView) findViewById(R.id.tv_alamat);
        tvMerk = (TextView) findViewById(R.id.tv_merk);
        tvType = (TextView) findViewById(R.id.tv_type);
        tvTahunPembuatan = (TextView) findViewById(R.id.tv_tahun_pembuatan);
        tvNomorRangka = (TextView) findViewById(R.id.tv_nomor_rangka);
        tvNomorMesin = (TextView) findViewById(R.id.tv_nomor_mesin);
        tvJenisKendaraan = (TextView) findViewById(R.id.tv_vehicle_type);

        Button btnUbah = (Button) findViewById(R.id.btn_ubah);
        btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KendaraanDetailActivity.this, KendaraanFormActivity.class);
                intent.putExtra("karyawan", karyawan);
                intent.putExtra("kendaraan", kendaraan);
                startActivity(intent);
                finish();
            }
        });

        Button btnHapus = (Button) findViewById(R.id.btn_hapus);
        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(KendaraanDetailActivity.this)
                        .setTitle(getString(R.string.konfirmasi))
                        .setMessage(getString(R.string.konfirmasi_hapus))
                        .setPositiveButton(getString(R.string.konfirmasi_yakin), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                hapusKendaraan(kendaraan.getKendaraanId());
                            }
                        })
                        .setNegativeButton(getString(R.string.konfirmasi_batal), null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setCancelable(false)
                        .show();
            }
        });

        if (karyawan != null) {
            btnUbah.setVisibility(View.VISIBLE);
            btnHapus.setVisibility(View.VISIBLE);
        }

        setData();
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
        Intent intent = new Intent(KendaraanDetailActivity.this, MainActivity.class);
        if (karyawan != null) {
            intent = new Intent(KendaraanDetailActivity.this, KendaraanActivity.class);
            intent.putExtra("karyawan", karyawan);
        }
        startActivity(intent);
        finish();
    }

    private void setData() {
        tvNomorRegistrasi.setText(kendaraan.getNomorRegistrasi());
        tvNamaPemilik.setText(kendaraan.getNama());
        tvAlamat.setText(kendaraan.getAlamat());
        tvMerk.setText(kendaraan.getMerk());
        tvType.setText(kendaraan.getType());
        tvTahunPembuatan.setText(kendaraan.getTahunPembuatan());
        tvNomorRangka.setText(kendaraan.getNomorRangka());
        tvNomorMesin.setText(kendaraan.getNomorMesin());
        tvJenisKendaraan.setText(kendaraan.getVehicleType());
    }

    public void hapusKendaraan(int kendaraanId) {
        progressDialog = new ProgressDialog(KendaraanDetailActivity.this);
        progressDialog.setMessage("Sedang Diproses...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        DeleteVehicleRequest request = new DeleteVehicleRequest(Model.getUser().getUserId(), kendaraanId);

        APIService apiService = HTTPClient.getClient().create(APIService.class);
        Call<DeleteVehicleResponse> call = apiService.deleteVehicle(request);
        call.enqueue(new Callback<DeleteVehicleResponse>() {
            @Override
            public void onResponse(Call<DeleteVehicleResponse> call, Response<DeleteVehicleResponse> response) {
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
            public void onFailure(Call<DeleteVehicleResponse> call, Throwable t) {
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
        Toast.makeText(KendaraanDetailActivity.this, message, Toast.LENGTH_LONG).show();
    }
}
