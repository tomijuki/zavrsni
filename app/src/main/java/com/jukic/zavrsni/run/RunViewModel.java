package com.jukic.zavrsni.run;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class RunViewModel extends AndroidViewModel {
    private RunRepository repository;
    private LiveData<List<Run>> allRuns;

    public RunViewModel(@NonNull Application application) {
        super(application);
        repository = new RunRepository(application);
        allRuns = repository.getAllRuns();
    }

    public void insert(Run run){
        repository.insert(run);
    }
    public void delete(Run run){
        repository.delete(run);
    }
    public void update(Run run) {
        repository.update(run);
    }

    public LiveData<List<Run>> getAllRuns() {
        return allRuns;
    }
}
