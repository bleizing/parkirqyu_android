package com.bleizing.parkirqyu.models;

public class Model {
    private static User user;
    private static boolean networkAvailable;

    public static void setUser(User user) {
        Model.user = user;
    }

    public static User getUser() {
        return user;
    }

    public static void setNetworkAvailable(boolean networkAvailable) {
        Model.networkAvailable = networkAvailable;
    }

    public static boolean isNetworkAvailable() {
        return networkAvailable;
    }
}
