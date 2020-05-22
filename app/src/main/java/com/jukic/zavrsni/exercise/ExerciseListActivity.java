package com.jukic.zavrsni.exercise;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jukic.zavrsni.ConfirmExerciseDialog;
import com.jukic.zavrsni.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Toast;
import java.util.List;


public class ExerciseListActivity extends AppCompatActivity implements ConfirmExerciseDialog.ConfirmExerciseDialogListener {

    public static Exercise delete;
    public static Exercise execute;

    public static final String EXTRA_EXECUTE = "com.jukic.zavrsni.EXTRA_EXECUTE";
    public static final int ADD_EXERCISE_REQUEST = 1;
    private ExerciseViewModel exerciseViewModel;

    public void openConfirmDialog(){
        ConfirmExerciseDialog dialog = new ConfirmExerciseDialog();
        dialog.show(getSupportFragmentManager(), "confirm dialog");
    }

    @Override
    public void onConfirmed() {
        exerciseViewModel.delete(delete);
        Toast.makeText(ExerciseListActivity.this, getString(R.string.exercise_delete), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.title_activity_exercise_list);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExerciseListActivity.this, ExerciseCreator.class);
                startActivityForResult(intent, ADD_EXERCISE_REQUEST);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.exerciseRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final ExerciseAdapter adapter = new ExerciseAdapter();
        recyclerView.setAdapter(adapter);



        exerciseViewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);
        exerciseViewModel.getAllExercises().observe(this, new Observer<List<Exercise>>() {
            @Override
            public void onChanged(List<Exercise> exercises) {
                //update RecyclerView
                adapter.submitList(exercises);
            }
        });

        adapter.setOnItemLongClickListener(new ExerciseAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(Exercise exercise) {
                delete = exercise;
                openConfirmDialog();
            }
        });

        adapter.setOnItemClickListener(new ExerciseAdapter.OnItemCLickListener() {
            @Override
            public void onItemClick(Exercise exercise) {
                execute = exercise;
                Intent intent = new Intent(ExerciseListActivity.this, ExerciseExecutor.class);
                intent.putExtra(EXTRA_EXECUTE, exercise);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_EXERCISE_REQUEST && resultCode == RESULT_OK && data != null){
            int type = data.getIntExtra(ExerciseCreator.EXTRA_TYPE, 0);
            String title = data.getStringExtra(ExerciseCreator.EXTRA_TITLE);
            String desc = data.getStringExtra(ExerciseCreator.EXTRA_DESCRIPTION);
            int sets = data.getIntExtra(ExerciseCreator.EXTRA_SETS, 0);
            int setTime = data.getIntExtra(ExerciseCreator.EXTRA_SETTIME, 0);
            int reps = data.getIntExtra(ExerciseCreator.EXTRA_REPS, 0);
            int weight = data.getIntExtra(ExerciseCreator.EXTRA_WEIGHT, 0);
            int pauseTime = data.getIntExtra(ExerciseCreator.EXTRA_PAUSETIME, 0);

            Exercise exercise = new Exercise(type, reps, sets, weight, title, desc, setTime, pauseTime);
            exerciseViewModel.insert(exercise);

            Toast.makeText(ExerciseListActivity.this, getString(R.string.exercise_add_alert), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ExerciseListActivity.this, getString(R.string.exercise_add_error), Toast.LENGTH_SHORT).show();
        }
    }
}
