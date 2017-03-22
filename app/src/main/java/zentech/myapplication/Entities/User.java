package zentech.myapplication.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by izik on 12/14/2016.
 */

public class User implements Parcelable
{
    private String objectId;
    private String loginEmail;
    private String facebookEmail;
    private String name;
    private ArrayList<Boat> userBoats = new ArrayList<>();
   //TODO:: private String phoneNumber;
    private String userFacebookID;
    private String zipCode;
    private String dob;

    public User (String objectId,String email, String name, ArrayList<Boat> userBoats, String facebookEmail, String userFacebookID, String zipCode, String dob)
    {
        this.loginEmail = email;
        this.name = name;
        this.objectId = objectId;
        this.userBoats = userBoats;
        this.facebookEmail = facebookEmail;
        this.userFacebookID = userFacebookID;
    }

    //region getters

    protected User(Parcel in) {
        objectId = in.readString();
        loginEmail = in.readString();
        facebookEmail = in.readString();
        name = in.readString();
        userBoats = in.createTypedArrayList(Boat.CREATOR);
        userFacebookID = in.readString();
        zipCode = in.readString();
        dob = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getObjectId() {
        return objectId;
    }

    public String getLoginEmail() {
        return loginEmail;
    }

    public String getFacebookEmail() {
        return facebookEmail;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Boat> getUserBoats()
    {
        return userBoats;
    }

    public String getUserFacebookID() {
        return userFacebookID;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getDateOfBirth() {
        return dob;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(objectId);
        dest.writeString(loginEmail);
        dest.writeString(facebookEmail);
        dest.writeString(name);
        dest.writeTypedList(userBoats);
        dest.writeString(userFacebookID);
        dest.writeString(zipCode);
        dest.writeString(dob);
    }

    //endregion
}
