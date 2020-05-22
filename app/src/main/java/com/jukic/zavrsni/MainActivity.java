package com.jukic.zavrsni;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jukic.zavrsni.exercise.ExerciseListActivity;
import com.jukic.zavrsni.run.RunListActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Intent intentRun;
    Intent intentExercise;
    Intent intentSettings;

    @Override
    protected void onRestart() {
        recreate();
        super.onRestart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadLocale();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intentSettings = new Intent(this, SettingsActivity.class);
        TextView settingsTextView = (TextView) findViewById(R.id.settingsTextView);
        settingsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentSettings);
            }
        });

        intentRun = new Intent(this, RunListActivity.class);
        TextView runTextView = (TextView) findViewById(R.id.runTextView);
        runTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentRun);
            }
        });

        intentExercise = new Intent(this, ExerciseListActivity.class);
        TextView exerciseTextView = (TextView) findViewById(R.id.exerciseTextView);
        exerciseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentExercise);
            }
        });

    }

    public void loadLocale(){
        SharedPreferences language_prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = language_prefs.getString("selected_language", "en");
        String area = language_prefs.getString("selected_area", "GB");

        setLocale(language, area);
    }

    private void setLocale(String lang, String area){
        Locale locale = new Locale(lang, area);
        Locale.setDefault(locale);;
        Configuration config = new Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        //save to sharedpref
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("selected_language", lang);
        editor.putString("selected_area", area);
        editor.apply();
    }
}
