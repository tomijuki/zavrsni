package com.jukic.zavrsni;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;


import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity{

    EditText heightEditText;
    EditText weightEditText;
    EditText dateEditText;
    EditText fullnameEditText;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadLocale();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setTitle(R.string.title_activity_settings);

        fullnameEditText = findViewById(R.id.etName);
        heightEditText = findViewById(R.id.etHeight);
        weightEditText = findViewById(R.id.etWeight);
        dateEditText = findViewById(R.id.etDate);


        final SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        heightEditText.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "999")});
        weightEditText.setFilters(new InputFilter[]{ new InputFilterMinMax("0","999")});


        heightEditText.setText(sharedPreferences.getString("height", ""));
        heightEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String string = s.toString();
                editor.putString("height", string);
                editor.apply();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        weightEditText.setText(sharedPreferences.getString("weight", ""));
        weightEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String string = s.toString();
                editor.putString("weight", string);
                editor.apply();
            }
        });

        fullnameEditText.setText(sharedPreferences.getString("name", ""));
        fullnameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String string = s.toString();
                editor.putString("name", string);
                editor.apply();
            }
        });

        dateEditText.setFocusable(false);
        dateEditText.setText(sharedPreferences.getString("date", ""));
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal= Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        SettingsActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                DateFormat dateFormat = DateFormat.getDateInstance();
                Date date = new GregorianCalendar(year,month, dayOfMonth).getTime();
                String dateString = dateFormat.format(date);
                dateEditText.setText(dateString);
                editor.putString("date", dateString);
                editor.apply();
            }
        };


        Switch lanSwitch = findViewById(R.id.languageSwitch);
        final Switch vibrateSwitch = findViewById(R.id.vibrateSwitch);
        SharedPreferences vibratePreferences = getSharedPreferences("Vibrate", MODE_PRIVATE);
        boolean vibrate = vibratePreferences.getBoolean("vibrate", false);
        if(vibrate){
            vibrateSwitch.setChecked(true);
        } else {
            vibrateSwitch.setChecked(false);
        }

        vibrateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    SharedPreferences.Editor vibrateEditor = getSharedPreferences("Vibrate", MODE_PRIVATE).edit();
                    vibrateEditor.putBoolean("vibrate", isChecked);
                    vibrateEditor.apply();
            }
        });

        if (Locale.getDefault().toLanguageTag().equals("hr-HR")){
            lanSwitch.setChecked(true);
        } else {
            lanSwitch.setChecked(false);
        }

        lanSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    setLocale("hr","HR");
                    recreate();
                } else {
                    setLocale("en", "und");
                    recreate();
                }
            }
        });
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

    public void loadLocale(){
        SharedPreferences language_prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = language_prefs.getString("selected_language", "en");
        String area = language_prefs.getString("selected_area", "GB");

        setLocale(language, area);
    }


}
