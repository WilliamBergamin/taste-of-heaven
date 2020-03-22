package com.example.roumeliotis.coen242projectapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

// View all of your orders to gain access to input code
public class Orders extends AppCompatActivity{

    ListView ordersList;
    ServerHelper serverHelper;
    Manager Manager;
    String eventKey;
    User user;

    // QR Stuff
    Bitmap bitmap;
    ImageView qrImage;
    ListView qrList;
    QRGEncoder qrgEncoder; // = new QRGEncoder(inputValue, null, QRGContents.Type.TEXT, smallerDimension);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders_page);

        getSupportActionBar().setTitle(R.string.app_name);
        serverHelper = new ServerHelper();
        Manager = new Manager(this);
        Intent userInfo = getIntent();
        user = userInfo.getParcelableExtra("user");
        eventKey = userInfo.getStringExtra("eventKey");
        qrImage = (ImageView) findViewById(R.id.QR_Image);

        ordersList = findViewById(R.id.orderCodesList);

        List<Order> orders = Manager.getOrdersByUserID(user.getid());
        List<String> orderStrings = new ArrayList<String>();
        List<ImageView> qrImages = new ArrayList<ImageView>();
        for (int i = 0; i < orders.size(); i++) {
            Log.d("Orders", orders.get(i).toString());
            orderStrings.add(orders.get(i).toString());
            qrgEncoder = new QRGEncoder(orders.get(i).getOrder_key(), null, QRGContents.Type.TEXT, 325);
            try {
                // Getting QR-Code as Bitmap
                bitmap = qrgEncoder.encodeAsBitmap();
                // Setting Bitmap to ImageView
                qrImage.setImageBitmap(bitmap);
                qrImages.add(qrImage);
            }
            catch(Exception e) {
                //Log.v(e, e.toString());
            }
        }

        ordersList.setAdapter(new ArrayAdapter<String>(this, R.layout.row, orderStrings));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Toast.makeText(this, "Selected Item: " +item.getTitle(), Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case R.id.event:
                goToEventReg();
                return true;
            case R.id.drink:
                goToCreateOrder();
                return true;
            case R.id.map:
                goToMap();
                return true;
            case R.id.orders:
                goToViewPastOrders();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }



    void goToMap() {
        Intent intent = new Intent();
        intent.putExtra("user", user);
        intent.putExtra("eventKey", eventKey);
        intent.setClass(Orders.this, Map.class);
        startActivity(intent);
    }

    void goToCreateOrder() {
        Intent intent = new Intent();
        intent.putExtra("eventKey", eventKey);
        intent.putExtra("user", user);
        intent.setClass(Orders.this, CreateDrink.class);
        startActivity(intent);
    }

    void goToViewPastOrders(){
        Intent intent = new Intent();
        intent.putExtra("eventKey", eventKey);
        intent.putExtra("user", user);
        intent.setClass(Orders.this, Orders.class);
        startActivity(intent);
    }

    void goToEventReg(){
        Intent intent = new Intent();
        //intent.putExtra("eventKey", eventKey);
        intent.putExtra("user", user);
        intent.setClass(Orders.this, EventRegistration.class);
        startActivity(intent);
    }
}
