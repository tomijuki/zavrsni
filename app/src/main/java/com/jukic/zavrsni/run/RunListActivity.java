package com.jukic.zavrsni.run;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jukic.zavrsni.ConfirmRunDialog;
import com.jukic.zavrsni.R;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

public class RunListActivity extends AppCompatActivity implements ConfirmRunDialog.ConfirmRunDialogListener {

    private RunViewModel runViewModel;

    public static final String EXTRA_INFO = "com.jukic.zavrsni.run.EXTRA_INFO";
    public static final String EXTRA_TITLE = "com.jukic.zavrsni.run.EXTRA_TITLE";
    public static final String EXTRA_START_LAT = "com.jukic.zavrsni.run.EXTRA_START_LAT";
    public static final String EXTRA_START_LON = "com.jukic.zavrsni.run.EXTRA_START_LON";
    public static final String EXTRA_END_LAT = "com.jukic.zavrsni.run.EXTRA_END_LAT";
    public static final String EXTRA_END_LON = "com.jukic.zavrsni.run.EXTRA_END_LON";

    public static final int ADD_RUN_REQUEST = 1;
    public static final int EDIT_RUN_REQUEST = 2;

    public static Run delete;

    public void openConfirmDialog(){
        ConfirmRunDialog dialog = new ConfirmRunDialog();
        dialog.show(getSupportFragmentManager(), "confirm dialog");
    }

    @Override
    public void onConfirmed() {
        runViewModel.delete(delete);
        Toast.makeText(RunListActivity.this, R.string.run_delete, Toast.LENGTH_SHORT).show();
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if (ContextCompat.checkSelfPermission(RunListActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(RunListActivity.this, RunActivity.class);
                startActivityForResult(intent, ADD_RUN_REQUEST);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_list);

        setTitle(R.string.title_activity_run_list);

        FloatingActionButton fab = findViewById(R.id.fabRun);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission( RunListActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(RunListActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                } else {
                    Intent intent = new Intent(RunListActivity.this, RunActivity.class);
                    startActivityForResult(intent, ADD_RUN_REQUEST);
                }


            }
        });



        RecyclerView recyclerView = findViewById(R.id.runRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final RunAdapter adapter = new RunAdapter();
        recyclerView.setAdapter(adapter);

        runViewModel = new ViewModelProvider(this).get(RunViewModel.class);
        runViewModel.getAllRuns().observe(this, new Observer<List<Run>>() {
            @Override
            public void onChanged(List<Run> runs) {
                //update RecyclerView
                adapter.submitList(runs);
            }
        });

        adapter.setOnItemClickListener(new RunAdapter.OnItemCLickListener() {
            @Override
            public void onItemClick(Run run, int position) {


                Intent intent = new Intent(RunListActivity.this, RunInfoActivity.class);
                intent.putExtra(RunInfoActivity.EXTRA_ID, run.getId());
                intent.putExtra(EXTRA_INFO, run);
                intent.putExtra(EXTRA_TITLE, position);
                intent.putExtra(EXTRA_START_LAT, run.getStartLatitude());
                intent.putExtra(EXTRA_START_LON, run.getStartLongitude());
                intent.putExtra(EXTRA_END_LAT, run.getEndLatitude());
                intent.putExtra(EXTRA_END_LON, run.getEndLongitude());
                startActivityForResult(intent, EDIT_RUN_REQUEST);

            }
        });

        adapter.setOnItemLongClickListener(new RunAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(Run run) {
                delete = run;
                openConfirmDialog();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_RUN_REQUEST && resultCode == RESULT_OK){
            long duration = data.getLongExtra(RunActivity.EXTRA_DURATION, 0);
            long distance = data.getLongExtra(RunActivity.EXTRA_DISTANCE, 0);
            long pace = data.getLongExtra(RunActivity.EXTRA_PACE, 0);
            double startLon = data.getDoubleExtra(RunActivity.EXTRA_START_LON, 0);
            double startLat = data.getDoubleExtra(RunActivity.EXTRA_START_LAT, 0);
            double endLon = data.getDoubleExtra(RunActivity.EXTRA_END_LON, 0);
            double endLat = data.getDoubleExtra(RunActivity.EXTRA_END_LAT, 0);

            DecimalFormat df = new DecimalFormat("###.########", DecimalFormatSymbols.getInstance(Locale.US));
            df.setRoundingMode(RoundingMode.CEILING);

            double startLongitude = Double.parseDouble(df.format(startLon));
            double startLatitude = Double.parseDouble(df.format(startLat));
            double endLongitude = Double.parseDouble(df.format(endLon));
            double endLatitude = Double.parseDouble(df.format(endLat));


            Run run = new Run(startLatitude,startLongitude,endLatitude,endLongitude,duration,distance,pace,7);
            runViewModel.insert(run);

            Toast.makeText(this, R.string.finish_alert, Toast.LENGTH_SHORT).show();

        } else if (requestCode == EDIT_RUN_REQUEST && resultCode == RESULT_OK) {

            int id = data.getIntExtra(RunInfoActivity.EXTRA_ID, -1);
            if (id == -1){
                Toast.makeText(this,"Error when saving the run.", Toast.LENGTH_SHORT).show();
                return;
            }

            long duration = data.getLongExtra(RunActivity.EXTRA_DURATION, 0);
            long distance = data.getLongExtra(RunActivity.EXTRA_DISTANCE, 0);
            long pace = data.getLongExtra(RunActivity.EXTRA_PACE, 0);
            double startLon = data.getDoubleExtra(RunActivity.EXTRA_START_LON, 0);
            double startLat = data.getDoubleExtra(RunActivity.EXTRA_START_LAT, 0);
            double endLon = data.getDoubleExtra(RunActivity.EXTRA_END_LON, 0);
            double endLat = data.getDoubleExtra(RunActivity.EXTRA_END_LAT, 0);
            int happiness = data.getIntExtra(RunInfoActivity.EXTRA_HAPPINESS,5);

            Run run = new Run(startLat,startLon,endLat,endLon,duration,distance,pace,happiness);
            run.setId(id);
            runViewModel.update(run);
        }else{

            Toast.makeText(this, R.string.run_save_error, Toast.LENGTH_SHORT).show();
        }
    }
}
