package zentech.myapplication.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by izik on 12/14/2016.
 */

public class Boat implements Parcelable
{
    private String lastTreatedTime;
    private String lastTreatedDate;
    private String objectId;
    private String name;
    private double fuel;
    private boolean isDocked;
    private String dockName;
    private String rackID;
    private boolean isRacked;
    private double weight;
    private String ownerID;
    private ArrayList<String> permittedUsersIDs;
    private boolean isForSale;
    private ArrayList<GetBoatOrder> getOrders;
    private ArrayList<ReturnBoatOrder> returnOrders;
    private String marineID;

    public Boat(String objectId, String name, double fuel, boolean isDocked, String dockName, String rackID, boolean isRacked, double weight, String ownerID, ArrayList<String> permittedUsersIDs,
                boolean isForSale, String lastTreatedTime, String lastTreatedDate, String marineID)
    {
        this.objectId = objectId;
        this.name = name;
        this.fuel = fuel;
        this.isDocked = isDocked;
        this.dockName = dockName;
        this.rackID = rackID;
        this.isRacked = isRacked;
        this.weight = weight;
        this.ownerID = ownerID;
        this.permittedUsersIDs = permittedUsersIDs;
        this.isForSale = isForSale;
        this.lastTreatedTime = lastTreatedTime;
        this.lastTreatedDate = lastTreatedDate;
        this.getOrders = new ArrayList<>();
        this.returnOrders = new ArrayList<>();
        this.marineID = marineID;
    }


    protected Boat(Parcel in) {
        lastTreatedTime = in.readString();
        lastTreatedDate = in.readString();
        objectId = in.readString();
        name = in.readString();
        fuel = in.readDouble();
        isDocked = in.readByte() != 0;
        dockName = in.readString();
        rackID = in.readString();
        isRacked = in.readByte() != 0;
        weight = in.readDouble();
        ownerID = in.readString();
        permittedUsersIDs = in.createStringArrayList();
        isForSale = in.readByte() != 0;
        getOrders = in.createTypedArrayList(GetBoatOrder.CREATOR);
        returnOrders = in.createTypedArrayList(ReturnBoatOrder.CREATOR);
        marineID = in.readString();
    }

    public static final Creator<Boat> CREATOR = new Creator<Boat>() {
        @Override
        public Boat createFromParcel(Parcel in) {
            return new Boat(in);
        }

        @Override
        public Boat[] newArray(int size) {
            return new Boat[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(lastTreatedTime);
        parcel.writeString(lastTreatedDate);
        parcel.writeString(objectId);
        parcel.writeString(name);
        parcel.writeDouble(fuel);
        parcel.writeByte((byte) (isDocked ? 1 : 0));
        parcel.writeString(dockName);
        parcel.writeString(rackID);
        parcel.writeByte((byte) (isRacked ? 1 : 0));
        parcel.writeDouble(weight);
        parcel.writeString(ownerID);
        parcel.writeStringList(permittedUsersIDs);
        parcel.writeByte((byte) (isForSale ? 1 : 0));
        parcel.writeTypedList(getOrders);
        parcel.writeTypedList(returnOrders);
        parcel.writeString(marineID);
    }

    //region getters
    public String getObjectId() {
        return objectId;
    }

    public String getName() {
        return name;
    }

    public double getFuel() {
        return fuel;
    }

    public boolean isDocked() {
        return isDocked;
    }

    public String getDockName() {
        return dockName;
    }

    public String getRackID() {
        return rackID;
    }

    public boolean isRacked() {
        return isRacked;
    }

    public double getWeight() {
        return weight;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public ArrayList<String> getPermittedUsers() {
        return permittedUsersIDs;
    }

    public boolean isForSale() {
        return isForSale;
    }

    public ArrayList<GetBoatOrder> getGetOrders() {
        return getOrders;
    }

    public ArrayList<ReturnBoatOrder> getReturnOrders() {
        return returnOrders;
    }

    public String getMarineID() {
        return marineID;
    }

    public String getLastTreatedTime() {
        return lastTreatedTime;
    }

    public String getLastTreatedDate() {
        return lastTreatedDate;
    }

    //endregion

}
