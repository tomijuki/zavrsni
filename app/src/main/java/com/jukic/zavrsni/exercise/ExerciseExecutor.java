package com.jukic.zavrsni.exercise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.jukic.zavrsni.R;

import java.util.Locale;

public class ExerciseExecutor extends AppCompatActivity {

    TextView titleTextView;
    TextView descTextView;
    TextView tapTextView;
    TextView timerTextView;
    TextView phaseTextView;

    private Vibrator vibrator;
    private boolean vibrate;

    private int setTime;
    private int pauseTime;
    private int reps;
    private int sets;
    private int weight;
    private int type;

    private long timeLeft;
    private long pauseTimeLeft;
    private long endTime;
    private boolean setInProgress;
    private boolean timerIsPaused;
    private boolean setTimerRunning;
    private boolean pauseTimerRunning;
    private CountDownTimer setCountDownTimer;
    private CountDownTimer pauseCountDownTimer;
    private int intervals;

    private LinearLayout layout;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        vibrator.cancel();

        if (setTimerRunning){
            setCountDownTimer.cancel();
        }
        if (pauseTimerRunning){
            pauseCountDownTimer.cancel();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadLocale();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_executor);
        setTitle(R.string.title_activity_exercise_executor);

        Intent intent = getIntent();
        Exercise exercise = intent.getParcelableExtra(ExerciseListActivity.EXTRA_EXECUTE);

        layout = findViewById(R.id.exerciseExecutorLayout);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            layout.setOrientation(LinearLayout.HORIZONTAL);
        }

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        SharedPreferences sharedPreferences = getSharedPreferences("Vibrate", MODE_PRIVATE);
        vibrate = sharedPreferences.getBoolean("vibrate", false);

        setTime = exercise.getSetTime();
        pauseTime = exercise.getPauseTime();
        reps = exercise.getRepetitions();
        sets = exercise.getSet();
        weight = exercise.getWeight();
        type = exercise.getType();

        intervals = 1;

        timeLeft = setTime * 1000;
        pauseTimeLeft = pauseTime * 1000;
        timerIsPaused = false;
        setTimerRunning = false;
        pauseTimerRunning = false;

        titleTextView = findViewById(R.id.exerciseTitleTextView);
        descTextView = findViewById(R.id.exerciseDescriptionTextView);
        tapTextView = findViewById(R.id.tapTextView);
        timerTextView = findViewById(R.id.timerTextView);
        phaseTextView = findViewById(R.id.phaseTextView);


        titleTextView.setText(exercise.getTitle());
        descTextView.setText(exercise.getDesc());


        timerTextView.setText("");
        phaseTextView.setText("");
        updateTapButton();

        tapTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pauseTimerRunning || setTimerRunning){
                    if (pauseTimerRunning){
                        Toast.makeText(ExerciseExecutor.this, getString(R.string.exercise_pause_in_progress), Toast.LENGTH_SHORT).show();
                    }else{
                        pauseExercise(setCountDownTimer);
                    }
                } else {
                    startExercise();
                }
            }
        });
    }

    private void finishExercise(){
        tapTextView.setClickable(true);
        phaseTextView.setText(R.string.exercise_finished);
        timerTextView.setText(R.string.exercise_congratulations);
        tapTextView.setText(R.string.exercise_finish);
        tapTextView.setTextColor(Color.BLACK);
        tapTextView.setBackgroundColor(Color.CYAN);
        tapTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExerciseExecutor.this.finish();
            }
        });
        setInProgress = false;
        pauseTimerRunning = false;
        setTimerRunning = false;
        intervals = sets;
        finishVibrate();
    }

    private void pauseFinished(){
        updateSetCountDownText();

        String phaseFormatted = String.format(Locale.getDefault(),getString(R.string.pause_finished),intervals-1,sets);
        phaseTextView.setText(phaseFormatted);

        tapTextView.setText(R.string.exercise_ready);
        tapTextView.setBackgroundColor(Color.GREEN);
        tapTextView.setTextColor(Color.BLACK);
        tapTextView.setClickable(true);

        finishVibrate();

        if (setTime == 0){
            tapTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pauseTimerRunning || setTimerRunning){
                        if (pauseTimerRunning){
                            Toast.makeText(ExerciseExecutor.this, getString(R.string.exercise_pause_in_progress), Toast.LENGTH_SHORT).show();
                        }else{
                            pauseExercise(setCountDownTimer);
                        }
                    } else {
                        startExercise();
                    }
                }
            });
        }
    }

    private void tickVibrate() {
        if (vibrate){
            vibrator.vibrate(25);
        }
    }

    private void finishVibrate() {
        if (vibrate) {
            vibrator.vibrate(150);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    vibrator.vibrate(200);
                }
            }, 200);
        }
    }

    private void startPause(){
        endTime = System.currentTimeMillis() + pauseTimeLeft;
        tapTextView.setClickable(false);
        pauseCountDownTimer = new CountDownTimer(pauseTimeLeft,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                pauseTimeLeft = millisUntilFinished;
                updatePauseCountDownText();
                if (millisUntilFinished<=5500){
                    tickVibrate();
                }
            }
            @Override
            public void onFinish() {
                pauseTimerRunning = false;
                resetTimer();
                if(intervals == sets){
                    finishExercise();
                } else {
                    intervals++;
                    pauseFinished();
                }
            }
        }.start();
        pauseTimerRunning = true;
        timerIsPaused = false;
        setInProgress = false;
        updateTapButton();
    }


    private void startExercise(){
        if (setTime != 0){
            endTime = System.currentTimeMillis() + timeLeft;

            setCountDownTimer = new CountDownTimer(timeLeft, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeLeft = millisUntilFinished;
                    updateSetCountDownText();
                    if (millisUntilFinished<=5500){
                        tickVibrate();
                    }
                }
                @Override
                public void onFinish() {
                    setTimerRunning = false;
                    resetTimer();
                    updatePauseCountDownText();
                    finishVibrate();
                    startPause();
                }
            }.start();
            setTimerRunning = true;
            timerIsPaused = false;
            updateTapButton();
        } else {
            String weightRepsFormatted;
            if(weight != 0){
                weightRepsFormatted = String.format(Locale.getDefault(), getString(R.string.exercise_weight),reps,weight);
            } else {
                weightRepsFormatted = String.format(Locale.getDefault(), getString(R.string.exercise_reps),reps);
            }
            timerTextView.setTextSize(22);
            timerTextView.setText(weightRepsFormatted);
            tapTextView.setText(R.string.exercise_done);
            tapTextView.setBackgroundColor(Color.BLUE);
            tapTextView.setTextColor(Color.WHITE);
            String phaseFormatted = String.format(Locale.getDefault(), getString(R.string.set_in_progress),intervals,sets);
            phaseTextView.setText(phaseFormatted);
            setInProgress = true;
            tapTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updatePauseCountDownText();
                    startPause();
                }
            });
        }
    }
    private void pauseExercise(CountDownTimer runningTimer){
        runningTimer.cancel();
        setTimerRunning = false;
        timerIsPaused = true;
        updateTapButton();
    }

    private void resetTimer(){
        timeLeft = setTime * 1000;
        pauseTimeLeft = pauseTime * 1000;
        timerIsPaused = false;
        updateTapButton();
    }

    private void updateSetCountDownText(){
        int minutes = (int) (timeLeft/1000) / 60;
        int seconds = (int) (timeLeft/1000) % 60;

        timerTextView.setTextSize(42);
        String phaseFormatted = String.format(Locale.getDefault(),getString(R.string.set_in_progress),intervals,sets);
        phaseTextView.setText(phaseFormatted);

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);
        timerTextView.setText(timeLeftFormatted);
    }

    private void updatePauseCountDownText(){
        int minutes = (int) (pauseTimeLeft/1000) / 60;
        int seconds = (int) (pauseTimeLeft/1000) % 60;

        timerTextView.setTextSize(42);
        String phaseFormatted = String.format(Locale.getDefault(),getString(R.string.pause_in_progress),intervals,sets);
        phaseTextView.setText(phaseFormatted);

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);
        timerTextView.setText(timeLeftFormatted);
    }

    private void updateTapButton(){
        if (pauseTimerRunning || setTimerRunning){
            if (pauseTimerRunning){
                tapTextView.setText(R.string.exercise_pause_in_progress);
                tapTextView.setBackgroundColor(Color.YELLOW);
                tapTextView.setTextColor(Color.BLACK);
            }else{
                tapTextView.setText(R.string.exercise_pause);
                tapTextView.setBackgroundColor(Color.BLUE);
                tapTextView.setTextColor(Color.WHITE);
            }
        } else {
            if (timerIsPaused){
                tapTextView.setText(R.string.exercise_resume);
                tapTextView.setBackgroundColor(Color.YELLOW);
                tapTextView.setTextColor(Color.BLACK);
            } else {
                tapTextView.setText(R.string.exercise_start);
                tapTextView.setBackgroundColor(Color.GREEN);
                tapTextView.setTextColor(Color.BLACK);
            }
        }
    }



    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        loadLocale();
        setContentView(R.layout.activity_exercise_executor);
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
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean("timerIsPaused", timerIsPaused);
        outState.putLong("millisLeft", timeLeft);
        outState.putBoolean("setTimerRunning", setTimerRunning);
        outState.putBoolean("pauseTimerRunning", pauseTimerRunning);
        outState.putLong("endTime", endTime);
        outState.putInt("intervals", intervals);
        outState.putLong("pauseLeft", pauseTimeLeft);
        outState.putBoolean("setInProgress", setInProgress);
    }
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        timeLeft = savedInstanceState.getLong("millisLeft");
        pauseTimeLeft = savedInstanceState.getLong("pauseLeft");
        timerIsPaused = savedInstanceState.getBoolean("timerIsPaused");
        setTimerRunning = savedInstanceState.getBoolean("setTimerRunning");
        pauseTimerRunning = savedInstanceState.getBoolean("pauseTimerRunning");
        setInProgress = savedInstanceState.getBoolean("setInProgress");
        intervals = savedInstanceState.getInt("intervals");

        if (setTimerRunning || pauseTimerRunning){
            if (pauseTimerRunning){
                endTime = savedInstanceState.getLong("endTime");
                pauseTimeLeft = endTime - System.currentTimeMillis();
                startPause();
            }else {
                endTime = savedInstanceState.getLong("endTime");
                timeLeft = endTime - System.currentTimeMillis();
                startExercise();
            }
        } else if (setInProgress){
            startExercise();
        } else if (intervals == sets){
            finishExercise();
        }else if (intervals != 1){
            pauseFinished();
        }
    }
}
