package com.example.roumeliotis.coen242projectapp;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable{

    private long id = -1;     //ID in server database
    private String name = null;     // Name of the user
    private String email = null;    // Email of the user
    private String password = null; // Password of the user
    private String token = null; // token to make requests

    public User(long id, String name, String email, String password, String token) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.token = token;
    }

    protected User(Parcel in) {
        id = in.readLong();
        name = in.readString();
        email = in.readString();
        password = in.readString();
        token = in.readString();
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


    // Get and set remoteID
    public long getid() {
        return id;
    }
    public void setid(long id) {
        this.id = id;
    }

    // Get and set name
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    // Get and set email
    public String getEmail(){ return email; }
    public void setEmail(String email){ this.email = email; }

    // Get and set password
    public String getPassword(){ return password; }
    public void setPassword(String password){ this.password = password; }

    // Get and set token
    public String getToken(){ return token; }
    public void setToken(String token){ this.token = token; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(password);
        parcel.writeString(token);
    }
}