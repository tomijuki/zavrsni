package com.jukic.zavrsni.run;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jukic.zavrsni.R;

import java.text.DecimalFormat;
import java.util.Locale;


public class RunInfoActivity extends AppCompatActivity implements OnMapReadyCallback {

    private double startLatitude;
    private double startLongitude;
    private double endLatitude;
    private double endLongitude;
    private long duration;
    private long distance;
    private long pace;
    private int happiness;
    private int id;

    private Run run;
    private int position;

    private TextView tvDuration;
    private TextView tvDistance;
    private TextView tvPace;

    private SeekBar seekBar;

    private LatLng start;
    private LatLng end;
    private MarkerOptions startMark;
    private MarkerOptions endMark;

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;

    public static final String EXTRA_HAPPINESS = "com.jukic.zavsni.run.EXTRA_HAPPINESS";
    public static final String EXTRA_ID = "com.jukic.zavrsni.run.EXTRA_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_info);

        Intent intent = getIntent();
        run = intent.getParcelableExtra(RunListActivity.EXTRA_INFO);

        tvDuration = findViewById(R.id.tvDuration);
        tvDistance = findViewById(R.id.tvDistance);
        tvPace = findViewById(R.id.tvPace);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        id = intent.getIntExtra(EXTRA_ID,-1);

        startLatitude = intent.getDoubleExtra(RunListActivity.EXTRA_START_LAT, 0);
        startLongitude = intent.getDoubleExtra(RunListActivity.EXTRA_START_LON, 0);
        endLatitude = intent.getDoubleExtra(RunListActivity.EXTRA_END_LAT, 0);
        endLongitude = intent.getDoubleExtra(RunListActivity.EXTRA_END_LON, 0);
        duration = run.getDuration();
        distance = run.getDistance();
        pace = run.getPace();
        happiness = run.getHappiness();

        int happy = 0x1F600;
        int sad = 0x1F915;
        TextView tvEmojiOne = findViewById(R.id.tvEmojiOne);
        TextView tvEmojiTwo = findViewById(R.id.tvEmojiTwo);
        tvEmojiOne.setText(getEmojiByUnicode(sad));
        tvEmojiTwo.setText(getEmojiByUnicode(happy));

        position = intent.getIntExtra(RunListActivity.EXTRA_TITLE,0);
        setTitle(getString(R.string.title_activity_run_info)+" #"+(position+1));

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        start = new LatLng(startLatitude, startLongitude);
        end = new LatLng(endLatitude, endLongitude);

        seekBar = findViewById(R.id.seekBar);
        seekBar.setMax(10);
        seekBar.setProgress(happiness);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                happiness = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        endMark = new MarkerOptions().position(new LatLng(endLatitude, endLongitude)).title(getString(R.string.end_location)).icon(BitmapDescriptorFactory.fromResource(R.drawable.end));
        startMark = new MarkerOptions().position(new LatLng(startLatitude, startLongitude)).title(getString(R.string.start_location)).icon(BitmapDescriptorFactory.fromResource(R.drawable.start));

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra(EXTRA_HAPPINESS, happiness);
        data.putExtra(RunActivity.EXTRA_DISTANCE, distance);
        data.putExtra(RunActivity.EXTRA_DURATION, duration);
        data.putExtra(RunActivity.EXTRA_PACE, pace);
        data.putExtra(RunActivity.EXTRA_START_LAT, start.latitude);
        data.putExtra(RunActivity.EXTRA_START_LON, start.longitude);
        data.putExtra(RunActivity.EXTRA_END_LAT, end.latitude);
        data.putExtra(RunActivity.EXTRA_END_LON, end.longitude);
        data.putExtra(EXTRA_ID, id);


        setResult(RESULT_OK, data);
        finish();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }

    private void updateValues() {

        int hours = (int) (duration/1000) / 3600;
        int minutes = (int) (duration/1000) / 60;
        int seconds = (int) (duration/1000) % 60;

        String durationFormatted = String.format(Locale.getDefault(),"%02d:%02d:%02d", hours, minutes, seconds);
        tvDuration.setText(durationFormatted);


        double distanceDouble = (double) distance/1000;
        DecimalFormat df = new DecimalFormat("#####.##");
        tvDistance.setText(df.format(distanceDouble)+" km");


        int s = (int) (pace % 60) ;
        int min = (int) (Math.floor(pace / 60));


        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", min, s);
        tvPace.setText(timeLeftFormatted +" min/km");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        updateValues();

        mMap.addMarker(startMark);
        mMap.addMarker(endMark);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(start);
        builder.include(end);
        LatLngBounds bounds = builder.build();

        int padding = 2;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 512,512, padding);

        googleMap.moveCamera(cu);
    }
}
