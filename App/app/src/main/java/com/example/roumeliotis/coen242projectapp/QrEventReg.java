package com.example.roumeliotis.coen242projectapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import org.json.JSONObject;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import static android.Manifest.permission.CAMERA;


public class QrEventReg extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView mScannerView;
    private User user;
    private ServerHelper serverHelper;

    private final String TAG = "QrEventReg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "OnCreate");

        Intent userInfo = getIntent();
        user = userInfo.getParcelableExtra("user");
        serverHelper = new ServerHelper();

        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);

        int currentApiVerson = android.os.Build.VERSION.SDK_INT;
        if (currentApiVerson >= android.os.Build.VERSION_CODES.M){
            if (!checkPermission()){
                requestPermission();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "OnResume");

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (checkPermission()) {
                if(mScannerView == null) {
                    mScannerView = new ZXingScannerView(this);
                    setContentView(mScannerView);
                }
                mScannerView.setResultHandler(this);
                mScannerView.startCamera();
            } else {
                requestPermission();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "OnPause");
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        final String result = rawResult.getText();
        Log.d(TAG, rawResult.getText());
        Log.d(TAG, rawResult.getBarcodeFormat().toString());

        if(BarcodeFormat.QR_CODE != rawResult.getBarcodeFormat()){
            mScannerView.resumeCameraPreview(QrEventReg.this);
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scanned Event Code");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                serverHelper.postUserToEvent(result, user.getToken(), getApplicationContext(),  new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        goToNextActivity(result, user);
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
                        mScannerView.resumeCameraPreview(QrEventReg.this);
                    }
                });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mScannerView.resumeCameraPreview(QrEventReg.this);
            }
        });
        builder.setMessage(rawResult.getText());
        AlertDialog alert1 = builder.create();
        alert1.show();
    }

    private boolean checkPermission(){
        return ( ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA ) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (!cameraAccepted){
                        Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access and camera", Toast.LENGTH_LONG).show();
                        goToEventReg();
                    }
                }
                break;
        }
    }

    void goToNextActivity(String event, User user) {
        Intent intent = new Intent();
        intent.putExtra("eventKey", event);
        intent.putExtra("user", user);
        intent.setClass(QrEventReg.this, CreateDrink.class);
        startActivity(intent);
    }

    private void goToEventReg(){
        Intent intent = new Intent();
        intent.putExtra("loggedInUser", user);
        intent.setClass(QrEventReg.this, EventRegistration.class);
        startActivity(intent);
    }
}
