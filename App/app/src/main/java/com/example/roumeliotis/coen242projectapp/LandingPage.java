package com.example.roumeliotis.coen242projectapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;



// Create a custom drink and add it to your cart
public class LandingPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    //Activity implements OnItemSelectedListener {

    ServerHelper serverHelper;
    Manager Manager;
    Button createDrink;
    Button viewMap;
    Button viewPastOrders;
    String eventKey;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_page);

        getSupportActionBar().setTitle(R.string.app_name);
        serverHelper = new ServerHelper();
        Manager = new Manager(this);
        Intent userInfo = getIntent();
        user = userInfo.getParcelableExtra("user");
        eventKey = userInfo.getStringExtra("eventKey");


        viewMap = findViewById(R.id.viewMap2);
        viewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMap();
            }
        });

        createDrink = findViewById(R.id.createDrink);
        createDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToCreateOrder();
            }
        });

        viewPastOrders = findViewById(R.id.viewOrders);
        viewPastOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToViewPastOrders();
            }
        });

    }

    void goToMap() {
        Intent intent = new Intent();
        intent.putExtra("user", user);
        intent.putExtra("eventKey", eventKey);
        intent.setClass(LandingPage.this, Map.class);
        startActivity(intent);
    }

    void goToCreateOrder() {
        Intent intent = new Intent();
        intent.putExtra("eventKey", eventKey);
        intent.putExtra("user", user);
        intent.setClass(LandingPage.this, CreateDrink.class);
        startActivity(intent);
    }

    void goToViewPastOrders(){
        Intent intent = new Intent();
        intent.putExtra("eventKey", eventKey);
        intent.putExtra("user", user);
        intent.setClass(LandingPage.this, Orders.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // TODO Auto-generated method stub
        //Toast.makeText(this, "YOUR SELECTION IS : " + parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub

    }


}