package edu.nyu.switchkey.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * @author : Kaustubh Deshmukh
 * @date : 14/12/15 : 11:29 PM
 * @email : kaustubh@betacraft.co
 */
public class FacebookPhoto implements Parcelable {
    private String mLink;
    private long mId;

    public FacebookPhoto(String link, long id){
        Log.d("##Facebook", "Adding " + link);
        mLink = link;
        mId = id;
    }

    protected FacebookPhoto(Parcel in) {
        mLink = in.readString();
        mId = in.readLong();
    }

    public static final Creator<FacebookPhoto> CREATOR = new Creator<FacebookPhoto>() {
        @Override
        public FacebookPhoto createFromParcel(Parcel in) {
            return new FacebookPhoto(in);
        }

        @Override
        public FacebookPhoto[] newArray(int size) {
            return new FacebookPhoto[size];
        }
    };

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        mLink = link;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * marshalled representation.
     *
     * @return a bitmask indicating the set of special object types marshalled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mLink);
        dest.writeLong(mId);
    }
}
