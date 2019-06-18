package com.bleizing.parkirqyu.utils;

import android.support.v4.widget.SwipeRefreshLayout;

public class SwipeRefreshUtils {
    public static void showRefresh(SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setRefreshing(true);
    }

    public static void hideRefresh(SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setRefreshing(false);
    }

    public static void processRefresh(SwipeRefreshUtilsListener listener) {
        listener.onProcessRefresh();
    }

    public interface SwipeRefreshUtilsListener {
        void onProcessRefresh();
    }
}
