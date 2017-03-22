package zentech.myapplication.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by ad05n on 1/29/2017.
 */
public class GetBoatOrder implements Parcelable
{
    private String boatID;
    private String objectId;
    private String time;
    private String date;
    private String orderingUserID;
    private ArrayList<Service> selectedServices;
    private double fuel;
    private boolean ice;

    public GetBoatOrder(String objectId, String time, String date, String orderingUserID, ArrayList<Service> selectedServices, String boatID, double fuel, boolean ice) {
        this.objectId = objectId;
        this.time = time;
        this.date = date;
        this.orderingUserID = orderingUserID;
        this.selectedServices = selectedServices;
        this.boatID = boatID;
        this.fuel = fuel;
        this.ice = ice;
   }

    protected GetBoatOrder(Parcel in) {
        boatID = in.readString();
        objectId = in.readString();
        time = in.readString();
        date = in.readString();
        orderingUserID = in.readString();
        selectedServices = in.createTypedArrayList(Service.CREATOR);
        fuel = in.readDouble();
        ice = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(boatID);
        dest.writeString(objectId);
        dest.writeString(time);
        dest.writeString(date);
        dest.writeString(orderingUserID);
        dest.writeTypedList(selectedServices);
        dest.writeDouble(fuel);
        dest.writeByte((byte) (ice ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GetBoatOrder> CREATOR = new Creator<GetBoatOrder>() {
        @Override
        public GetBoatOrder createFromParcel(Parcel in) {
            return new GetBoatOrder(in);
        }

        @Override
        public GetBoatOrder[] newArray(int size) {
            return new GetBoatOrder[size];
        }
    };

    //region getters

    public String getBoatID() {
        return boatID;
    }

    public String getObjectId() {
        return objectId;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getOrderingUserID() {
        return orderingUserID;
    }

    public ArrayList<Service> getSelectedServices() {
        return selectedServices;
    }




    //endregion
}
