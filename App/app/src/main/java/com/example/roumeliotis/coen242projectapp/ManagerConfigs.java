package com.example.roumeliotis.coen242projectapp;

public class ManagerConfigs {

    // Needs to be fixed/updated
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "USERS.db";

    public static final String TABLE_USER = "USERS";
    public static final String USER_ID_COLUMN = "id";
    public static final String USER_NAME_COLUMN = "name";
    public static final String USER_EMAIL_COLUMN = "email";
    public static final String USER_PASSWORD_COLUMN = "password";
    public static final String USER_TOKEN_COLUMN = "token";

    public static final String TABLE_ORDER = "ORDERS";
    public static final String ORDER_ID = "id";
    public static final String USER_ID_ORDER = "user_id";
    public static final String ORDER_REMOTE_ID = "remote_id";
    public static final String ORDER_KEY = "order_key";
    public static final String ORDER_MACHINE_ID = "machine_id";
    public static final String ORDER_MIXER_COLUMN = "mixer";
    public static final String ORDER_ALCOHOL_COLUMN = "alcohol";
    public static final String ORDER_DOUBLE_COLUMN = "double";
    public static final String ORDER_PRICE_COLUMN = "price";
    public static final String ORDER_STATE_COLUMN = "status";
    public static final String ORDER_PAID_COLUMN = "paid";

    public static final String TABLE_EVENTLIST = "EVENTLIST";
    public static final String EVENT_USER_EMAIL = "email";
    public static final String EVENT_KEY = "key";
}
