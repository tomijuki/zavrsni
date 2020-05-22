package com.jukic.zavrsni.run;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.jukic.zavrsni.R;

import java.text.DecimalFormat;
import java.util.Locale;

public class RunActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private Context activity;

    private Button tapButton;
    private Button tapButtonFinish;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private boolean running;
    private boolean ready;
    private boolean paused;
    private Handler handler;
    private Runnable runnable;


    public static final String EXTRA_DISTANCE = "com.jukic.zavrsni.run.EXTRA_DISTANCE";
    public static final String EXTRA_DURATION = "com.jukic.zavrsni.run.EXTRA_DURATION";
    public static final String EXTRA_PACE = "com.jukic.zavrsni.run.EXTRA_PACE";
    public static final String EXTRA_START_LAT = "com.jukic.zavrsni.run.EXTRA_START_LAT";
    public static final String EXTRA_START_LON = "com.jukic.zavrsni.run.EXTRA_START_LON";
    public static final String EXTRA_END_LAT = "com.jukic.zavrsni.run.EXTRA_END_LAT";
    public static final String EXTRA_END_LON = "com.jukic.zavrsni.run.EXTRA_END_LON";


    private Location currentLocation;
    private MarkerOptions startMark;
    private MarkerOptions endMark;
    private LatLng start;
    private LatLng end;

    private PolylineOptions polylineOptions;
    private Polyline currentPolyline;

    private float distance;
    private long distanceLong;
    private long duration;
    private long pauseOffset;
    private long pace;

    private TextView distanceTextView;
    private Chronometer chronometer;
    private TextView paceTextView;

    private ConstraintLayout constraintLayout;
    private ConstraintSet constraintSet = new ConstraintSet();

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if (ContextCompat.checkSelfPermission(RunActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            }
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        setTitle(R.string.title_activity_run);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        activity = this;
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        polylineOptions = new PolylineOptions();

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        constraintLayout = findViewById(R.id.constraintRun);
        constraintSet.clone(constraintLayout);

        duration = 0;
        distance = 0;
        distanceLong = 0;
        pace = 0;

        running = false;
        paused = false;
        ready = false;

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (running){
                    tapButton.setBackgroundColor(Color.GRAY);
                    tapButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            check();
                        }
                    });
                }
            }
        };

        distanceTextView = findViewById(R.id.diTextView);
        chronometer = findViewById(R.id.chronometer);
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                duration = SystemClock.elapsedRealtime() - chronometer.getBase();
            }
        });
        paceTextView = findViewById(R.id.paTextView);


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if(ready && !paused){
                    updateMarker(running, location);
                    updateMap(running);
                    updateValues(running, location);
                }
                currentLocation = location;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };





        tapButton = (Button) findViewById(R.id.tapButton);
        tapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                        if(currentLocation!= null){
                            start = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                            startRunning();
                        } else {
                            Toast.makeText(activity,getString(R.string.run_toast), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        LocationAlertDialog();
                    }
            }
        });

        tapButtonFinish = (Button) findViewById(R.id.tapButtonFinish);
        tapButtonFinish.setBackgroundColor(Color.CYAN);
        tapButtonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishRunning();
            }
        });

        if (!running && !paused){
            tapButton.setText(R.string.start);
            tapButton.setBackgroundColor(Color.GREEN);
            constraintSet.connect(R.id.tapButton, ConstraintSet.END, constraintLayout.getId(), ConstraintSet.END);
            constraintSet.connect(R.id.tapButtonFinish, ConstraintSet.START, constraintLayout.getId(), ConstraintSet.END);
            constraintSet.applyTo(constraintLayout);
        }

        if(savedInstanceState == null){
            mapFragment.setRetainInstance(true);
        }

        if (ContextCompat.checkSelfPermission(RunActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(RunActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, locationListener);
            currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (currentLocation==null){
                currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        }
    }

    private void updateValues(boolean running, Location location) {
        if(running){
            distance += currentLocation.distanceTo(location);
            DecimalFormat df = new DecimalFormat("##.##");
            distanceTextView.setText(df.format(distance/1000)+" km");

            float durInS = (float) duration/1000;
            float disInKm = distance/1000;
            pace = Math.round(durInS/disInKm);

            int s = (int) (pace % 60) ;
            int min = (int) (Math.floor(pace / 60));
            if (min>999){
                min = 999;
            }


            String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", min, s);
            paceTextView.setText(timeLeftFormatted +" min/km");
        }
    }

    private void finishRunning(){
        if (end == null){
            end = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        }

        distanceLong = Math.round(distance);

        Intent data = new Intent();
        data.putExtra(EXTRA_DISTANCE, distanceLong);
        data.putExtra(EXTRA_DURATION, duration);
        data.putExtra(EXTRA_PACE, pace);
        data.putExtra(EXTRA_START_LAT, start.latitude);
        data.putExtra(EXTRA_START_LON, start.longitude);
        data.putExtra(EXTRA_END_LAT, end.latitude);
        data.putExtra(EXTRA_END_LON, end.longitude);

        setResult(RESULT_OK, data);
        finish();
    }

    private void startRunning() {
        if (!paused){
            polylineOptions.add(start);
            distance = 0;
        }
        startChronometer();

        tapButton.setText(R.string.pause);
        tapButton.setBackgroundColor(Color.GRAY);
        constraintSet.connect(R.id.tapButton, ConstraintSet.END, constraintLayout.getId(), ConstraintSet.END);
        constraintSet.connect(R.id.tapButtonFinish, ConstraintSet.START, constraintLayout.getId(), ConstraintSet.END);
        constraintSet.applyTo(constraintLayout);
        running = true;
        paused = false;
    }

    private void pauseRunning(){
        if (running){
            handler.removeCallbacks(runnable);
            pauseChronometer();
            paused = true;
            running = false;

            tapButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startRunning();
                }
            });
            tapButton.setText(R.string.resume);
            tapButton.setBackgroundColor(Color.YELLOW);
            constraintSet.connect(R.id.tapButton, ConstraintSet.END, R.id.tapButtonFinish, ConstraintSet.START);
            constraintSet.connect(R.id.tapButtonFinish, ConstraintSet.START, R.id.tapButton, ConstraintSet.END);
            constraintSet.applyTo(constraintLayout);
        }
    }

    private void check(){
        tapButton.setBackgroundColor(Color.YELLOW);
        tapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseRunning();
            }
        });
        handler.postDelayed(runnable, 5000);
    }

    private  void startChronometer(){
        if (!running){
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
        }
        tapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });
    }

    private  void pauseChronometer(){
        if (running){
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
        }
    }

    private void resetChronometer(){
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }



    public void updateMarker(boolean run, Location location){
        mMap.clear();
        if(run){
            end = new LatLng(location.getLatitude(), location.getLongitude());
            endMark = new MarkerOptions().position(end).title(getString(R.string.current_location)).icon(BitmapDescriptorFactory.fromResource(R.drawable.end));
            startMark = new MarkerOptions().position(start).title(getString(R.string.start_location)).icon(BitmapDescriptorFactory.fromResource(R.drawable.start));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(end));
        } else {
            start = new LatLng(location.getLatitude(), location.getLongitude());
            startMark = new MarkerOptions().position(start).title(getString(R.string.current_location)).icon(BitmapDescriptorFactory.fromResource(R.drawable.start));
        }
    }

    public void updateMap(boolean run){
        if (run){
            mMap.addMarker(startMark);
            mMap.addMarker(endMark);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(end,16));
            polylineOptions.add(end);
            mMap.addPolyline(polylineOptions);
        } else {
            mMap.addMarker(startMark);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start,16));
        }
    }

    public void LocationAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle(R.string.location_alert);
        alertDialog.setMessage(R.string.gps_network_not_enabled);
        alertDialog.setPositiveButton(R.string.title_activity_settings,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        activity.startActivity(intent);
                        onBackPressed();
                    }
                });
        alertDialog.setNegativeButton(android.R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //tryLocating = false;
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setZoomGesturesEnabled(false);
        ready = true;

        if (ContextCompat.checkSelfPermission(RunActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(RunActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, locationListener);
            currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (currentLocation != null){
                updateMarker(false, currentLocation);
                updateMap(false);
            } else {
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                    LocationAlertDialog();
                }
            }
        }
    }
}
