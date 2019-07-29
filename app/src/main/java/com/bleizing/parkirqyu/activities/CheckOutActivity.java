package com.bleizing.parkirqyu.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bleizing.parkirqyu.Constants;
import com.bleizing.parkirqyu.R;
import com.bleizing.parkirqyu.models.Model;
import com.bleizing.parkirqyu.network.APIService;
import com.bleizing.parkirqyu.network.HTTPClient;
import com.bleizing.parkirqyu.network.ProcessCheckOutRequest;
import com.bleizing.parkirqyu.network.ProcessCheckOutResponse;
import com.bleizing.parkirqyu.network.ProcessPreCheckOutResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckOutActivity extends AppCompatActivity {
    private static final String TAG = "CheckOutActivity";

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        Intent intent = getIntent();
        final ProcessPreCheckOutResponse.Data data = intent.getParcelableExtra("data");

        TextView tvNomorRegistrasi = (TextView) findViewById(R.id.tv_nomor_registrasi);
        TextView tvJenisKendaraan = (TextView) findViewById(R.id.tv_jenis_kendaraan);
        TextView tvInvoiceCode = (TextView) findViewById(R.id.tv_invoice_code);
        TextView tvParkirStart = (TextView) findViewById(R.id.tv_parkir_start);
        TextView tvParkirEnd = (TextView) findViewById(R.id.tv_parkir_end);
        TextView tvDurasiParkir = (TextView) findViewById(R.id.tv_durasi_parkir);
        TextView tvNominal = (TextView) findViewById(R.id.tv_nominal);

        TextView tvCash = (TextView) findViewById(R.id.tv_cash);

        Button btnBayar = (Button) findViewById(R.id.btn_bayar);
        btnBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processCheckOut(data.getInvoiceId(), data.getSaldoEnough());
            }
        });

        if (data.getSaldoEnough() == 0) {
            btnBayar.setText("Bayar Manual");
            tvCash.setVisibility(View.VISIBLE);
        }

        tvNomorRegistrasi.setText(data.getNomorRegistrasi());
        tvJenisKendaraan.setText(data.getVehicleType());
        tvInvoiceCode.setText(data.getInvoiceCode());
        tvParkirStart.setText(data.getParkirStart());
        tvParkirEnd.setText(data.getParkirEnd());
        tvDurasiParkir.setText(data.getDurasiParkir());
        tvNominal.setText(data.getNominal());
    }

    private void processCheckOut(int invoiceId, int paymentType) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mohon Tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ProcessCheckOutRequest request = new ProcessCheckOutRequest(Model.getUser().getUserId(), invoiceId, paymentType);
        APIService apiService = HTTPClient.getClient().create(APIService.class);
        Call<ProcessCheckOutResponse> call = apiService.processCheckOut(request);
        call.enqueue(new Callback<ProcessCheckOutResponse>() {
            @Override
            public void onResponse(Call<ProcessCheckOutResponse> call, Response<ProcessCheckOutResponse> response) {
                if (response.isSuccessful()) {
                    switch (response.body().getStatusCode()) {
                        case Constants.STATUS_CODE_UPDATED :
                            processCheckOutResponse(true);
                            break;
                        case Constants.STATUS_CODE_BAD_REQUEST :
                            processCheckOutResponse(false);
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<ProcessCheckOutResponse> call, Throwable t) {
                Toast.makeText(CheckOutActivity.this, getString(R.string.connection_error), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }

    private void processCheckOutResponse(final boolean isSuccess) {
        final View view = LayoutInflater.from(CheckOutActivity.this).inflate(R.layout.dialog_checkout_response, null);
        final AlertDialog dialog = new AlertDialog.Builder(CheckOutActivity.this).create();
        dialog.setView(view);
        dialog.setCancelable(false);

        String message = "";

        if (isSuccess) {
            message = "Pembayaran berhasil. Terima kasih.";
        } else {
            message = "Mohon maaf, pembayaran gagal.";
        }

        TextView tvCheckinResponse = (TextView) view.findViewById(R.id.tv_checkout_response);
        tvCheckinResponse.setText(message);

        Button btnOk = (Button) view.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (isSuccess) {
                    Intent intent = new Intent(CheckOutActivity.this, ParkirScannerActivity.class);
                    intent.putExtra("type", 2);
                    startActivity(intent);
                    finish();
                }
            }
        });

        progressDialog.dismiss();

        dialog.show();
    }
}
