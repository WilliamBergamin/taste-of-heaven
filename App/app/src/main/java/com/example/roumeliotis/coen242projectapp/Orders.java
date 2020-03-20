package com.example.roumeliotis.coen242projectapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

// View all of your orders to gain access to input code
public class Orders extends AppCompatActivity{

    ListView ordersList;
    ServerHelper serverHelper;
    Manager Manager;
    String eventKey;
    User user;
    ImageButton backToOrder;

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
        backToOrder = findViewById(R.id.returnButton);

        ordersList = findViewById(R.id.orderCodesList);

        List<Order> orders = Manager.getOrdersByUserID(user.getid());
        List<String> orderStrings = new ArrayList<String>();
        for (int i = 0; i < orders.size(); i++) {
            Log.d("Orders", orders.get(i).toString());
            orderStrings.add(orders.get(i).toString());
        }

        ordersList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, orderStrings));
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
        intent.setClass(Orders.this, CreateDrink.class);
        startActivity(intent);
    }
}
