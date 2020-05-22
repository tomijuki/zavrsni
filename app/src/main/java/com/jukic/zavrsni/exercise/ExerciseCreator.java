package com.jukic.zavrsni.exercise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;


import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jukic.zavrsni.InputFilterMinMax;
import com.jukic.zavrsni.R;
import com.jukic.zavrsni.SimpleTimePickerDialog;

public class ExerciseCreator extends AppCompatActivity implements SimpleTimePickerDialog.SimpleTimePickerDialogListener {

    public static final String EXTRA_TYPE = "com.jukic.zavrsni.EXTRA_TYPE";
    public static final String EXTRA_TITLE = "com.jukic.zavrsni.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.jukic.zavrsni.EXTRA_DESCRIPTION";
    public static final String EXTRA_SETS = "com.jukic.zavrsni.EXTRA_SETS";
    public static final String EXTRA_SETTIME = "com.jukic.zavrsni.EXTRA_SETTIME";
    public static final String EXTRA_REPS = "com.jukic.zavrsni.EXTRA_REPS";
    public static final String EXTRA_WEIGHT = "com.jukic.zavrsni.EXTRA_WEIGHT";
    public static final String EXTRA_PAUSETIME = "com.jukic.zavrsni.EXTRA_PAUSETIME";

    ConstraintLayout constraintLayout;
    private ConstraintSet constraintSet = new ConstraintSet();
    Spinner typeSpinner;

    int setTime;
    int pauseTime;

    EditText titleEditText;
    EditText setsEditText;
    EditText weightEditText;
    EditText repsEditText;
    EditText setTimeEditText;
    EditText pauseTimeEditText;
    EditText descEditText;


    TextView kgTextView;
    TextView setsTextView;
    TextView weightTextView;
    TextView repsTextView;
    TextView setTimeTextView;
    TextView pauseTimeTextView;
    TextView descTextView;



    public void openDialog(View v){
        SimpleTimePickerDialog simpleTimePickerDialog = new SimpleTimePickerDialog(v);
        simpleTimePickerDialog.show(getSupportFragmentManager(),"simple time picker dialog");
    }

    public void enduranceSelected(){
        repsTextView.setVisibility(View.GONE);
        repsEditText.setText(null);
        repsEditText.setVisibility(View.GONE);

        setsTextView.setVisibility(View.VISIBLE);
        setsEditText.setText(null);
        setsEditText.setVisibility(View.VISIBLE);

        weightTextView.setVisibility(View.GONE);
        kgTextView.setVisibility(View.GONE);
        weightEditText.setText(null);
        weightEditText.setVisibility(View.GONE);

        setTimeTextView.setVisibility(View.VISIBLE);
        setTimeEditText.setText("00:00");
        setTimeEditText.setVisibility(View.VISIBLE);

        pauseTimeTextView.setVisibility(View.VISIBLE);
        pauseTimeEditText.setText("00:00");
        pauseTimeEditText.setVisibility(View.VISIBLE);

        descTextView.setVisibility(View.VISIBLE);
        descEditText.setText(null);
        descEditText.setVisibility(View.VISIBLE);


        constraintSet.clone(constraintLayout);
        constraintSet.connect(R.id.setsEditText,ConstraintSet.TOP,R.id.typeSpinner,ConstraintSet.BOTTOM,8);
        constraintSet.connect(R.id.setTimeEditText,ConstraintSet.TOP,R.id.setsEditText,ConstraintSet.BOTTOM,8);
        constraintSet.connect(R.id.pauseTimeEditText,ConstraintSet.TOP,R.id.setTimeEditText,ConstraintSet.BOTTOM,8);
        constraintSet.applyTo(constraintLayout);
    }
    public void bodyweightSelected(){
        repsTextView.setVisibility(View.VISIBLE);
        repsEditText.setText("");
        repsEditText.setVisibility(View.VISIBLE);

        setsTextView.setVisibility(View.VISIBLE);
        setsEditText.setText("");
        setsEditText.setVisibility(View.VISIBLE);

        weightTextView.setVisibility(View.GONE);
        kgTextView.setVisibility(View.GONE);
        weightEditText.setText("");
        weightEditText.setVisibility(View.GONE);

        setTimeTextView.setVisibility(View.GONE);
        setTimeEditText.setText("00:00");
        setTimeEditText.setVisibility(View.GONE);

        pauseTimeTextView.setVisibility(View.VISIBLE);
        pauseTimeEditText.setText("00:00");
        pauseTimeEditText.setVisibility(View.VISIBLE);

        descTextView.setVisibility(View.VISIBLE);
        descEditText.setText("");
        descEditText.setVisibility(View.VISIBLE);


        constraintSet.clone(constraintLayout);
        constraintSet.connect(R.id.repsEditText,ConstraintSet.TOP,R.id.typeSpinner,ConstraintSet.BOTTOM,8);
        constraintSet.connect(R.id.setsEditText,ConstraintSet.TOP,R.id.repsEditText,ConstraintSet.BOTTOM,8);
        constraintSet.connect(R.id.pauseTimeEditText,ConstraintSet.TOP,R.id.setsEditText,ConstraintSet.BOTTOM,8);
        constraintSet.applyTo(constraintLayout);

    }
    public void weightsSelected(){
        repsTextView.setVisibility(View.VISIBLE);
        repsEditText.setText(null);
        repsEditText.setVisibility(View.VISIBLE);

        setsTextView.setVisibility(View.VISIBLE);
        setsEditText.setText(null);
        setsEditText.setVisibility(View.VISIBLE);

        weightTextView.setVisibility(View.VISIBLE);
        kgTextView.setVisibility(View.VISIBLE);
        weightEditText.setText(null);
        weightEditText.setVisibility(View.VISIBLE);

        setTimeTextView.setVisibility(View.GONE);
        setTimeEditText.setText("00:00");
        setTimeEditText.setVisibility(View.GONE);

        pauseTimeTextView.setVisibility(View.VISIBLE);
        pauseTimeEditText.setText("00:00");
        pauseTimeEditText.setVisibility(View.VISIBLE);

        descTextView.setVisibility(View.VISIBLE);
        descEditText.setText(null);
        descEditText.setVisibility(View.VISIBLE);


        constraintSet.clone(constraintLayout);
        constraintSet.connect(R.id.repsEditText,ConstraintSet.TOP,R.id.typeSpinner,ConstraintSet.BOTTOM,8);
        constraintSet.connect(R.id.setsEditText,ConstraintSet.TOP,R.id.repsEditText,ConstraintSet.BOTTOM,8);
        constraintSet.connect(R.id.weightEditText,ConstraintSet.TOP,R.id.setsEditText,ConstraintSet.BOTTOM,8);
        constraintSet.connect(R.id.pauseTimeEditText,ConstraintSet.TOP,R.id.weightEditText,ConstraintSet.BOTTOM,8);
        constraintSet.applyTo(constraintLayout);
    }
    public void nothingSelected(){
        repsTextView.setVisibility(View.GONE);
        repsEditText.setText("");
        repsEditText.setVisibility(View.GONE);

        setsTextView.setVisibility(View.GONE);
        repsEditText.setText("");
        setsEditText.setVisibility(View.GONE);

        weightTextView.setVisibility(View.GONE);
        kgTextView.setVisibility(View.GONE);
        weightEditText.setText("");
        weightEditText.setVisibility(View.GONE);

        setTimeTextView.setVisibility(View.GONE);
        setTimeEditText.setText("00:00");
        setTimeEditText.setVisibility(View.GONE);

        pauseTimeTextView.setVisibility(View.GONE);
        pauseTimeEditText.setText("00:00");
        pauseTimeEditText.setVisibility(View.GONE);

        descTextView.setVisibility(View.GONE);
        descEditText.setText("");
        descEditText.setVisibility(View.GONE);


        constraintSet.clone(constraintLayout);

        constraintSet.applyTo(constraintLayout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_creator);

        constraintLayout = findViewById(R.id.csl);

        setTime = 0;
        pauseTime = 0;

        titleEditText = findViewById(R.id.exerciseTitleEditText);

        typeSpinner = (Spinner) findViewById(R.id.typeSpinner);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.types, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        typeSpinner.setAdapter(arrayAdapter);
        typeSpinner.setSelection(0);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 1:
                        enduranceSelected();
                        break;
                    case 2:
                        bodyweightSelected();
                        break;
                    case 3:
                        weightsSelected();
                        break;
                    default:
                        nothingSelected();
                        Toast.makeText(ExerciseCreator.this, R.string.exercise_type_alert,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        repsTextView = findViewById(R.id.repsTextView);
        repsEditText = findViewById(R.id.repsEditText);
        repsEditText.setFilters(new InputFilter[]{ new InputFilterMinMax("0","999")});

        setsTextView = findViewById(R.id.setsTextView);
        setsEditText = findViewById(R.id.setsEditText);
        setsEditText.setFilters(new InputFilter[]{ new InputFilterMinMax("0","999")});

        kgTextView = findViewById(R.id.kgTextView);
        weightTextView = findViewById(R.id.weightTextView);
        weightEditText = findViewById(R.id.weightEditText);
        weightEditText.setFilters(new InputFilter[]{ new InputFilterMinMax("0","999")});

        setTimeTextView = findViewById(R.id.setTimeTextView);
        setTimeEditText = findViewById(R.id.setTimeEditText);
        setTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(v);
            }
        });
        pauseTimeTextView = findViewById(R.id.pauseTimeTextView);
        pauseTimeEditText = findViewById(R.id.pauseTimeEditText);
        pauseTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(v);
            }
        });

        descTextView = findViewById(R.id.descriptionTextView);
        descEditText = findViewById(R.id.descEditText);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle(R.string.title_activity_exercise_creator);
    }

    private void saveExercise(){
        String title = titleEditText.getText().toString();
        String description = descEditText.getText().toString();
        String setsString = setsEditText.getText().toString();
        String repsString = repsEditText.getText().toString();
        String weightString = weightEditText.getText().toString();
        int sets;
        int reps;
        int weight;

        if(setsString.length() == 0){
            sets = 0;
        } else {
            sets = Integer.parseInt(setsString);
        }

        if(repsString.length() == 0){
            reps = 0;
        } else {
            reps = Integer.parseInt(repsString);
        }

        if(weightString.length() == 0){
            weight = 0;
        } else {
            weight = Integer.parseInt(weightString);
        }


        Integer type = typeSpinner.getSelectedItemPosition();

        switch (type){
            case 0:
                Toast.makeText(this, R.string.exercise_fill_alert, Toast.LENGTH_LONG).show();
                return;
            case 1:
                if(title.trim().isEmpty() || description.trim().isEmpty() || setTime == 0 || setsString.trim().isEmpty() || sets == 0 || pauseTime == 0) {
                    Toast.makeText(this, R.string.exercise_fill_alert, Toast.LENGTH_LONG).show();
                    return;
                }

                break;
            case 2:
                if(title.trim().isEmpty() || description.trim().isEmpty() || setsString.trim().isEmpty() || sets == 0 || repsString.trim().isEmpty() || reps == 0 || pauseTime == 0) {
                    Toast.makeText(this, R.string.exercise_fill_alert, Toast.LENGTH_LONG).show();
                    return;
                }
                break;
            case 3:
                if(title.trim().isEmpty() || description.trim().isEmpty() || setsString.trim().isEmpty() || sets == 0 || repsString.trim().isEmpty() || reps == 0 || weightString.trim().isEmpty() || weight == 0 || pauseTime == 0) {
                    Toast.makeText(this, R.string.exercise_fill_alert, Toast.LENGTH_LONG).show();
                    return;
                }
                break;
            default:
                break;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_TYPE, type);
        data.putExtra(EXTRA_SETS, sets);
        data.putExtra(EXTRA_SETTIME, setTime);
        data.putExtra(EXTRA_REPS, reps);
        data.putExtra(EXTRA_WEIGHT, weight);
        data.putExtra(EXTRA_PAUSETIME, pauseTime);

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.create_exercise_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_exercise:
                saveExercise();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void applyData(int id, int minutes, int seconds) {
        if(id == R.id.setTimeEditText){
            setTime = minutes*60 + seconds;
        } else if(id ==  R.id.pauseTimeEditText){
            pauseTime = minutes*60 + seconds;
        }
        String min;
        if(minutes<10){
            min = "0"+minutes;
        } else {
            min = String.valueOf(minutes);
        }
        String sec;
        if(seconds<10){
            sec = "0"+seconds;
        } else {
            sec = String.valueOf(seconds);
        }
        EditText et = findViewById(id);
        et.setText(min+":"+sec);
    }
}
