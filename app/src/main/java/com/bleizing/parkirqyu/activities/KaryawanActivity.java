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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bleizing.parkirqyu.Constants;
import com.bleizing.parkirqyu.R;
import com.bleizing.parkirqyu.RecyclerTouchListener;
import com.bleizing.parkirqyu.RecyclerViewClickListener;
import com.bleizing.parkirqyu.adapters.KaryawanAdapter;
import com.bleizing.parkirqyu.models.Karyawan;
import com.bleizing.parkirqyu.models.Model;
import com.bleizing.parkirqyu.network.APIService;
import com.bleizing.parkirqyu.network.BaseRequest;
import com.bleizing.parkirqyu.network.DataResponse;
import com.bleizing.parkirqyu.network.DeleteEmployeeRequest;
import com.bleizing.parkirqyu.network.DeleteEmployeeResponse;
import com.bleizing.parkirqyu.network.GetAllEmployeeResponse;
import com.bleizing.parkirqyu.network.HTTPClient;
import com.bleizing.parkirqyu.utils.SwipeRefreshUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KaryawanActivity extends AppCompatActivity implements SwipeRefreshUtils.SwipeRefreshUtilsListener {
    private static final String TAG = "KaryawanActivity";

    private SwipeRefreshLayout swipeRefreshLayout;

    private KaryawanAdapter adapter;

    private ArrayList<Karyawan> karyawanArrayList;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karyawan);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(refreshListener);

        FloatingActionButton fabAddKaryawan = (FloatingActionButton) findViewById(R.id.fab_add_karyawan);
        fabAddKaryawan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KaryawanActivity.this, KaryawanFormActivity.class);
                startActivity(intent);
            }
        });

        karyawanArrayList = new ArrayList<>();
        initKaryawan();
    }

    @Override
    public void onProcessRefresh() {
        getKaryawanList();
    }

    private SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            SwipeRefreshUtils.showRefresh(swipeRefreshLayout);
            SwipeRefreshUtils.processRefresh(KaryawanActivity.this);
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

    private void initKaryawan() {
        RecyclerView rvKaryawan = (RecyclerView) findViewById(R.id.karyawan_recycler_view);

        rvKaryawan.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvKaryawan.setItemAnimator(new DefaultItemAnimator());
        adapter = new KaryawanAdapter(this, karyawanArrayList);
        rvKaryawan.setAdapter(adapter);
    }

    private void getKaryawanList() {
        if (karyawanArrayList != null && karyawanArrayList.size() > 0) {
            karyawanArrayList.clear();
        }

        BaseRequest baseRequest = new BaseRequest(Model.getUser().getUserId());
        APIService apiService = HTTPClient.getClient().create(APIService.class);
        Call<GetAllEmployeeResponse> call = apiService.getAllEmployee(baseRequest);
        call.enqueue(new Callback<GetAllEmployeeResponse>() {
            @Override
            public void onResponse(Call<GetAllEmployeeResponse> call, Response<GetAllEmployeeResponse> response) {
                if (response.isSuccessful()) {
                    switch (response.body().getStatusCode()) {
                        case Constants.STATUS_CODE_SUCCESS :
                            getAllEmployeeSuccess(response.body().getData());
                            break;
                        case Constants.STATUS_CODE_BAD_REQUEST :
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<GetAllEmployeeResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(KaryawanActivity.this, getString(R.string.connection_error), Toast.LENGTH_LONG).show();
                SwipeRefreshUtils.hideRefresh(swipeRefreshLayout);
            }
        });
    }

    private void getAllEmployeeSuccess(ArrayList<GetAllEmployeeResponse.Data> dataArrayList) {
        if (dataArrayList.size() > 0) {
            for (GetAllEmployeeResponse.Data data : dataArrayList) {
                int userId = data.getUserId();
                String nama = data.getNama();
                String email = data.getEmail();

                Karyawan karyawan = new Karyawan(userId, email, nama);
                karyawanArrayList.add(karyawan);
            }
            adapter.setKaryawanArrayList(karyawanArrayList);
        } else {
            Toast.makeText(KaryawanActivity.this, getString(R.string.data_empty), Toast.LENGTH_LONG).show();
        }
        SwipeRefreshUtils.hideRefresh(swipeRefreshLayout);
    }

    public void hapusEmployee(int employeeId) {
        progressDialog = new ProgressDialog(KaryawanActivity.this);
        progressDialog.setMessage("Sedang Diproses...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        DeleteEmployeeRequest request = new DeleteEmployeeRequest(Model.getUser().getUserId(), employeeId);

        APIService apiService = HTTPClient.getClient().create(APIService.class);
        Call<DeleteEmployeeResponse> call = apiService.deleteEmployee(request);
        call.enqueue(new Callback<DeleteEmployeeResponse>() {
            @Override
            public void onResponse(Call<DeleteEmployeeResponse> call, Response<DeleteEmployeeResponse> response) {
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
            public void onFailure(Call<DeleteEmployeeResponse> call, Throwable t) {
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
        Toast.makeText(KaryawanActivity.this, message, Toast.LENGTH_LONG).show();
    }
}
