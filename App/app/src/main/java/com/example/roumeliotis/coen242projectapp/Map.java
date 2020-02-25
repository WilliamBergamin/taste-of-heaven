package com.example.roumeliotis.coen242projectapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class Map extends AppCompatActivity {

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
        serverHelper = new ServerHelper();
        Manager = new Manager(this);
        Intent userInfo = getIntent();
        user = userInfo.getParcelableExtra("user");
        eventKey = userInfo.getStringExtra("eventKey");
        backToOrder = findViewById(R.id.returnButton);

        backToOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToCreateDrink();
            }
        });
    }


    void goToCreateDrink() {
        Intent intent = new Intent();
        intent.putExtra("user", user);
        intent.putExtra("eventKey", eventKey);
        intent.setClass(Map.this, CreateDrink.class);
        startActivity(intent);
    }
};