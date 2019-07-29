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
import com.bleizing.parkirqyu.adapters.KaryawanAdapter;
import com.bleizing.parkirqyu.adapters.LogTransaksiAdapter;
import com.bleizing.parkirqyu.models.Karyawan;
import com.bleizing.parkirqyu.models.LogTransaksi;
import com.bleizing.parkirqyu.models.Model;
import com.bleizing.parkirqyu.network.APIService;
import com.bleizing.parkirqyu.network.BaseRequest;
import com.bleizing.parkirqyu.network.GetLogTransactionResponse;
import com.bleizing.parkirqyu.network.HTTPClient;
import com.bleizing.parkirqyu.utils.SwipeRefreshUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogTransaksiActivity extends AppCompatActivity implements SwipeRefreshUtils.SwipeRefreshUtilsListener {

    private SwipeRefreshLayout swipeRefreshLayout;

    private LogTransaksiAdapter adapter;

    private ArrayList<LogTransaksi> logTransaksiArrayList;

    private RecyclerView recyclerView;

    private boolean isAllData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_transaksi);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        isAllData = getIntent().getBooleanExtra("isAllData", false);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(refreshListener);

        logTransaksiArrayList = new ArrayList<>();
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
            SwipeRefreshUtils.processRefresh(LogTransaksiActivity.this);
        }
    };

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.log_recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new LogTransaksiAdapter(this, logTransaksiArrayList);
        recyclerView.setAdapter(adapter);
    }

    private void getData() {
        if (logTransaksiArrayList != null && logTransaksiArrayList.size() > 0) {
            logTransaksiArrayList.clear();
        }

        BaseRequest baseRequest = new BaseRequest(Model.getUser().getUserId());
        APIService apiService = HTTPClient.getClient().create(APIService.class);

        Call<GetLogTransactionResponse> call;
        if (isAllData) {
            call = apiService.getAllLogTransaction(baseRequest);
        } else {
            call = apiService.getUserLogTransaction(baseRequest);
        }

        call.enqueue(new Callback<GetLogTransactionResponse>() {
            @Override
            public void onResponse(Call<GetLogTransactionResponse> call, Response<GetLogTransactionResponse> response) {
                if (response.isSuccessful()) {
                    switch (response.body().getStatusCode()) {
                        case Constants.STATUS_CODE_SUCCESS :
                            getDataSuccess(response.body().getData());
                            break;
                        case Constants.STATUS_CODE_BAD_REQUEST :
                            SwipeRefreshUtils.hideRefresh(swipeRefreshLayout);
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<GetLogTransactionResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(LogTransaksiActivity.this, getString(R.string.connection_error), Toast.LENGTH_LONG).show();

                SwipeRefreshUtils.hideRefresh(swipeRefreshLayout);
            }
        });
    }

    private void getDataSuccess(ArrayList<GetLogTransactionResponse.Data> dataArrayList) {
        if (dataArrayList.size() > 0) {
            for (GetLogTransactionResponse.Data data : dataArrayList) {
                String nomorRegitrasi = data.getNomorRegistrasi();
                String invoiceCode = data.getInvoiceCode();
                String invoiceType = data.getInvoiceType();
                String nominal = data.getNominal();
                String transactionType = data.getTransactionType();
                String time = data.getTime();
                String namaPetugas = data.getNamaPetugas();
                String jenisPelanggan = data.getJenisPelanggan();

                LogTransaksi logTransaksi = new LogTransaksi(nomorRegitrasi, invoiceCode, invoiceType, nominal, transactionType, time, namaPetugas, jenisPelanggan);

                logTransaksiArrayList.add(logTransaksi);
            }
        } else {
            recyclerView.setVisibility(View.GONE);
            Toast.makeText(LogTransaksiActivity.this, getString(R.string.data_empty), Toast.LENGTH_LONG).show();
        }
        adapter.setLogTransaksiArrayList(logTransaksiArrayList);
        SwipeRefreshUtils.hideRefresh(swipeRefreshLayout);
    }
}
