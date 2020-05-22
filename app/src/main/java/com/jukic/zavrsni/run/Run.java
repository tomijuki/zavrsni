package com.jukic.zavrsni.run;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "run_table")
public class Run implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private double startLatitude;
    private double startLongitude;
    private double endLatitude;
    private double endLongitude;
    private long duration;
    private long distance;
    private long pace;
    private int happiness;

    public Run(double startLatitude, double startLongitude, double endLatitude, double endLongitude, long duration, long distance, long pace, int happiness) {
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.endLatitude = endLatitude;
        this.endLongitude = endLongitude;
        this.duration = duration;
        this.distance = distance;
        this.pace = pace;
        this.happiness = happiness;
    }

    protected Run(Parcel in) {
        id = in.readInt();
        startLatitude = in.readDouble();
        startLongitude = in.readDouble();
        endLatitude = in.readDouble();
        endLongitude = in.readDouble();
        duration = in.readLong();
        distance = in.readLong();
        pace = in.readLong();
        happiness = in.readInt();
    }

    public static final Creator<Run> CREATOR = new Creator<Run>() {
        @Override
        public Run createFromParcel(Parcel in) {
            return new Run(in);
        }

        @Override
        public Run[] newArray(int size) {
            return new Run[size];
        }
    };

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getHappiness() {
        return happiness;
    }

    public double getStartLatitude() {
        return startLatitude;
    }

    public double getStartLongitude() {
        return startLongitude;
    }

    public double getEndLatitude() {
        return endLatitude;
    }

    public double getEndLongitude() {
        return endLongitude;
    }

    public long getDuration() {
        return duration;
    }

    public long getDistance() {
        return distance;
    }

    public long getPace() {
        return pace;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(startLatitude);
        dest.writeDouble(startLongitude);
        dest.writeDouble(endLatitude);
        dest.writeDouble(endLongitude);
        dest.writeInt(id);
        dest.writeLong(duration);
        dest.writeLong(distance);
        dest.writeLong(pace);
        dest.writeInt(happiness);
    }
}
