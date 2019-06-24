package com.bleizing.parkirqyu.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bleizing.parkirqyu.Constants;
import com.bleizing.parkirqyu.R;
import com.bleizing.parkirqyu.RecyclerTouchListener;
import com.bleizing.parkirqyu.RecyclerViewClickListener;
import com.bleizing.parkirqyu.adapters.KendaraanAdapter;
import com.bleizing.parkirqyu.adapters.KendaraanParkirAdapter;
import com.bleizing.parkirqyu.models.Kendaraan;
import com.bleizing.parkirqyu.models.KendaraanPakir;
import com.bleizing.parkirqyu.models.Model;
import com.bleizing.parkirqyu.models.User;
import com.bleizing.parkirqyu.network.APIService;
import com.bleizing.parkirqyu.network.BaseRequest;
import com.bleizing.parkirqyu.network.GetUserVehicleResponse;
import com.bleizing.parkirqyu.network.GetVehicleInParkirResponse;
import com.bleizing.parkirqyu.network.HTTPClient;
import com.bleizing.parkirqyu.network.LoginResponse;
import com.bleizing.parkirqyu.utils.PrefUtils;
import com.bleizing.parkirqyu.utils.SwipeRefreshUtils;
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.SdkCoreFlowBuilder;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.core.UIKitCustomSetting;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.BankType;
import com.midtrans.sdk.corekit.models.BillInfoModel;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.UserAddress;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bleizing.parkirqyu.Constants.BASE_URL;
import static com.midtrans.sdk.corekit.core.Constants.ADDRESS_TYPE_BILLING;
import static com.midtrans.sdk.corekit.core.Constants.ADDRESS_TYPE_BOTH;
import static com.midtrans.sdk.corekit.core.Constants.ADDRESS_TYPE_SHIPPING;

public class MainActivity extends AppCompatActivity implements SwipeRefreshUtils.SwipeRefreshUtilsListener {
    private static final String TAG = "MainActivity";

    private SwipeRefreshLayout swipeRefreshLayout;

    private ArrayList<Kendaraan> kendaraanArrayList;
    private ArrayList<KendaraanPakir> kendaraanParkirArrayList;

    private KendaraanAdapter kendaraanAdapter;
    private KendaraanParkirAdapter kendaraanParkirAdapter;

    private LinearLayout llKendaraanParkir;
    private LinearLayout llKendaraan;

    private TextView tvNama;
    private TextView tvSaldo;

    private PrefUtils prefUtils;

    private boolean userLoading = false;
    private boolean kendaraanLoading = false;
    private boolean kendaraanParkirLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefUtils = new PrefUtils(this);

        tvNama = (TextView) findViewById(R.id.tv_nama);
        tvSaldo = (TextView) findViewById(R.id.tv_saldo);

        initUser();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(refreshListener);
        refreshListener.onRefresh();

        LinearLayout llTarifParkir = (LinearLayout) findViewById(R.id.ll_tarif_parkir);
        llTarifParkir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TarifParkirActivity.class);
                startActivity(intent);
            }
        });

        TextView tvLihatDetail = (TextView) findViewById(R.id.tv_lihat_detail);
        tvLihatDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserDetailActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout llKaryawan = (LinearLayout) findViewById(R.id.ll_karyawan);
        llKendaraanParkir = (LinearLayout) findViewById(R.id.ll_kendaraan_parkir);
        llKendaraan = (LinearLayout) findViewById(R.id.ll_kendaraan);
        Button btnBertugas = (Button) findViewById(R.id.btn_bertugas);
        Button btnCheckin = (Button) findViewById(R.id.btn_checkin);

        TextView tvTopup = (TextView) findViewById(R.id.tv_topup);
        tvTopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TopupActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnCheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ParkirScannerActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
            }
        });

        btnBertugas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ParkirScannerActivity.class);
                intent.putExtra("type", 2);
                startActivity(intent);
            }
        });

        if (Model.getUser().getUserType() == 1) {
            btnCheckin.setVisibility(View.VISIBLE);
        }

        TextView tvLogout = (TextView) findViewById(R.id.tv_logout);
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefUtils.clearUser();

                Model.setUser(null);

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        llKaryawan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, KaryawanActivity.class);
                startActivity(intent);
            }
        });

        if (Model.getUser().getUserType() == 2) {
            btnBertugas.setVisibility(View.VISIBLE);
        } else if (Model.getUser().getUserType() == 1) {
            llKaryawan.setVisibility(View.VISIBLE);
            btnCheckin.setVisibility(View.VISIBLE);
        }

        kendaraanArrayList = new ArrayList<>();
        kendaraanParkirArrayList = new ArrayList<>();

        initKendaraan();
        initKendaraanParkir();
    }

    @Override
    public void onProcessRefresh() {
        getUserInfo();
        getKendaraanList();
        getKendaraanParkirList();
    }

    private SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            SwipeRefreshUtils.showRefresh(swipeRefreshLayout);
            SwipeRefreshUtils.processRefresh(MainActivity.this);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.PERMISSION_WRITE_EXTERNAL_STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else if (Build.VERSION.SDK_INT >= 23 && !shouldShowRequestPermissionRationale(permissions[0])) {
                    // User selected the Never Ask Again Option Change settings in app settings manually
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setTitle("Konfirmasi");
                    alertDialogBuilder
                            .setMessage("Mohon izinkan kami dari setting untuk membaca gambar barcode. Terima kasih")
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
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !hasPermission()) {

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                        alertDialogBuilder.setTitle("Konfirmasi");
                        alertDialogBuilder
                                .setMessage("Untuk membaca gambar barcode, anda harus mengizinkan kami. Izinkan?")
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
                    }
                }
            }
        }
    }

    private void initKendaraan() {
        RecyclerView rvKendaraan = (RecyclerView) findViewById(R.id.kendaraan_recycler_view);
        rvKendaraan.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvKendaraan.setItemAnimator(new DefaultItemAnimator());

        kendaraanAdapter = new KendaraanAdapter(this, kendaraanArrayList, 1);
        rvKendaraan.setAdapter(kendaraanAdapter);
    }

    private void getUserInfo() {
        userLoading = true;
        BaseRequest baseRequest = new BaseRequest(Model.getUser().getUserId());

        APIService apiService = HTTPClient.getClient().create(APIService.class);
        Call<LoginResponse> call = apiService.getUserInfo(baseRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body().getStatusCode() == Constants.STATUS_CODE_SUCCESS) {
                    getUserInfoSuccess(response.body().getData());
                } else {
                    userLoading = false;
                    hideRefresh();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                t.printStackTrace();
                userLoading = false;
                hideRefresh();
            }
        });
    }

    private void getUserInfoSuccess(LoginResponse.Data data) {
        int userId = data.getUserId();
        String nama = data.getNama();
        String email = data.getEmail();
        String jenisKelamin = data.getJenisKelamin();
        String tempatLahir = data.getTempatLahir();
        String tanggalLahir = data.getTanggalLahir();
        String alamat = data.getAlamat();
        String saldo = data.getSaldo();
        int userType = data.getUserType();

        User user = new User(userId, nama, email, jenisKelamin, tempatLahir, tanggalLahir, alamat, saldo, userType);
        Model.setUser(user);

        prefUtils.saveUser(user);

        userLoading = false;
        setUserView();

        hideRefresh();
    }

    private void getKendaraanList() {
        kendaraanLoading = true;
        if (kendaraanArrayList != null && kendaraanArrayList.size() > 0) {
            kendaraanArrayList.clear();
        }
        BaseRequest baseRequest = new BaseRequest(Model.getUser().getUserId());
        APIService apiService = HTTPClient.getClient().create(APIService.class);
        Call<GetUserVehicleResponse> call = apiService.getUserVehicle(baseRequest);
        call.enqueue(new Callback<GetUserVehicleResponse>() {
            @Override
            public void onResponse(Call<GetUserVehicleResponse> call, Response<GetUserVehicleResponse> response) {
                if (response.isSuccessful()) {
                    switch (response.body().getStatusCode()) {
                        case Constants.STATUS_CODE_SUCCESS :
                            getKendaraanListSuccess(response.body().getData());
                            break;
                        case Constants.STATUS_CODE_BAD_REQUEST :
                            llKendaraan.setVisibility(View.GONE);

                            kendaraanLoading = false;
                            hideRefresh();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<GetUserVehicleResponse> call, Throwable t) {
                t.printStackTrace();
                llKendaraan.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, getString(R.string.connection_error), Toast.LENGTH_LONG).show();

                kendaraanLoading = false;
                hideRefresh();
            }
        });
    }

    private void getKendaraanListSuccess(ArrayList<GetUserVehicleResponse.Data> dataArrayList) {
        if (dataArrayList.size() > 0) {
            for (GetUserVehicleResponse.Data data : dataArrayList) {
                int kendaraanId = data.getKendaraanId();
                String nomorRegistrasi = data.getNomorRegistrasi();
                String nama = data.getNama();
                String alamat = data.getAlamat();
                String merk = data.getMerk();
                String type = data.getType();
                String tahunPembuatan = data.getTahunPembuatan();
                String nomorRangka = data.getNomorRangka();
                String nomorMesin = data.getNomorMesin();
                String vehicleType = data.getVehicleType();

                Kendaraan kendaraan = new Kendaraan(kendaraanId, nomorRegistrasi, nama, alamat, merk, type, tahunPembuatan, nomorRangka, nomorMesin, vehicleType);
                kendaraanArrayList.add(kendaraan);
            }
            llKendaraan.setVisibility(View.VISIBLE);
            kendaraanAdapter.updateKendaraanArrayList(kendaraanArrayList);
        } else {
            llKendaraan.setVisibility(View.GONE);
        }
        kendaraanLoading = false;
        hideRefresh();
    }

    private void initKendaraanParkir() {
        RecyclerView rvKendaraanParkir = (RecyclerView) findViewById(R.id.kendaraan_parkir_recycler_view);
        rvKendaraanParkir.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvKendaraanParkir.setItemAnimator(new DefaultItemAnimator());

        kendaraanParkirAdapter = new KendaraanParkirAdapter(this, kendaraanParkirArrayList);
        rvKendaraanParkir.setAdapter(kendaraanParkirAdapter);
    }

    private void getKendaraanParkirList() {
        kendaraanParkirLoading = true;
        if (kendaraanParkirArrayList != null && kendaraanParkirArrayList.size() > 0) {
            kendaraanParkirArrayList.clear();
        }

        BaseRequest baseRequest = new BaseRequest(Model.getUser().getUserId());
        APIService apiService = HTTPClient.getClient().create(APIService.class);
        Call<GetVehicleInParkirResponse> call = apiService.getVehicleParkir(baseRequest);
        call.enqueue(new Callback<GetVehicleInParkirResponse>() {
            @Override
            public void onResponse(Call<GetVehicleInParkirResponse> call, Response<GetVehicleInParkirResponse> response) {
                if (response.isSuccessful()) {
                    switch (response.body().getStatusCode()) {
                        case Constants.STATUS_CODE_SUCCESS :
                            getKendaraanParkirSuccess(response.body().getData());
                            break;
                        case Constants.STATUS_CODE_BAD_REQUEST :
                            llKendaraanParkir.setVisibility(View.GONE);

                            kendaraanParkirLoading = false;
                            hideRefresh();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<GetVehicleInParkirResponse> call, Throwable t) {
                t.printStackTrace();
                llKendaraanParkir.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, getString(R.string.connection_error), Toast.LENGTH_LONG).show();

                kendaraanParkirLoading = false;
                hideRefresh();
            }
        });
    }

    private void getKendaraanParkirSuccess(ArrayList<GetVehicleInParkirResponse.Data> dataArrayList) {
        if (dataArrayList.size() > 0) {
            for (GetVehicleInParkirResponse.Data data : dataArrayList) {
                String infoParkir = data.getInfoParkir();
                String nominal = data.getNominal();
                String nomorRegistrasi = data.getNomorRegistrasi();

                KendaraanPakir kendaraanPakir = new KendaraanPakir(infoParkir, nominal, nomorRegistrasi);
                kendaraanParkirArrayList.add(kendaraanPakir);
            }
            llKendaraanParkir.setVisibility(View.VISIBLE);
            kendaraanParkirAdapter.updateKendaraanParkirArrayList(kendaraanParkirArrayList);
        } else {
            llKendaraanParkir.setVisibility(View.GONE);
        }
        kendaraanParkirLoading = false;
        hideRefresh();
    }

    public void showBarcode(String nomorRegistrasi) {
        if (hasPermission()) {
            String imageName = nomorRegistrasi + ".png";

            String imgUrl = Constants.BASE_URL_BARCODE + imageName;

            Log.d(TAG, "imgUrl = " + imgUrl);

            View view = LayoutInflater.from(this).inflate(R.layout.dialog_barcode, null);
            final AlertDialog dialog = new AlertDialog.Builder(this).create();
            dialog.setView(view);

            TextView tvNomorRegistrasi = (TextView) view.findViewById(R.id.tv_nomor_registrasi);
            tvNomorRegistrasi.setText(nomorRegistrasi);

            Button btnTutup = (Button) view.findViewById(R.id.btn_tutup);
            btnTutup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            ImageView imageView = view.findViewById(R.id.img_barcode);
            Picasso.Builder builder = new Picasso.Builder(getApplicationContext());
            builder.listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    exception.printStackTrace();
                }

            });
            Picasso pic = builder.build();
            pic.load(imgUrl).into(picassoImageTarget(getApplicationContext(), "vehicle", imageName, imageView));
            dialog.show();
        } else {
            requestPermission();
        }
    }

    private Target picassoImageTarget(Context context, final String imageDir, final String imageName, final ImageView imageView) {
        ContextWrapper cw = new ContextWrapper(context);
        final File directory = cw.getDir(imageDir, Context.MODE_PRIVATE);
        final File myImageFile = new File(directory, imageName);
        return new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                Log.d(TAG, "onBitmapLoaded");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "run()");
                        if (!myImageFile.exists()) {
                            Log.d(TAG, "!myImageFile.exists()");
                            FileOutputStream fos = null;
                            try {
                                fos = new FileOutputStream(myImageFile);
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                try {
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, "loaded from >> " + myImageFile.getAbsolutePath());
                                Picasso.get().load(myImageFile).resize(800, 800).centerInside().into(imageView);
                            }
                        });
                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Log.d(TAG, "onBitmapFailed");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Log.d(TAG, "onPrepareLoad");
                if (placeHolderDrawable != null) {}
            }
        };
    }

    private boolean hasPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] permission = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (ActivityCompat.checkSelfPermission(MainActivity.this, permission[0]) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.PERMISSION_WRITE_EXTERNAL_STORAGE_REQUEST_CODE );
    }

    private void initUser() {
        if (Model.getUser() == null) {
            if (prefUtils.isLoggedIn()) {
                User user = prefUtils.getUser();
                Model.setUser(user);
                setUserView();
            }
        }
    }

    private void hideRefresh() {
        if (!userLoading && !kendaraanLoading && !kendaraanParkirLoading) {
            SwipeRefreshUtils.hideRefresh(swipeRefreshLayout);
        }
    }

    private void setUserView() {
        if (!userLoading) {
            tvNama.setText(Model.getUser().getNama());
            tvSaldo.setText(Model.getUser().getSaldo());
        }
    }
}
