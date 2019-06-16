package com.bleizing.parkirqyu.models;

public class Model {
    private static int userId;

    private static User user;

    public static void setUserId(int userId) {
        Model.userId = userId;
    }

    public static int getUserId() {
        return userId;
    }

    public static void setUser(User user) {
        Model.user = user;
    }

    public static User getUser() {
        return user;
    }
}
