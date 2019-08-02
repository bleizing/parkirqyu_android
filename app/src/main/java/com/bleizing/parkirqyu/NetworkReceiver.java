package com.bleizing.parkirqyu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bleizing.parkirqyu.utils.NetworkUtils;

public class NetworkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try
        {
            NetworkUtils.checkNetwork(context);
        } catch (NullPointerException e) {
            e.printStackTrace();
            NetworkUtils.setNetworkIsConnected(false);
        }
    }

}
