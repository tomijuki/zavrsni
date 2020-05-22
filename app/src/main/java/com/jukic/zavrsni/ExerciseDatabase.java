package com.jukic.zavrsni;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.jukic.zavrsni.exercise.Exercise;
import com.jukic.zavrsni.exercise.ExerciseDao;
import com.jukic.zavrsni.run.Run;
import com.jukic.zavrsni.run.RunDao;

@Database(entities = {Run.class, Exercise.class}, version = 1)
public abstract class ExerciseDatabase extends RoomDatabase {

    private static ExerciseDatabase instance;

    public abstract ExerciseDao exerciseDao();
    public abstract RunDao runDao();

    public static synchronized ExerciseDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ExerciseDatabase.class,"exercise_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateAsyncTask(instance).execute();
        }
    };

    private static class PopulateAsyncTask extends AsyncTask<Void,Void,Void>{
        private ExerciseDao exerciseDao;
        private RunDao runDao;

        private PopulateAsyncTask(ExerciseDatabase db){
            runDao = db.runDao();
            exerciseDao = db.exerciseDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            runDao.insert(new Run(37.3402,-122.0455,37.3977,-122.0424, 900000, 3000, 30000, 5));
            runDao.insert(new Run(37.3402,-122.0455,37.3977,-122.0424, 990000, 3000, 33000, 5));
            runDao.insert(new Run(37.3402,-122.0455,37.3977,-122.0424, 930000, 3000, 31000,5));
            exerciseDao.insert(new Exercise(1, 0, 3, 0, "PLANK","Ma cvrsto buraz", 60, 60));
            exerciseDao.insert(new Exercise(2, 15, 3, 0, "PUSH UP","Ma pumpaj buraz", 0, 60));
            exerciseDao.insert(new Exercise(3, 12, 3, 60, "BENCH PRESS","Ma dizi buraz", 0, 60));
            exerciseDao.insert(new Exercise(1, 0, 3, 0, "PLANK 1","Ma cvrsto buraz", 60, 60));
            exerciseDao.insert(new Exercise(2, 15, 3, 0, "PUSH UP 1","Ma pumpaj buraz", 0, 60));
            exerciseDao.insert(new Exercise(3, 12, 3, 60, "BENCH PRESS 1","Ma dizi buraz", 0, 60));
            exerciseDao.insert(new Exercise(1, 0, 3, 0, "PLANK 2","Ma cvrsto buraz", 60, 60));
            exerciseDao.insert(new Exercise(2, 15, 3, 0, "PUSH UP 2","Ma pumpaj buraz", 0, 60));
            exerciseDao.insert(new Exercise(3, 12, 3, 60, "BENCH PRESS 2","Ma dizi buraz", 0, 60));
            exerciseDao.insert(new Exercise(1, 0, 3, 0, "PLANK 3","Ma cvrsto buraz", 60, 60));
            exerciseDao.insert(new Exercise(2, 15, 3, 0, "PUSH UP 3","Ma pumpaj buraz", 0, 60));
            exerciseDao.insert(new Exercise(3, 12, 3, 60, "BENCH PRESS 3","Ma dizi buraz", 0, 60));

            return null;
        }
    }
}
