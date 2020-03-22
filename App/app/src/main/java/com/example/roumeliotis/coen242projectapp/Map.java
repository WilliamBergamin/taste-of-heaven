package com.example.roumeliotis.coen242projectapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map extends AppCompatActivity implements OnMapReadyCallback{

    ServerHelper serverHelper;
    Manager Manager;
    ImageButton backToOrder;
    String eventKey;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_page);
        getSupportActionBar().setTitle(R.string.app_name);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        serverHelper = new ServerHelper();
        Manager = new Manager(this);
        Intent userInfo = getIntent();
        user = userInfo.getParcelableExtra("user");
        eventKey = userInfo.getStringExtra("eventKey");
    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng eV = new LatLng(45.495483, -73.577603);
        map.addMarker(new MarkerOptions().position(eV).title("Machine"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(eV, 15));
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
        intent.setClass(Map.this, Map.class);
        startActivity(intent);
    }

    void goToCreateOrder() {
        Intent intent = new Intent();
        intent.putExtra("eventKey", eventKey);
        intent.putExtra("user", user);
        intent.setClass(Map.this, CreateDrink.class);
        startActivity(intent);
    }

    void goToViewPastOrders(){
        Intent intent = new Intent();
        intent.putExtra("eventKey", eventKey);
        intent.putExtra("user", user);
        intent.setClass(Map.this, Orders.class);
        startActivity(intent);
    }

    void goToEventReg(){
        Intent intent = new Intent();
        //intent.putExtra("eventKey", eventKey);
        intent.putExtra("user", user);
        intent.setClass(Map.this, EventRegistration.class);
        startActivity(intent);
    }
};