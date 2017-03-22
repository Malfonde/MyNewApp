package zentech.myapplication.Entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ad05n on 1/29/2017.
 */

public class Service implements Parcelable
{
    private String marineID;
    private String name;
    private String description;
    private String objectId;
    private double price;

    public Service(String name, String description, String objectId, double price, String marineID) {
        this.name = name;
        this.description = description;
        this.objectId = objectId;
        this.price = price;
        this.marineID = marineID;
    }

    //region getters

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getObjectId() {
        return objectId;
    }

    public double getPrice() {
        return price;
    }

    protected Service(Parcel in) {
        marineID = in.readString();
        name = in.readString();
        description = in.readString();
        objectId = in.readString();
        price = in.readDouble();
    }

    public static final Creator<Service> CREATOR = new Creator<Service>() {
        @Override
        public Service createFromParcel(Parcel in) {
            return new Service(in);
        }

        @Override
        public Service[] newArray(int size) {
            return new Service[size];
        }
    };

    public String getMarineID() {
        return marineID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(marineID);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(objectId);
        parcel.writeDouble(price);
    }


    //end


}
