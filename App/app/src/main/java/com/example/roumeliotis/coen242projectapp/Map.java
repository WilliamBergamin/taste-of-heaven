package com.example.roumeliotis.coen242projectapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
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

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        serverHelper = new ServerHelper();
        Manager = new Manager(this);
        Intent userInfo = getIntent();
        user = userInfo.getParcelableExtra("user");
        eventKey = userInfo.getStringExtra("eventKey");
        backToOrder = findViewById(R.id.returnButton);
/*
        backToOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToCreateDrink();
            }
        });*/
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    void goToCreateDrink() {
        Intent intent = new Intent();
        intent.putExtra("user", user);
        intent.putExtra("eventKey", eventKey);
        intent.setClass(Map.this, CreateDrink.class);
        startActivity(intent);
    }
};