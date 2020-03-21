//package com.example.roumeliotis.coen242projectapp;
//
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
//import android.view.View;
//
//import com.google.zxing.Result;
//import me.dm7.barcodescanner.zxing.ZXingScannerView;
//import static android.Manifest.permission.CAMERA;
//
//
//public class QrEventReg extends AppCompatActivity implements ZXingScannerView.ResultHandler{
//
//    private static final int REQUEST_CAMERA = 1;
//    private ZXingScannerView mScannerView;
//
//    private final String TAG = "QrEventReg";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Log.d(TAG, "OnCreate");
//
//        mScannerView = new ZXingScannerView(this);
//        setContentView(mScannerView);
//
//        int currentApiVerson = android.os.Build.VERSION.SDK_INT;
//        if (currentApiVerson >= android.os.Build.VERSION_CODES.M){
//            if (!checkPermission()){
//                requestPermission();
//            }
//        }
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//    }
//
//    private boolean checkPermission(){
//        return ( ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA ) == PackageManager.PERMISSION_GRANTED);
//    }
//
//    private void requestPermission(){
//        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
//    }
//}
