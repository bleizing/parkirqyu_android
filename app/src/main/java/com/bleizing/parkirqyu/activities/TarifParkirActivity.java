package com.bleizing.parkirqyu.activities;

import android.app.ProgressDialog;
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
import com.bleizing.parkirqyu.models.Model;
import com.bleizing.parkirqyu.models.TarifParkir;
import com.bleizing.parkirqyu.network.APIService;
import com.bleizing.parkirqyu.network.GetParkirRateResponse;
import com.bleizing.parkirqyu.network.HTTPClient;

import java.lang.annotation.Target;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TarifParkirActivity extends AppCompatActivity {

    private TextView tvSatuJamMobil;
    private TextView tvTiapJamMobil;
    private TextView tvPerHariMobil;

    private TextView tvSatuJamMotor;
    private TextView tvTiapJamMotor;
    private TextView tvPerHariMotor;

    private ProgressDialog progressDialog;

    private ArrayList<TarifParkir> tarifParkirArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarif_parkir);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tarifParkirArrayList = new ArrayList<>();

        tvSatuJamMobil = (TextView) findViewById(R.id.tv_satu_jam_pertama_mobil);
        tvTiapJamMobil = (TextView) findViewById(R.id.tv_tiap_jam_mobil);
        tvPerHariMobil = (TextView) findViewById(R.id.tv_per_hari_mobil);

        tvSatuJamMotor = (TextView) findViewById(R.id.tv_satu_jam_pertama_motor);
        tvTiapJamMotor = (TextView) findViewById(R.id.tv_tiap_jam_motor);
        tvPerHariMotor = (TextView) findViewById(R.id.tv_per_hari_motor);

        Button btnUbahMobil = (Button) findViewById(R.id.btn_ubah_mobil);
        btnUbahMobil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ubahTarif(tarifParkirArrayList.get(0));
            }
        });

        Button btnUbahMotor = (Button) findViewById(R.id.btn_ubah_motor);
        btnUbahMotor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ubahTarif(tarifParkirArrayList.get(1));
            }
        });

        if (Model.getUser().getUserType() == 1) {
            btnUbahMobil.setVisibility(View.VISIBLE);
            btnUbahMotor.setVisibility(View.VISIBLE);
        }

        getTarifParkir();
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
        finish();
    }

    private void ubahTarif(TarifParkir tarifParkir) {
        Intent intent = new Intent(TarifParkirActivity.this, TarifParkirFormActivity.class);
        intent.putExtra("tarif_parkir", tarifParkir);
        startActivity(intent);
        finish();
    }

    private void getTarifParkir() {
        progressDialog = new ProgressDialog(TarifParkirActivity.this);
        progressDialog.setMessage("Mohon Tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        APIService apiService = HTTPClient.getClient().create(APIService.class);
        Call<GetParkirRateResponse> call = apiService.getParkirRate();
        call.enqueue(new Callback<GetParkirRateResponse>() {
            @Override
            public void onResponse(Call<GetParkirRateResponse> call, Response<GetParkirRateResponse> response) {
                if (response.isSuccessful() && response.body().getStatusCode() == Constants.STATUS_CODE_SUCCESS) {
                    responseSuccess(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<GetParkirRateResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(TarifParkirActivity.this, getString(R.string.connection_error), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                onBackPressed();
            }
        });
    }

    private void responseSuccess(ArrayList<GetParkirRateResponse.Data> dataArrayList) {
        if (dataArrayList.size() > 0) {
            if (tarifParkirArrayList != null && tarifParkirArrayList.size() > 0) {
                tarifParkirArrayList.clear();
            }

            for (GetParkirRateResponse.Data data : dataArrayList) {
                int id = data.getId();
                int satuJam = data.getSatuJam();
                int tiapJam = data.getTiapJam();
                int perHari = data.getPerHari();

                TarifParkir tarifParkir = new TarifParkir(id, satuJam, tiapJam, perHari);
                tarifParkirArrayList.add(tarifParkir);
            }
        }

        setView();

        progressDialog.dismiss();
    }

    private void setView() {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

        TarifParkir tarifParkirMobil = tarifParkirArrayList.get(0);

        tvSatuJamMobil.setText(formatRupiah.format(tarifParkirMobil.getSatuJam()));
        tvTiapJamMobil.setText(formatRupiah.format(tarifParkirMobil.getTiapJam()));
        tvPerHariMobil.setText(formatRupiah.format(tarifParkirMobil.getPerHari()));

        TarifParkir tarifParkirMotor = tarifParkirArrayList.get(1);

        tvSatuJamMotor.setText(formatRupiah.format(tarifParkirMotor.getSatuJam()));
        tvTiapJamMotor.setText(formatRupiah.format(tarifParkirMotor.getTiapJam()));
        tvPerHariMotor.setText(formatRupiah.format(tarifParkirMotor.getPerHari()));
    }
}
