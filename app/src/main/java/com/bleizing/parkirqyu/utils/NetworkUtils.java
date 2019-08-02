package com.bleizing.parkirqyu.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.bleizing.parkirqyu.Constants;
import com.bleizing.parkirqyu.R;
import com.bleizing.parkirqyu.activities.ParkirScannerActivity;
import com.bleizing.parkirqyu.models.KendaraanCheckIn;
import com.bleizing.parkirqyu.models.Model;
import com.bleizing.parkirqyu.network.APIService;
import com.bleizing.parkirqyu.network.HTTPClient;
import com.bleizing.parkirqyu.network.ProcessCheckInRequest;
import com.bleizing.parkirqyu.network.ProcessCheckInResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkUtils {

    private static List<KendaraanCheckIn> kendaraanCheckInList;

    public static void checkNetwork(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //should check null because in airplane mode it will be null
            setNetworkIsConnected(netInfo != null && netInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            setNetworkIsConnected(false);
        }
    }

    public static void setNetworkIsConnected(boolean isConnected) {
        Model.setNetworkAvailable(isConnected);

        if (Model.isNetworkAvailable()) {
            syncToServer();
        }
    }

    private static void syncToServer() {
        NetworkUtils.kendaraanCheckInList = KendaraanCheckIn.listAll(KendaraanCheckIn.class);
        if (kendaraanCheckInList.size() > 0) {
            for (int i = 0; i < kendaraanCheckInList.size(); i++) {
                KendaraanCheckIn kendaraanCheckIn = kendaraanCheckInList.get(i);

                sendToServer(kendaraanCheckIn);
            }
        }
    }

    private static void sendToServer(final KendaraanCheckIn kendaraanCheckIn) {
        ProcessCheckInRequest request = new ProcessCheckInRequest(Model.getUser().getUserId(), kendaraanCheckIn.getNomorRegistrasi(), kendaraanCheckIn.getVehicleType(), kendaraanCheckIn.getTimeStart());
        APIService apiService = HTTPClient.getClient().create(APIService.class);
        Call<ProcessCheckInResponse> call = apiService.processCheckIn(request);
        call.enqueue(new Callback<ProcessCheckInResponse>() {
            @Override
            public void onResponse(Call<ProcessCheckInResponse> call, Response<ProcessCheckInResponse> response) {
                if (response.isSuccessful()) {
                    switch (response.body().getStatusCode()) {
                        case Constants.STATUS_CODE_CREATED:
                            NetworkUtils.kendaraanCheckInList.remove(kendaraanCheckIn);
                            break;
                        case Constants.STATUS_CODE_BAD_REQUEST:
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<ProcessCheckInResponse> call, Throwable t) {
                t.printStackTrace();
                sendToServer(kendaraanCheckIn);
            }
        });
    }

    public static String getCurrentTimeStamp(){
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

            return dateFormat.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
}
