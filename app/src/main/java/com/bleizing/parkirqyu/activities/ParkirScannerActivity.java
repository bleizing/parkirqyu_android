package com.bleizing.parkirqyu.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bleizing.parkirqyu.Constants;
import com.bleizing.parkirqyu.R;
import com.bleizing.parkirqyu.models.Model;
import com.bleizing.parkirqyu.network.APIService;
import com.bleizing.parkirqyu.network.HTTPClient;
import com.bleizing.parkirqyu.network.ProcessCheckInRequest;
import com.bleizing.parkirqyu.network.ProcessCheckInResponse;
import com.bleizing.parkirqyu.network.ProcessPreCheckOutRequest;
import com.bleizing.parkirqyu.network.ProcessPreCheckOutResponse;
import com.google.zxing.Result;

import me.dm7.barcodescanner.core.IViewFinder;
import me.dm7.barcodescanner.core.ViewFinderView;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParkirScannerActivity extends AppCompatActivity {
    private static final String TAG = "ParkirScannerActivity";

    private ZXingScannerView scannerView;

    private int type;

    private int vehicleType;

    private ProgressDialog progressDialog;

    private AlertDialog dialogManual;

    private EditText editNomorRegistrasi;

    private RadioGroup rgJenisKendaraan;

    private View viewDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parkir_scanner);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);

        initView();
    }

    private ZXingScannerView.ResultHandler resultHandler = new ZXingScannerView.ResultHandler() {
        @Override
        public void handleResult(Result result) {
            vehicleType = 0;
            if (type == 1) {
                prosesCheckin(result.getText(), true);
            } else {
                prosesPreCheckout(result.getText(), true);
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (hasPermission()) {
            if (scannerView == null) {
                initView();
            }
            scannerView.startCamera(1);
        } else {
            requestPermission();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.PERMISSION_CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    scannerView.startCamera(1);
                } else if (Build.VERSION.SDK_INT >= 23 && !shouldShowRequestPermissionRationale(permissions[0])) {
                    // User selected the Never Ask Again Option Change settings in app settings manually
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setTitle("Konfirmasi");
                    alertDialogBuilder
                            .setMessage("Mohon izinkan kami dari setting untuk menampilkan scanner. Terima kasih")
                            .setCancelable(false)
                            .setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    intent.setData(uri);
                                    startActivityForResult(intent, 1000);     // Comment 3.
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    alertDialog.setCancelable(false);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !hasPermission()) {

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                        alertDialogBuilder.setTitle("Konfirmasi");
                        alertDialogBuilder
                                .setMessage("Untuk menampilkan scanner, anda harus mengizinkan kami. Izinkan?")
                                .setCancelable(false)
                                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        requestPermission();
                                    }
                                })
                                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finish();
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        alertDialog.setCancelable(false);
                    }
                }
            }
        }
    }

    private boolean hasPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] permission = {Manifest.permission.CAMERA};
            if (ActivityCompat.checkSelfPermission(ParkirScannerActivity.this, permission[0]) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, Constants.PERMISSION_CAMERA_REQUEST_CODE);
    }

    private void initView() {
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.fl_scanner);

        scannerView = new ZXingScannerView(this) {
            @Override
            protected IViewFinder createViewFinderView(Context context) {
                return new ViewFinderView(context);
            }
        };
        scannerView.setAutoFocus(true);
        scannerView.setResultHandler(resultHandler);

        frameLayout.addView(scannerView);

        dialogManual = new AlertDialog.Builder(ParkirScannerActivity.this).create();

        viewDialog = LayoutInflater.from(ParkirScannerActivity.this).inflate(R.layout.dialog_parkir_manual, null);

        final Button btnManual = (Button) findViewById(R.id.btn_manual);

        editNomorRegistrasi = (EditText) viewDialog.findViewById(R.id.edit_nomor_registrasi);

        rgJenisKendaraan = (RadioGroup) viewDialog.findViewById(R.id.rg_jenis_kendaraan);

        if (type == 1) {
            btnManual.setText("Manual Check In");
        } else if (type == 2) {
            btnManual.setText("Manual Check Out");
        }

        btnManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editNomorRegistrasi.setText("");
                rgJenisKendaraan.check(R.id.rb_mobil);

                dialogManual.setView(viewDialog);
                dialogManual.setCancelable(false);

                LinearLayout llJenisKendaraan = (LinearLayout) viewDialog.findViewById(R.id.ll_jenis_kendaraan);

                if (type == 1) {
                    llJenisKendaraan.setVisibility(View.VISIBLE);
                }

                Button btnProses = (Button) viewDialog.findViewById(R.id.btn_proses);
                btnProses.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String nomorRegistasi = editNomorRegistrasi.getText().toString();

                        if (nomorRegistasi.equals("")) {
                            Toast.makeText(ParkirScannerActivity.this, getString(R.string.data_incompleted), Toast.LENGTH_LONG).show();
                            return;
                        }

                        if (type == 1) {
                            int selectedJenisKendaraan = rgJenisKendaraan.getCheckedRadioButtonId();

                            RadioButton rbJenisKendaraan = (RadioButton) viewDialog.findViewById(selectedJenisKendaraan);

                            vehicleType = 1;

                            if (rbJenisKendaraan.getText().equals("Motor")) {
                                vehicleType = 2;
                            }

                            prosesCheckin(nomorRegistasi, false);
                        } else {
                            prosesPreCheckout(nomorRegistasi, false);
                        }

                        hideKeyboard(viewDialog);
                    }
                });

                Button btnBatal = (Button) viewDialog.findViewById(R.id.btn_batal);
                btnBatal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogManual.dismiss();

                        hideKeyboard(viewDialog);
                    }
                });

                dialogManual.show();
            }
        });
    }

    private void prosesCheckin(String nomorRegistrasi, final boolean fromScanner) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mohon Tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ProcessCheckInRequest request = new ProcessCheckInRequest(Model.getUser().getUserId(), nomorRegistrasi, vehicleType);
        APIService apiService = HTTPClient.getClient().create(APIService.class);
        Call<ProcessCheckInResponse> call = apiService.processCheckIn(request);
        call.enqueue(new Callback<ProcessCheckInResponse>() {
            @Override
            public void onResponse(Call<ProcessCheckInResponse> call, Response<ProcessCheckInResponse> response) {
                if (response.isSuccessful()) {
                    switch (response.body().getStatusCode()) {
                        case Constants.STATUS_CODE_CREATED :
                            processCheckinResponse("Check in berhasil. Silahkan masuk.", true, fromScanner);
                            break;
                        case Constants.STATUS_CODE_BAD_REQUEST :
                            processCheckinResponse("Mohon maaf, check in gagal. Harap hubungi petugas.", false, fromScanner);
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<ProcessCheckInResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(ParkirScannerActivity.this, getString(R.string.connection_error), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }

    private void processCheckinResponse(String message, final boolean isSuccess, final boolean fromScanner) {
        final View view = LayoutInflater.from(ParkirScannerActivity.this).inflate(R.layout.dialog_checkin_response, null);
        final AlertDialog dialog = new AlertDialog.Builder(ParkirScannerActivity.this).create();
        dialog.setView(view);
        dialog.setCancelable(false);

        TextView tvCheckinResponse = (TextView) view.findViewById(R.id.tv_checkin_response);
        tvCheckinResponse.setText(message);

        Button btnOk = (Button) view.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (isSuccess) {
                    if (!fromScanner) {
                        dialogManual.dismiss();
                    }
                    editNomorRegistrasi.setText("");
                    rgJenisKendaraan.check(R.id.rb_mobil);
                    scannerView.resumeCameraPreview(resultHandler);
                } else {
                    if (fromScanner) {
                        scannerView.resumeCameraPreview(resultHandler);
                    }
                }
            }
        });

        progressDialog.dismiss();

        dialog.show();
    }

    private void prosesPreCheckout(String nomorRegistrasi, final boolean fromScanner) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mohon Tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ProcessPreCheckOutRequest request = new ProcessPreCheckOutRequest(Model.getUser().getUserId(), nomorRegistrasi);
        APIService apiService = HTTPClient.getClient().create(APIService.class);
        Call<ProcessPreCheckOutResponse> call = apiService.processPreCheckOut(request);
        call.enqueue(new Callback<ProcessPreCheckOutResponse>() {
            @Override
            public void onResponse(Call<ProcessPreCheckOutResponse> call, Response<ProcessPreCheckOutResponse> response) {
                if (response.isSuccessful()) {
                    switch (response.body().getStatusCode()) {
                        case Constants.STATUS_CODE_SUCCESS :
                            Intent intent = new Intent(ParkirScannerActivity.this, CheckOutActivity.class);
                            intent.putExtra("data", response.body().getData());
                            startActivity(intent);
                            finish();
                            break;
                        case Constants.STATUS_CODE_BAD_REQUEST :
                            processPreCheckoutFailed(fromScanner);
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<ProcessPreCheckOutResponse> call, Throwable t) {
                Toast.makeText(ParkirScannerActivity.this, getString(R.string.connection_error), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }

    private void processPreCheckoutFailed(final boolean fromScanner) {
        final View view = LayoutInflater.from(ParkirScannerActivity.this).inflate(R.layout.dialog_precheckout_failed, null);
        final AlertDialog dialog = new AlertDialog.Builder(ParkirScannerActivity.this).create();
        dialog.setView(view);
        dialog.setCancelable(false);

        Button btnOk = (Button) view.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (fromScanner) {
                    editNomorRegistrasi.setText("");
                    scannerView.resumeCameraPreview(resultHandler);
                }
            }
        });

        progressDialog.dismiss();

        dialog.show();
    }

    private void hideKeyboard(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch(Exception ignored) {
        }
    }
}
