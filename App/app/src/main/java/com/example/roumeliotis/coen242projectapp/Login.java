package com.example.roumeliotis.coen242projectapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.RequestFuture;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


// Log in to your account
public class Login extends AppCompatActivity{

    //TODO reset the orders with the ones pulled from the server on login

    EditText getEmail;
    EditText getPassword;
    TextView getLoginPrompt;
    TextView getEmailPrompt;
    TextView getPasswordPrompt;
    TextView getSignUpPrompt;
    Button getLogin;
    Button getSignup;
    ImageView getLoadImage;
    ServerHelper serverHelper;
    Manager manager;
    public static final String TAG = "Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serverHelper = new ServerHelper();
        manager = new Manager(this);
        SharedPreferences prefs = getSharedPreferences("preferences", MODE_PRIVATE);

        setContentView(R.layout.login_page);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.app_name);

        getEmail = findViewById(R.id.emailInput);
        getPassword = findViewById(R.id.passwordInput);
        getLoginPrompt = findViewById(R.id.logintoyouraccount);
        getEmailPrompt = findViewById(R.id.email);
        getPasswordPrompt = findViewById(R.id.password);
        getSignUpPrompt = findViewById(R.id.donthaveanaccount);
        getLogin = findViewById(R.id.loginButton);
        getSignup = findViewById(R.id.signup);
        getLoadImage = findViewById(R.id.loadImage);

        getSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSignup();
            }
        });

        getLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = getEmail.getText().toString().trim();
                String password = getPassword.getText().toString().trim();
                if(("").equals(email) || ("").equals(password)){
                    Toast toast=Toast.makeText(getApplicationContext(),"Invalid input",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else{
                    // login in here (serverhelper.java)
                    serverHelper.getToken(email, password, getApplicationContext(),  new VolleyCallback() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            try {
                                User loggedInUser = new User(-1,
                                        response.getString("name"),
                                        response.getString("email"),
                                        "password",
                                        response.getString("token"));
                                loggedInUser.setid(manager.insertUser(loggedInUser));
                                SharedPreferences prefs = getSharedPreferences("preferences", MODE_PRIVATE);
                                prefs.edit().putString("token", response.getString("token")).commit();
                                fillDrinkTable(loggedInUser);
                                goToNextActivity(loggedInUser);
                            } catch(JSONException e){
                                e.printStackTrace();
                                Toast toast=Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT);
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
                            textToast.setText("Invalid Email or Password");
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

        //Attempt to login using the saved token
        if(prefs.getString("token",null) != null){
            serverHelper.getUser(prefs.getString("token",null),getApplicationContext(), new VolleyCallback(){
                @Override
                public void onSuccess(JSONObject response) {
                    SharedPreferences prefs = getSharedPreferences("preferences", MODE_PRIVATE);
                    String token = prefs.getString("token",null);
                    try {
                        User loggedInUser = new User(manager.getUserIdFromToken(token),
                                response.getString("name"),
                                response.getString("email"),
                                "password",
                                response.getString("token"));
                        fillDrinkTable(loggedInUser);
                        goToNextActivity(loggedInUser);
                    }
                    catch (JSONException error){    //Will hit here if the JSON response was an error
                        Log.e(TAG, error.toString());
                        loadPage();
                        Toast toast = Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        prefs.edit().remove("token").commit();
                    }
                }

                @Override
                public void onError(VolleyError error) {
                    Log.e(TAG, error.toString());
                    loadPage();
                    Toast toast = Toast.makeText(getApplicationContext(), "Session Error, logging out", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    SharedPreferences prefs = getSharedPreferences("preferences", MODE_PRIVATE);
                    prefs.edit().remove("token").commit();
                }
            });
        }
        else loadPage();
    }

    void loadPage() {
        getLoadImage.setVisibility(View.GONE);
        getEmail.setVisibility(View.VISIBLE);
        getPassword.setVisibility(View.VISIBLE);
        getLoginPrompt.setVisibility(View.VISIBLE);
        getEmailPrompt.setVisibility(View.VISIBLE);
        getPasswordPrompt.setVisibility(View.VISIBLE);
        getSignUpPrompt.setVisibility(View.VISIBLE);
        getLogin.setVisibility(View.VISIBLE);
        getSignup.setVisibility(View.VISIBLE);
    }

    void goToSignup() {
        Intent intent = new Intent();
        intent.setClass(Login.this, Signup.class);
        startActivity(intent);
    }

    void goToNextActivity(User loggedInUser) {
        Intent intent = new Intent();
        intent.putExtra("loggedInUser", loggedInUser);
        intent.setClass(Login.this, EventRegistration.class);
        startActivity(intent);
    }

    void fillDrinkTable(final User user){
        Log.d(TAG, "fillDrinkTable");
        serverHelper.getOrders(user, getApplicationContext(), new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    manager.clearDrinks();
                    JSONArray orders = response.getJSONArray("orders");
                    for(int i = 0; i < orders.length(); i++){
                        JSONObject order = orders.getJSONObject(i);
                        manager.insertOrder(new Order(
                                -1,
                                user.getid(),
                                order.getString("order_key"),
                                "",
                                order.getJSONArray("drinks").getJSONObject(0).getString("mixer_type"),
                                order.getJSONArray("drinks").getJSONObject(0).getString("alcohol_type"),
                                order.getJSONArray("drinks").getJSONObject(0).getBoolean("double"),
                                order.getDouble("price"),
                                order.getString("state"),
                                order.getBoolean("payed")
                        ));
                    }
                }
                catch (JSONException e){
                    Log.e(TAG, e.toString());
                    Toast toast = Toast.makeText(getApplicationContext(), "Error, past orders may incorrect", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }

            @Override
            public void onError(VolleyError error) {
                Log.e(TAG, error.toString());
                Toast toast = Toast.makeText(getApplicationContext(), "Error, past orders may be out of date", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
    }
}
