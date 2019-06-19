package com.bleizing.parkirqyu.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bleizing.parkirqyu.Constants;
import com.bleizing.parkirqyu.R;
import com.bleizing.parkirqyu.adapters.KaryawanAdapter;
import com.bleizing.parkirqyu.adapters.KendaraanAdapter;
import com.bleizing.parkirqyu.models.Karyawan;
import com.bleizing.parkirqyu.models.Kendaraan;
import com.bleizing.parkirqyu.models.Model;
import com.bleizing.parkirqyu.network.APIService;
import com.bleizing.parkirqyu.network.DataResponse;
import com.bleizing.parkirqyu.network.DeleteVehicleRequest;
import com.bleizing.parkirqyu.network.DeleteVehicleResponse;
import com.bleizing.parkirqyu.network.GetKendaraanByUserRequest;
import com.bleizing.parkirqyu.network.GetUserVehicleResponse;
import com.bleizing.parkirqyu.network.HTTPClient;
import com.bleizing.parkirqyu.utils.SwipeRefreshUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KendaraanActivity extends AppCompatActivity implements SwipeRefreshUtils.SwipeRefreshUtilsListener {
    private static final String TAG = "KaryawanActivity";

    private SwipeRefreshLayout swipeRefreshLayout;

    private ArrayList<Kendaraan> kendaraanArrayList;

    private KendaraanAdapter adapter;

    private ProgressDialog progressDialog;

    public Karyawan karyawan;

    private RecyclerView rvKendaraan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kendaraan);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(refreshListener);

        FloatingActionButton fabAddKendaraan = (FloatingActionButton) findViewById(R.id.fab_add_kendaraan);
        fabAddKendaraan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KendaraanActivity.this, KendaraanFormActivity.class);
                intent.putExtra("karyawan", karyawan);
                startActivity(intent);
                finish();
            }
        });

        karyawan = null;

        Intent intent = getIntent();
        if (intent.getParcelableExtra("karyawan") != null) {
            karyawan = intent.getParcelableExtra("karyawan");
        }

        TextView tvHeaderNama = (TextView) findViewById(R.id.tv_header_nama);
        tvHeaderNama.setText(karyawan.getNama());

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
            SwipeRefreshUtils.processRefresh(KendaraanActivity.this);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        refreshListener.onRefresh();
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

    private void initKendaraan() {
        rvKendaraan = (RecyclerView) findViewById(R.id.kendaraan_recycler_view);

        rvKendaraan.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvKendaraan.setItemAnimator(new DefaultItemAnimator());
        adapter = new KendaraanAdapter(this, kendaraanArrayList, 2);
        rvKendaraan.setAdapter(adapter);
    }

    private void getKendaraanList() {
        if (kendaraanArrayList != null && kendaraanArrayList.size() > 0) {
            kendaraanArrayList.clear();
        }

        GetKendaraanByUserRequest request = new GetKendaraanByUserRequest(Model.getUser().getUserId(), karyawan.getUserId());
        APIService apiService = HTTPClient.getClient().create(APIService.class);
        Call<GetUserVehicleResponse> call = apiService.getVehicleByUserId(request);
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
                Toast.makeText(KendaraanActivity.this, getString(R.string.connection_error), Toast.LENGTH_LONG).show();
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
        } else {
            rvKendaraan.setVisibility(View.GONE);
            Toast.makeText(KendaraanActivity.this, getString(R.string.data_empty), Toast.LENGTH_LONG).show();
        }
        adapter.updateKendaraanArrayList(kendaraanArrayList);
        SwipeRefreshUtils.hideRefresh(swipeRefreshLayout);
    }

    public void hapusKendaraan(int kendaraanId) {
        progressDialog = new ProgressDialog(KendaraanActivity.this);
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
                            refreshListener.onRefresh();
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
        Toast.makeText(KendaraanActivity.this, message, Toast.LENGTH_LONG).show();
    }
}
