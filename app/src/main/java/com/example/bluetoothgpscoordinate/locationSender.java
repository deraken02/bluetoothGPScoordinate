package com.example.bluetoothgpscoordinate;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;

public class locationSender extends AppCompatActivity implements LocationListener {

    protected LocationManager locationManager;
    TextView mTextviewCoordinate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_sender);
        mTextviewCoordinate = (TextView) findViewById(R.id.location);
        mTextviewCoordinate.setText("Obtaining coordinates in progress ... ");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onLocationChanged(Location location) {
        mTextviewCoordinate = (TextView) findViewById(R.id.location);
        mTextviewCoordinate.setText("latitude:" + location.getLatitude() + ", longitude:" + location.getLongitude()+", date:" + location.getTime());
    }
}