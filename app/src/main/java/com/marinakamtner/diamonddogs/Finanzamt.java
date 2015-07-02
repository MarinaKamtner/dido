package com.marinakamtner.diamonddogs;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by marina on 25.06.15.
 */
public class Finanzamt implements Parcelable {

    private Integer id;          // DB id
    private String idkz;
    private String name;
    private String street;       // strasse
    private String postcode;     // plz
    private String location;     // Ort
    private String phone;
    private String openinghours; // Oeffnungszeiten
    private String photourl;
    private String latitude;
    private String longitude;

    public Finanzamt() {
        super();
    }

    public Finanzamt(String idkz, String name, String street, String postcode, String location, String phone, String photourl, String openinghours, String latitude, String longitude) {
        //  this.id = id;
        this.idkz = idkz;
        this.name = name;
        this.postcode = postcode;
        this.location = location;
        this.street = street;
        this.phone = phone;
        this.openinghours = openinghours;
        this.photourl = photourl;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Finanzamt{" +
                //   "id=" + id +
                ", name='" + name + '\'' +
                ", street='" + street + '\'' +
                ", postcode='" + postcode + '\'' +
                ", location='" + location + '\'' +
                ", phone='" + phone + '\'' +
                ", photourl='" + photourl + '\'' +
                ", openinghours='" + openinghours + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdkz() {
        return idkz;
    }

    public void setIdkz(String idkz) {
        this.idkz = idkz;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getOpeninghours() {
        return openinghours;
    }

    public void setOpeninghours(String openinghours) {
        this.openinghours = openinghours;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    // Parcelable FIFO
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(idkz);
        out.writeString(name);
        out.writeString(street);
        out.writeString(postcode);
        out.writeString(location);
        out.writeString(phone);
        out.writeString(photourl);
        out.writeString(openinghours);
        out.writeString(latitude);
        out.writeString(longitude);
    }

    // Parcelable: this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Finanzamt> CREATOR = new Parcelable.Creator<Finanzamt>() {
        public Finanzamt createFromParcel(Parcel in) {
            return new Finanzamt(in);
        }

        // Parcelable
        public Finanzamt[] newArray(int size) {
            return new Finanzamt[size];
        }
    };

    // Parcelable: FIFO! example constructor that takes a Parcel and gives you an object populated with it's values
    private Finanzamt(Parcel in) {
        // id = in.readInt();
        idkz = in.readString();
        name = in.readString();
        street = in.readString();
        postcode = in.readString();
        location = in.readString();
        phone = in.readString();
        photourl = in.readString();
        openinghours = in.readString();
        latitude = in.readString();
        longitude = in.readString();
    }
}
