package com.example.roumeliotis.coen242projectapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QRCode extends AppCompatActivity {

    Bitmap bitmap;
    ImageView qrImage;
    String eventKey;
    User user;
    String orderKey;
    Manager Manager;
    QRGEncoder qrgEncoder; // = new QRGEncoder(inputValue, null, QRGContents.Type.TEXT, smallerDimension);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_page);

        getSupportActionBar().setTitle(R.string.app_name);
        Manager = new Manager(this);
        Intent userInfo = getIntent();
        user = userInfo.getParcelableExtra("user");
        eventKey = userInfo.getStringExtra("eventKey");
        orderKey = userInfo.getStringExtra("orderKey");
        qrImage = (ImageView) findViewById(R.id.QR_Image);

        qrgEncoder = new QRGEncoder(orderKey, null, QRGContents.Type.TEXT, 700);
        try {
            // Getting QR-Code as Bitmap
            bitmap = qrgEncoder.encodeAsBitmap();
            // Setting Bitmap to ImageView
            qrImage.setImageBitmap(bitmap);
        }
        catch(Exception e) {
            //Log.v(e, e.toString());
        }
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
        intent.setClass(QRCode.this, Map.class);
        startActivity(intent);
    }

    void goToCreateOrder() {
        Intent intent = new Intent();
        intent.putExtra("eventKey", eventKey);
        intent.putExtra("user", user);
        intent.setClass(QRCode.this, CreateDrink.class);
        startActivity(intent);
    }

    void goToViewPastOrders(){
        Intent intent = new Intent();
        intent.putExtra("eventKey", eventKey);
        intent.putExtra("user", user);
        intent.setClass(QRCode.this, Orders.class);
        startActivity(intent);
    }

    void goToEventReg(){
        Intent intent = new Intent();
        //intent.putExtra("eventKey", eventKey);
        intent.putExtra("user", user);
        intent.setClass(QRCode.this, EventRegistration.class);
        startActivity(intent);
    }
}
