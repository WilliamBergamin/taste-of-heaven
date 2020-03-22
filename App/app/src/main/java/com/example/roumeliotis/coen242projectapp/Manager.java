package com.example.roumeliotis.coen242projectapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Manager extends SQLiteOpenHelper{

    private static final String TAG = "Manager";
    private Context context = null;

    public Manager(Context context) {
        super(context, ManagerConfigs.DATABASE_NAME, null, ManagerConfigs.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG,"Manager onCreate");

        //Create User Table
        String CREATE_USER_TABLE = "CREATE TABLE " + ManagerConfigs.TABLE_USER + "(" +
                ManagerConfigs.USER_ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ManagerConfigs.USER_NAME_COLUMN + " TEXT NOT NULL, " +
                ManagerConfigs.USER_EMAIL_COLUMN + " TEXT NOT NULL UNIQUE, " +
                ManagerConfigs.USER_PASSWORD_COLUMN + " TEXT NOT NULL," +
                ManagerConfigs.USER_TOKEN_COLUMN + " TEXT UNIQUE)";

        Log.d(TAG, CREATE_USER_TABLE);
        db.execSQL(CREATE_USER_TABLE);

        // Create Order Table
        String CREATE_ORDER_TABLE = "CREATE TABLE " + ManagerConfigs.TABLE_ORDER + "(" +
                ManagerConfigs.ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ManagerConfigs.USER_ID_ORDER + " INTEGER NOT NULL, " +
//                ManagerConfigs.ORDER_REMOTE_ID + " INTEGER NOT NULL UNIQUE, " +
                ManagerConfigs.ORDER_KEY + " INTEGER NOT NULL UNIQUE, " +
                ManagerConfigs.ORDER_MACHINE_ID + " TEXT NOT NULL, " +
                ManagerConfigs.ORDER_MIXER_COLUMN + " TEXT NOT NULL, " +
                ManagerConfigs.ORDER_ALCOHOL_COLUMN + " TEXT NOT NULL, " +
                ManagerConfigs.ORDER_DOUBLE_COLUMN + " BOOLEAN NOT NULL, " +
                ManagerConfigs.ORDER_PRICE_COLUMN + " REAL NOT NULL, " +
                ManagerConfigs.ORDER_STATE_COLUMN + " TEXT NOT NULL, " +
                ManagerConfigs.ORDER_PAID_COLUMN + " BOOLEAN NOT NULL)";

        Log.d(TAG, CREATE_ORDER_TABLE);
        db.execSQL(CREATE_ORDER_TABLE);

//        // Create event table
//        String CREATE_EVENT_TABLE = "CREATE TABLE " + ManagerConfigs.TABLE_EVENTLIST + "(" +
//                ManagerConfigs.EVENT_USER_EMAIL + "TEXT NOT NULL, " +
//                ManagerConfigs.EVENT_KEY + "TEXT NOT NULL)";
//
//        Log.d(TAG, CREATE_EVENT_TABLE);
//        db.execSQL(CREATE_EVENT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ManagerConfigs.TABLE_USER);
        onCreate(db);
    }


    // Insert user
    public long insertUser(User user){
        Log.d(TAG, "insertUser");
        long id = -1;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM "+ ManagerConfigs.TABLE_USER);

        ContentValues contentValues = new ContentValues();
        contentValues.put(ManagerConfigs.USER_NAME_COLUMN, user.getName());
        contentValues.put(ManagerConfigs.USER_EMAIL_COLUMN, user.getEmail());
        contentValues.put(ManagerConfigs.USER_PASSWORD_COLUMN, user.getPassword());
        contentValues.put(ManagerConfigs.USER_TOKEN_COLUMN, user.getToken());

        try {
            id = sqLiteDatabase.replaceOrThrow(ManagerConfigs.TABLE_USER, null, contentValues);
        } catch (SQLiteException e){
            Log.d(TAG,"Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }
        Log.d(TAG, "success");
        return id;
    }

    public void clearDrinks(){
        Log.d(TAG, "clearDrinks");
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM "+ ManagerConfigs.TABLE_ORDER);
    }

    public long getUserIdFromToken(String token){
        Log.d(TAG, "getUserIdFromToken");
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long id = -1;
        try {
            String SELECT_QUERY = String.format("SELECT %s FROM %s WHERE %s == \"%s\";",ManagerConfigs.USER_ID_COLUMN, ManagerConfigs.TABLE_USER,
                ManagerConfigs.USER_TOKEN_COLUMN, token);
            Cursor cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);

            while(cursor.moveToNext()){
                id = cursor.getLong(cursor.getColumnIndex(ManagerConfigs.USER_ID_COLUMN));
            }
        }
        catch (Exception e){
            Log.d(TAG,"Exception: " + e.getMessage());
        } finally {
            sqLiteDatabase.close();
        }
        return id;
    }

    // Insert order
    public long insertOrder(Order order){
        Log.d(TAG, "insertOrder");

        long id = -1;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
//        contentValues.put(ManagerConfigs.ORDER_ID, order.getId());
//        contentValues.put(ManagerConfigs.USER_REMOTE_ID, order.getRemote_id());
        contentValues.put(ManagerConfigs.USER_ID_ORDER, order.getUserId());
        contentValues.put(ManagerConfigs.ORDER_KEY, order.getOrder_key());
        contentValues.put(ManagerConfigs.ORDER_MACHINE_ID, order.getMachine_id());
        contentValues.put(ManagerConfigs.ORDER_MIXER_COLUMN, order.getMixer());
        contentValues.put(ManagerConfigs.ORDER_ALCOHOL_COLUMN, order.getAlcohol());
        contentValues.put(ManagerConfigs.ORDER_DOUBLE_COLUMN, order.getDoubleAlcohol());
        contentValues.put(ManagerConfigs.ORDER_PRICE_COLUMN, order.getPrice());
        contentValues.put(ManagerConfigs.ORDER_STATE_COLUMN, order.getState());
        contentValues.put(ManagerConfigs.ORDER_PAID_COLUMN, order.getPaid());

        try {
            id = sqLiteDatabase.insertOrThrow(ManagerConfigs.TABLE_ORDER, null, contentValues);
            Log.d(TAG, "New Order has been inserted by user: " +order.getUserId());
        } catch (SQLiteException e){
            Log.d(TAG,"Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }
        Log.d(TAG, "success");
        return id;
    }

    public List<Order> getOrdersByUserID(long user_id){

        Log.d(TAG, "getOrdersByUserID");
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = null;
        List<Order> orders = new ArrayList<>();
        try {
            String SELECT_QUERY = String.format("SELECT * FROM %s WHERE %s = %s",ManagerConfigs.TABLE_ORDER,
                    ManagerConfigs.USER_ID_ORDER, user_id);
            cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);

            Log.d(TAG, "There is " +cursor.getCount()+" orders found");

            while(cursor.moveToNext()){
                long order_id = cursor.getLong(cursor.getColumnIndex(ManagerConfigs.ORDER_ID));
                String order_key = cursor.getString(cursor.getColumnIndex(ManagerConfigs.ORDER_KEY));
                String machine_id = cursor.getString((cursor.getColumnIndex(ManagerConfigs.ORDER_MACHINE_ID)));
                String mixer = cursor.getString((cursor.getColumnIndex(ManagerConfigs.ORDER_MIXER_COLUMN)));
                String alcohol = cursor.getString((cursor.getColumnIndex(ManagerConfigs.ORDER_ALCOHOL_COLUMN)));
                boolean doubleAlcohol = "1".equals(cursor.getString(cursor.getColumnIndex(ManagerConfigs.ORDER_DOUBLE_COLUMN)));
                double price = cursor.getDouble((cursor.getColumnIndex(ManagerConfigs.ORDER_PRICE_COLUMN)));
                String state = cursor.getString((cursor.getColumnIndex(ManagerConfigs.ORDER_STATE_COLUMN)));
                Boolean paid = Boolean.getBoolean(cursor.getString((cursor.getColumnIndex(ManagerConfigs.ORDER_PAID_COLUMN))));
                orders.add(new Order(order_id, user_id, order_key, machine_id, mixer, alcohol, doubleAlcohol, price, state, paid));
            }

        } catch (Exception e){
            Log.d(TAG,"Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }
        return orders;
    }


//    // Insert new event key
//    public long insertEventKey(String event, String userEmail){
//        Log.d(TAG, "insertEventKet");
//        long id = -1;
//        SQLiteDatabase sqlLiteDatabase = this.getWritableDatabase();
//
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(ManagerConfigs.EVENT_USER_EMAIL, userEmail);
//        contentValues.put(ManagerConfigs.EVENT_KEY, event);
//
//        try{
//
//        }
//    }

}
