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
import com.bleizing.parkirqyu.models.Kendaraan;
import com.bleizing.parkirqyu.models.Model;
import com.bleizing.parkirqyu.models.User;
import com.bleizing.parkirqyu.network.APIService;
import com.bleizing.parkirqyu.network.BaseRequest;
import com.bleizing.parkirqyu.network.GetUserVehicleResponse;
import com.bleizing.parkirqyu.network.HTTPClient;
import com.bleizing.parkirqyu.utils.PrefUtils;
import com.bleizing.parkirqyu.utils.SwipeRefreshUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshUtils.SwipeRefreshUtilsListener {
    private static final String TAG = "MainActivity";

    private SwipeRefreshLayout swipeRefreshLayout;

    private ArrayList<Kendaraan> kendaraanArrayList;
    private ArrayList<Kendaraan> kendaraanParkirArrayList;

    private KendaraanAdapter kendaraanAdapter;

    private LinearLayout llKaryawan;
    private LinearLayout llKendaraanParkir;
    private LinearLayout llKendaraan;

    private Button btnBertugas;

    private TextView tvLogout;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = Model.getUser();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(refreshListener);
        refreshListener.onRefresh();

        llKaryawan = (LinearLayout) findViewById(R.id.ll_karyawan);
        llKendaraanParkir = (LinearLayout) findViewById(R.id.ll_kendaraan_parkir);
        llKendaraan = (LinearLayout) findViewById(R.id.ll_kendaraan);
        btnBertugas = (Button) findViewById(R.id.btn_bertugas);

        tvLogout = (TextView) findViewById(R.id.tv_logout);
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefUtils prefUtils = new PrefUtils(MainActivity.this);

                prefUtils.setLoggedIn(false);
                prefUtils.setUserId(0);
                prefUtils.setNama("");
                prefUtils.setJenisKelamin("");
                prefUtils.setTempatLahir("");
                prefUtils.setTanggalLahir("");
                prefUtils.setAlamat("");
                prefUtils.setSaldo("");
                prefUtils.setUserType(0);

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

        if (user.getUserType() == 2) {
            btnBertugas.setVisibility(View.VISIBLE);
        } else if (user.getUserType() == 1) {
            llKaryawan.setVisibility(View.VISIBLE);
        }

        kendaraanArrayList = new ArrayList<>();
        initKendaraan();
    }

    @Override
    public void onProcessRefresh() {
        getKendaraanList();
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
        rvKendaraan.addOnItemTouchListener(new RecyclerTouchListener(this, rvKendaraan, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(MainActivity.this, kendaraanAdapter.getKendaraanArrayList().get(position).getNomorRegistrasi() + " Clicked", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        kendaraanAdapter = new KendaraanAdapter(this, kendaraanArrayList, 1);
        rvKendaraan.setAdapter(kendaraanAdapter);
    }

    private void getKendaraanList() {
        if (kendaraanArrayList != null && kendaraanArrayList.size() > 0) {
            kendaraanArrayList.clear();
        }
        BaseRequest baseRequest = new BaseRequest(user.getUserId());
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
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<GetUserVehicleResponse> call, Throwable t) {
                t.printStackTrace();
                llKendaraan.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, getString(R.string.connection_error), Toast.LENGTH_LONG).show();
                SwipeRefreshUtils.hideRefresh(swipeRefreshLayout);
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
        SwipeRefreshUtils.hideRefresh(swipeRefreshLayout);
    }

    public void showBarcode(String nomorRegistrasi) {
        if (hasPermission()) {
            String imgUrl = Constants.BASE_URL_BARCODE + nomorRegistrasi + ".png";

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
            Picasso.with(this).load(imgUrl).into(picassoImageTarget(getApplicationContext(), "vehicle", nomorRegistrasi, imageView));
            dialog.show();
        } else {
            requestPermission();
        }
    }

    private Target picassoImageTarget(Context context, final String imageDir, final String imageName, final ImageView imageView) {
        ContextWrapper cw = new ContextWrapper(context);
        final File directory = cw.getDir(imageDir, Context.MODE_PRIVATE); // path to /data/data/yourapp/app_imageDir
        return new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final File myImageFile = new File(directory, imageName); // Create image file
                        if (!myImageFile.exists()) {
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
                                Picasso.with(MainActivity.this).load(myImageFile).resize(800, 800).centerInside().into(imageView);
                            }
                        });
                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
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
}
