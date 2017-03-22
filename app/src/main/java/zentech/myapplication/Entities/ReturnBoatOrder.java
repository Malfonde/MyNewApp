package zentech.myapplication.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by ad05n on 1/29/2017.
 */
public class ReturnBoatOrder implements Parcelable
{
    private String boatID;
    private String objectId;
    private String time;
    private String date;
    private String orderingUserID;
    private ArrayList<Service> selectedServices;

    public ReturnBoatOrder(String objectId, String time, String date, String orderingUserID, ArrayList<Service> selectedServices, String boatID)
    {
        this.objectId = objectId;
        this.time = time;
        this.date = date;
        this.orderingUserID = orderingUserID;
        this.selectedServices = selectedServices;
        this.boatID = boatID;
    }
    protected ReturnBoatOrder(Parcel in) {
        boatID = in.readString();
        objectId = in.readString();
        time = in.readString();
        date = in.readString();
        orderingUserID = in.readString();
        selectedServices = in.createTypedArrayList(Service.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(boatID);
        dest.writeString(objectId);
        dest.writeString(time);
        dest.writeString(date);
        dest.writeString(orderingUserID);
        dest.writeTypedList(selectedServices);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ReturnBoatOrder> CREATOR = new Creator<ReturnBoatOrder>() {
        @Override
        public ReturnBoatOrder createFromParcel(Parcel in) {
            return new ReturnBoatOrder(in);
        }

        @Override
        public ReturnBoatOrder[] newArray(int size) {
            return new ReturnBoatOrder[size];
        }
    };

    //region getters



    public String getBoatID() {
        return boatID;
    }

    public ArrayList<Service> getSelectedServices() {
        return selectedServices;
    }

    public String getObjectId()
    {
        return objectId;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getOrderingUser() {
        return orderingUserID;
    }

    //endregion
}
