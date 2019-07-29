package com.bleizing.parkirqyu.activities;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bleizing.parkirqyu.Constants;
import com.bleizing.parkirqyu.R;
import com.bleizing.parkirqyu.adapters.KendaraanParkirAdapter;
import com.bleizing.parkirqyu.models.KendaraanPakir;
import com.bleizing.parkirqyu.models.Model;
import com.bleizing.parkirqyu.network.APIService;
import com.bleizing.parkirqyu.network.BaseRequest;
import com.bleizing.parkirqyu.network.GetVehicleInParkirResponse;
import com.bleizing.parkirqyu.network.HTTPClient;
import com.bleizing.parkirqyu.utils.SwipeRefreshUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllKendaraanParkirActivity extends AppCompatActivity implements SwipeRefreshUtils.SwipeRefreshUtilsListener {

    private SwipeRefreshLayout swipeRefreshLayout;

    private KendaraanParkirAdapter kendaraanParkirAdapter;

    private ArrayList<KendaraanPakir> kendaraanParkirArrayList;

    private RecyclerView rvKendaraanParkir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_kendaraan_parkir);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(refreshListener);

        kendaraanParkirArrayList = new ArrayList<>();
        initView();
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

    @Override
    protected void onStart() {
        super.onStart();
        refreshListener.onRefresh();
    }

    @Override
    public void onProcessRefresh() {
        getData();
    }

    private SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            SwipeRefreshUtils.showRefresh(swipeRefreshLayout);
            SwipeRefreshUtils.processRefresh(AllKendaraanParkirActivity.this);
        }
    };

    private void initView() {
        rvKendaraanParkir = (RecyclerView) findViewById(R.id.kendaraan_parkir_recycler_view);
        rvKendaraanParkir.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvKendaraanParkir.setItemAnimator(new DefaultItemAnimator());

        kendaraanParkirAdapter = new KendaraanParkirAdapter(this, kendaraanParkirArrayList, true);
        rvKendaraanParkir.setAdapter(kendaraanParkirAdapter);
    }

    private void getData() {
        if (kendaraanParkirArrayList != null && kendaraanParkirArrayList.size() > 0) {
            kendaraanParkirArrayList.clear();
        }

        BaseRequest baseRequest = new BaseRequest(Model.getUser().getUserId());
        APIService apiService = HTTPClient.getClient().create(APIService.class);
        Call<GetVehicleInParkirResponse> call = apiService.getAllVehicleParkir(baseRequest);
        call.enqueue(new Callback<GetVehicleInParkirResponse>() {
            @Override
            public void onResponse(Call<GetVehicleInParkirResponse> call, Response<GetVehicleInParkirResponse> response) {
                if (response.isSuccessful()) {
                    switch (response.body().getStatusCode()) {
                        case Constants.STATUS_CODE_SUCCESS :
                            getKendaraanParkirSuccess(response.body().getData());
                            break;
                        case Constants.STATUS_CODE_BAD_REQUEST :
                            rvKendaraanParkir.setVisibility(View.GONE);

                            SwipeRefreshUtils.hideRefresh(swipeRefreshLayout);
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<GetVehicleInParkirResponse> call, Throwable t) {
                t.printStackTrace();
                rvKendaraanParkir.setVisibility(View.GONE);
                Toast.makeText(AllKendaraanParkirActivity.this, getString(R.string.connection_error), Toast.LENGTH_LONG).show();

                SwipeRefreshUtils.hideRefresh(swipeRefreshLayout);
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
            kendaraanParkirAdapter.updateKendaraanParkirArrayList(kendaraanParkirArrayList);
        } else {
            rvKendaraanParkir.setVisibility(View.GONE);
            Toast.makeText(AllKendaraanParkirActivity.this, getString(R.string.data_empty), Toast.LENGTH_LONG).show();
        }
        SwipeRefreshUtils.hideRefresh(swipeRefreshLayout);
    }
}
