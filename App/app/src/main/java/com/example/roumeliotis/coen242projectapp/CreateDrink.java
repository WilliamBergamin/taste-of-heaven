package com.example.roumeliotis.coen242projectapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.os.Bundle;
import android.widget.AdapterView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CheckBox;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

// Create a custom drink and add it to your cart
public class CreateDrink extends AppCompatActivity implements OnItemSelectedListener{
        //Activity implements OnItemSelectedListener {

    private Spinner spinnerAlcohol;
    private Spinner spinnerMixer;
    ServerHelper serverHelper;
    Manager Manager;
    Button payNowButton;
    String eventKey;
    User user;
    final String TAG = "CreateDrink";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createdrink_page);

        getSupportActionBar().setTitle(R.string.app_name);
        serverHelper = new ServerHelper();
        Manager = new Manager(this);
        Intent userInfo = getIntent();
        user = userInfo.getParcelableExtra("user");
        eventKey = userInfo.getStringExtra("eventKey");

        // Alcohol dropdown menu
        spinnerAlcohol = (Spinner) findViewById(R.id.alcoholDropdown);
        spinnerAlcohol.setOnItemSelectedListener(this);
        List<String> alcohol = new ArrayList<String>();
        alcohol.add("None");
        alcohol.add("Rum");
        alcohol.add("Vodka");
        alcohol.add("Gin");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, alcohol);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAlcohol.setAdapter(adapter);

        // Mixer dropdown menu
        spinnerMixer = (Spinner) findViewById(R.id.mixerDropdown);
        spinnerMixer.setOnItemSelectedListener(this);
        List<String> mixer = new ArrayList<String>();
        mixer.add("None");
        mixer.add("Orange juice");
        mixer.add("Apple juice");
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mixer);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMixer.setAdapter(adapter2);

        // See if user selected double drink
        final CheckBox checkBox = (CheckBox) findViewById(R.id.doubleCheckbox);

        // Create the official drink and add the order to the cart
        payNowButton = findViewById(R.id.payNowButton);
        payNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean is_double = false;
                if (checkBox.isChecked()) {
                    is_double = true;
                }
                String alcohol = spinnerAlcohol.getSelectedItem().toString();
                String mixer = spinnerMixer.getSelectedItem().toString();
                if(("").equals(alcohol) || ("").equals(mixer) || (("None").equals(alcohol) && ("None").equals(mixer))){
                    // Error message here for empty field
                    Toast toast=Toast.makeText(getApplicationContext(),"Invalid input",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                else {
                    // Add order to the cart
                    serverHelper.postNewOrder(user.getToken(), eventKey, mixer, alcohol, is_double, getApplicationContext(), new VolleyCallback() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            try {
                                Order newOrder = new Order(-1,
                                        user.getid(),
                                        response.getString("order_key"),
                                        "",
                                        response.getJSONArray("drinks").getJSONObject(0).getString("mixer_type"),
                                        response.getJSONArray("drinks").getJSONObject(0).getString("alcohol_type"),
                                        response.getJSONArray("drinks").getJSONObject(0).getBoolean("double"),
                                        response.getDouble("price"),
                                        response.getString("state"),
                                        response.getBoolean("payed"));
                                Manager.insertOrder(newOrder);
                                goToNextActivity();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast toast = Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                        }

                        @Override
                        public void onError(VolleyError error) {
                            LayoutInflater inflater = getLayoutInflater();
                            View layoutToast = inflater.inflate(R.layout.toast,
                                    (ViewGroup) findViewById(R.id.toast_layout));
                            TextView textToast = layoutToast.findViewById(R.id.toast_text);
                            textToast.setText("Order not completed");
                            Toast toastWrongName = new Toast(getApplicationContext());
                            toastWrongName.setGravity(Gravity.CENTER, 0, 0);
                            toastWrongName.setDuration(Toast.LENGTH_SHORT);
                            toastWrongName.setView(layoutToast);
                            toastWrongName.show();
                        }
                    });
                }
            }
        });
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

    void goToNextActivity() {
        Intent intent = new Intent();
        intent.putExtra("eventKey", eventKey);
        intent.putExtra("user", user);
        intent.setClass(CreateDrink.this, Orders.class);
        startActivity(intent);
    }

    void goToMap() {
        Intent intent = new Intent();
        intent.putExtra("user", user);
        intent.putExtra("eventKey", eventKey);
        intent.setClass(CreateDrink.this, Map.class);
        startActivity(intent);
    }

    void goToCreateOrder() {
        Intent intent = new Intent();
        intent.putExtra("eventKey", eventKey);
        intent.putExtra("user", user);
        intent.setClass(CreateDrink.this, CreateDrink.class);
        startActivity(intent);
    }

    void goToViewPastOrders(){
        Intent intent = new Intent();
        intent.putExtra("eventKey", eventKey);
        intent.putExtra("user", user);
        intent.setClass(CreateDrink.this, Orders.class);
        startActivity(intent);
    }

    void goToEventReg(){
        Intent intent = new Intent();
        //intent.putExtra("eventKey", eventKey);
        intent.putExtra("user", user);
        intent.setClass(CreateDrink.this, EventRegistration.class);
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