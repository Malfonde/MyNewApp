package zentech.myapplication.Entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ad05n on 1/29/2017.
 */
public class Worker implements Parcelable {
    private String name;
    private String lastName;
    private String objectID;
    private String iD;
    private Boolean isAppointed;
    private double salary;

    public Worker(String name, String lastName, String objectID, String iD, Boolean isAppointed, double salary) {
        this.name = name;
        this.lastName = lastName;
        this.objectID = objectID;
        this.iD = iD;
        this.isAppointed = isAppointed;
        this.salary = salary;
    }


    protected Worker(Parcel in) {
        name = in.readString();
        lastName = in.readString();
        objectID = in.readString();
        iD = in.readString();
        salary = in.readDouble();
    }

    public static final Creator<Worker> CREATOR = new Creator<Worker>() {
        @Override
        public Worker createFromParcel(Parcel in) {
            return new Worker(in);
        }

        @Override
        public Worker[] newArray(int size) {
            return new Worker[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(lastName);
        parcel.writeString(objectID);
        parcel.writeString(iD);
        parcel.writeDouble(salary);
    }

    //region getters

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getObjectID() {
        return objectID;
    }

    public String getiD() {
        return iD;
    }

    public Boolean getAppointed() {
        return isAppointed;
    }

    public double getSalary() {
        return salary;
    }


    //endregion
}
