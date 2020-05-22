package com.jukic.zavrsni.exercise;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "exercise_table")
public class Exercise implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int Type;
    private int Repetitions;
    private int Set;
    private int Weight;
    @Nullable
    private String Title;
    @Nullable
    private String Desc;
    private int SetTime;
    private int PauseTime;

    public Exercise(int Type, int Repetitions, int Set, int Weight, String Title, String Desc, int SetTime, int PauseTime){

        this.Type = Type;
        this.Repetitions = Repetitions;
        this.Set = Set;
        this.Weight = Weight;
        this.Title = Title;
        this.Desc = Desc;
        this.SetTime = SetTime;
        this.PauseTime = PauseTime;
    }

    protected Exercise(Parcel in) {
        id = in.readInt();
        Type = in.readInt();
        Repetitions = in.readInt();
        Set = in.readInt();
        Weight = in.readInt();
        Title = in.readString();
        Desc = in.readString();
        SetTime = in.readInt();
        PauseTime = in.readInt();
    }

    public static final Creator<Exercise> CREATOR = new Creator<Exercise>() {
        @Override
        public Exercise createFromParcel(Parcel in) {
            return new Exercise(in);
        }

        @Override
        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return Type;
    }

    public int getRepetitions() {
        return Repetitions;
    }

    public int getSet() {
        return Set;
    }

    public int getWeight() {
        return Weight;
    }

    public String getTitle() {
        return Title;
    }

    public String getDesc() {
        return Desc;
    }

    public int getSetTime() {
        return SetTime;
    }

    public int getPauseTime() {
        return PauseTime;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(Type);
        dest.writeInt(Repetitions);
        dest.writeInt(Set);
        dest.writeInt(Weight);
        dest.writeString(Title);
        dest.writeString(Desc);
        dest.writeInt(SetTime);
        dest.writeInt(PauseTime);
    }
}
