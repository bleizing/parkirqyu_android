package com.bleizing.parkirqyu.models;

import java.util.ArrayList;

public class Model {
    private static User user;

    public static void setUser(User user) {
        Model.user = user;
    }

    public static User getUser() {
        return user;
    }
}
