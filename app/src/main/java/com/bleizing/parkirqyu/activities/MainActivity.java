package com.bleizing.parkirqyu.activities;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
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

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshUtils.SwipeRefreshUtilsListener {

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
        kendaraanAdapter = new KendaraanAdapter(this, kendaraanArrayList);
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
}
