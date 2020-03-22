package com.example.roumeliotis.coen242projectapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONObject;

// Sign in to an event, so that all orders are placed at event machines
public class EventRegistration extends AppCompatActivity {

    EditText getEventCode;
    Button getEventButton;
    ServerHelper serverHelper;
    Manager Manager;
    User user;
    String event_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventreg_page);

        getSupportActionBar().setTitle(R.string.app_name);
        serverHelper = new ServerHelper();
        Manager = new Manager(this);
        getEventCode = findViewById(R.id.eventCodeInput);
        getEventButton = findViewById(R.id.eventButton);

        Intent userInfo = getIntent();
        user = userInfo.getParcelableExtra("loggedInUser");
        getEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event_key = getEventCode.getText().toString().trim();
                if(("").equals(event_key)){
                    Toast toast=Toast.makeText(getApplicationContext(),"Invalid input",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                else{
                    serverHelper.postUserToEvent(event_key, user.getToken(), getApplicationContext(),  new VolleyCallback() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            goToNextActivity(event_key, user);
                        }

                        @Override
                        public void onError(VolleyError error) {
                            LayoutInflater inflater = getLayoutInflater();
                            View layoutToast = inflater.inflate(R.layout.toast,
                                    (ViewGroup) findViewById(R.id.toast_layout));
                            TextView textToast = layoutToast.findViewById(R.id.toast_text);
                            textToast.setText("Bad event token");
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
    public void onBackPressed(){
        moveTaskToBack(true);
    }

    void goToNextActivity(String event, User user) {
        Intent intent = new Intent();
        intent.putExtra("eventKey", event);
        intent.putExtra("user", user);
        intent.setClass(EventRegistration.this, LandingPage.class);
        startActivity(intent);
    }
}
