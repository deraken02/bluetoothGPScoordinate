package com.example.bluetoothgpscoordinate;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    private Button mButtonGetCoordinate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButtonGetCoordinate = (Button) findViewById(R.id.getLocation_button);

        mButtonGetCoordinate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getCoordinate = new Intent(MainActivity.this, locationSender.class);
                startActivity(getCoordinate);
            }
        });
    }
}