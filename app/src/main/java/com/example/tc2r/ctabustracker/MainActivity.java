package com.example.tc2r.ctabustracker;

import android.Manifest;
import android.os.Debug;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.mock.MockContentProvider;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class MainActivity extends AppCompatActivity {

    Button btnShowLocation;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;

    GPSTracker mGPSTracker;
    TextView location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            if(ActivityCompat.checkSelfPermission(this, mPermission) != MockPackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{mPermission}, REQUEST_CODE_PERMISSION);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        btnShowLocation = (Button) findViewById(R.id.button);
        btnShowLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGPSTracker = new GPSTracker(MainActivity.this);

                location = (TextView) findViewById(R.id.locationText);


                if(mGPSTracker.canGetLocation()){
                    Log.wtf("TAG", "canGetLocation = " + mGPSTracker.canGetLocation);
                    double latitude = mGPSTracker.getLatitude();
                    Log.wtf("TAG", "latitude = " + latitude);
                    double longitude = mGPSTracker.getLongitude();
                    location.setText(latitude + " " + longitude);
                }else {
                    mGPSTracker.showSettingsAlert();
                }
            }
        });
    }
}
