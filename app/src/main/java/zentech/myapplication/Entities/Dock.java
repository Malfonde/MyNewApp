package zentech.myapplication.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by ad05n on 1/29/2017.
 */
public class Dock implements Parcelable
{
    private String objectId;
    private ArrayList<String> boatsIDs;
    private String name;

    public Dock(String objectId, ArrayList<String> boatsIDs, String name) {
        this.objectId = objectId;
        this.boatsIDs = boatsIDs;
        this.name = name;
    }


    protected Dock(Parcel in) {
        objectId = in.readString();
        boatsIDs = in.createStringArrayList();
        name = in.readString();
    }

    public static final Creator<Dock> CREATOR = new Creator<Dock>() {
        @Override
        public Dock createFromParcel(Parcel in) {
            return new Dock(in);
        }

        @Override
        public Dock[] newArray(int size) {
            return new Dock[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(objectId);
        parcel.writeStringList(boatsIDs);
        parcel.writeString(name);
    }

    //region getters

    public String getObjectId() {
        return objectId;
    }

    public ArrayList<String> getBoatsIDs() {
        return boatsIDs;
    }

    public String getName() {
        return name;
    }

    //endregion
}
