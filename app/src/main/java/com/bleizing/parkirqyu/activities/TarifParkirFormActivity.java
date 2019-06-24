package com.bleizing.parkirqyu.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bleizing.parkirqyu.Constants;
import com.bleizing.parkirqyu.R;
import com.bleizing.parkirqyu.models.Model;
import com.bleizing.parkirqyu.models.TarifParkir;
import com.bleizing.parkirqyu.network.APIService;
import com.bleizing.parkirqyu.network.ChangeParkirRateRequest;
import com.bleizing.parkirqyu.network.ChangeTarifParkirResponse;
import com.bleizing.parkirqyu.network.DataResponse;
import com.bleizing.parkirqyu.network.HTTPClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TarifParkirFormActivity extends AppCompatActivity {

    private TarifParkir tarifParkir;

    private ProgressDialog progressDialog;

    private EditText editSatuJam;
    private EditText editTiapJam;
    private EditText editPerHari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarif_parkir_form);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tarifParkir = getIntent().getParcelableExtra("tarif_parkir");

        editSatuJam = (EditText) findViewById(R.id.edit_satu_jam);
        editTiapJam = (EditText) findViewById(R.id.edit_tiap_jam);
        editPerHari = (EditText) findViewById(R.id.edit_per_hari);

        TextView tvHeaderInput = (TextView) findViewById(R.id.tv_header_input);

        String headerInput = "Mobil";
        if (tarifParkir.getId() == 2) {
            headerInput = "Motor";
        }

        tvHeaderInput.setText(headerInput);

        editSatuJam.setText(String.valueOf(tarifParkir.getSatuJam()));
        editTiapJam.setText(String.valueOf(tarifParkir.getTiapJam()));
        editPerHari.setText(String.valueOf(tarifParkir.getPerHari()));

        Button btnSimpan = (Button) findViewById(R.id.btn_simpan);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = tarifParkir.getId();
                if (!editSatuJam.getText().toString().equals("") && !editTiapJam.getText().toString().equals("") && !editPerHari.getText().toString().equals("")) {
                    int satuJam = Integer.parseInt(editSatuJam.getText().toString());
                    int tiapJam = Integer.parseInt(editTiapJam.getText().toString());
                    int perHari = Integer.parseInt(editPerHari.getText().toString());

                    ubahTarifParkir(id, satuJam, tiapJam, perHari);
                } else {
                    showToast(getString(R.string.data_incompleted));
                }
            }
        });

        Button btnBatal = (Button) findViewById(R.id.btn_batal);
        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
        Intent intent = new Intent(TarifParkirFormActivity.this, TarifParkirActivity.class);
        startActivity(intent);
        finish();
    }

    private void ubahTarifParkir(int id, int satuJam, int tiapJam, int perHari) {
        progressDialog = new ProgressDialog(TarifParkirFormActivity.this);
        progressDialog.setMessage("Mohon Tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ChangeParkirRateRequest request = new ChangeParkirRateRequest(Model.getUser().getUserId(), id, satuJam, tiapJam, perHari);

        APIService apiService = HTTPClient.getClient().create(APIService.class);
        Call<ChangeTarifParkirResponse> call = apiService.changeParkirRate(request);
        call.enqueue(new Callback<ChangeTarifParkirResponse>() {
            @Override
            public void onResponse(Call<ChangeTarifParkirResponse> call, Response<ChangeTarifParkirResponse> response) {
                if (response.isSuccessful()) {
                    switch (response.body().getStatusCode()) {
                        case Constants.STATUS_CODE_UPDATED :
                            showToast("Tarif parkir berhasil diubah");

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
            public void onFailure(Call<ChangeTarifParkirResponse> call, Throwable t) {
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
        Toast.makeText(TarifParkirFormActivity.this, message, Toast.LENGTH_LONG).show();
    }
}
