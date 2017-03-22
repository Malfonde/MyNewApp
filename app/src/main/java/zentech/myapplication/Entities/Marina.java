package zentech.myapplication.Entities;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

import zentech.myapplication.DAL.Model;

/**
 * Created by izik on 12/17/2016.
 */

public class Marina implements Parcelable
{
    private String imagePath;
    private String name;
    private ArrayList<Worker> workers;
 //TODO::location
    private String objectID;
    private String email;
    private String facebookEmail;
    private String marineFacebookID;
    private ArrayList<Dock> docks;
    private ArrayList<Service> returnBoatServices;
    private ArrayList<Service> getBoatServices;
    private ArrayList<String> rackedBoatsIDs;

    public Marina(String name, ArrayList<Worker> workers, String objectID, String email,
                  String facebookEmail, String marineFacebookID, ArrayList<Dock> docks, ArrayList<Service> returnBoatServices, ArrayList<Service> getBoatServices, ArrayList<String> rackedBoatsIDs)
    {
        this.name = name;
        this.workers = workers;
        this.objectID = objectID;
        this.email = email;
        this.facebookEmail = facebookEmail;
        this.marineFacebookID = marineFacebookID;
        this.docks = docks;
        this.returnBoatServices = returnBoatServices;
        this.getBoatServices = getBoatServices;
        this.rackedBoatsIDs = rackedBoatsIDs;
        this.imagePath = Model.instance().SharedFolder +objectID + Model.instance().MarineProfileImageName;
    }


    protected Marina(Parcel in) {
        imagePath = in.readString();
        name = in.readString();
        workers = in.createTypedArrayList(Worker.CREATOR);
        objectID = in.readString();
        email = in.readString();
        facebookEmail = in.readString();
        marineFacebookID = in.readString();
        docks = in.createTypedArrayList(Dock.CREATOR);
        returnBoatServices = in.createTypedArrayList(Service.CREATOR);
        getBoatServices = in.createTypedArrayList(Service.CREATOR);
        rackedBoatsIDs = in.createStringArrayList();
    }

    public static final Creator<Marina> CREATOR = new Creator<Marina>() {
        @Override
        public Marina createFromParcel(Parcel in) {
            return new Marina(in);
        }

        @Override
        public Marina[] newArray(int size) {
            return new Marina[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imagePath);
        parcel.writeString(name);
        parcel.writeTypedList(workers);
        parcel.writeString(objectID);
        parcel.writeString(email);
        parcel.writeString(facebookEmail);
        parcel.writeString(marineFacebookID);
        parcel.writeTypedList(docks);
        parcel.writeTypedList(returnBoatServices);
        parcel.writeTypedList(getBoatServices);
        parcel.writeStringList(rackedBoatsIDs);
    }

    //region getters

    public String getImagePath() {
        return imagePath;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Worker> getWorkers() {
        return workers;
    }

    public String getObjectID() {
        return objectID;
    }

    public String getEmail() {
        return email;
    }

    public String getFacebookEmail() {
        return facebookEmail;
    }

    public String getMarineFacebookID() {
        return marineFacebookID;
    }

    public ArrayList<Dock> getDocks() {
        return docks;
    }

    public ArrayList<Service> getReturnBoatServices() {
        return returnBoatServices;
    }

    public ArrayList<Service> getGetBoatServices() {
        return getBoatServices;
    }

    public ArrayList<String> getRackedBoatsIDs() {
        return rackedBoatsIDs;
    }

    //endregion
}
