package com.example.roumeliotis.coen242projectapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Order implements Parcelable {

    private long id = -1;
    private long user_id = -1;
    private String order_key = null;
    private String machine_id = null;
    private String mixer = null;            // Mixer type
    private String alcohol = null;          // Alcohol type
    private boolean doubleAlcohol = false;  // Double
    private double price = 0.0;
    private String state = null;
    private boolean paid = false;

    public Order(long id,long user_id, String order_key, String machine_id, String mixer, String alcohol,
                 boolean doubleAlcohol, double price, String state, Boolean paid) {
        this.id = id;
        this.user_id = user_id;
        this.order_key = order_key;
        this.machine_id = machine_id;
        this.mixer = mixer;
        this.alcohol = alcohol;
        this.doubleAlcohol = doubleAlcohol;
        this.price = price;
        this.state = state;
        this.paid = paid;
    }

    protected Order(Parcel in) {
        id = in.readLong();
        user_id = in.readLong();
        order_key = in.readString();
        machine_id = in.readString();
        mixer = in.readString();
        alcohol = in.readString();
        doubleAlcohol = in.readByte() != 0;  //doubleAlcohol == true if byte != 0
        price = in.readDouble();
        state = in.readString();
        paid = in.readByte() != 0;  //paid == true if byte != 0
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };


    // Get and set ID
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    // Get and set user id
    public long getUserId() { return user_id; }
    public void setUserId(long id) { this.user_id = id; }
    // Get and set orderKey
    public String getOrder_key() { return order_key; }
    public void setOrder_key(String order_key) { this.order_key = order_key; }

    // Get and set machine ID
    public String getMachine_id() { return machine_id; }
    public void setMachine_id(String machine_id) { this.machine_id = machine_id; }

    // Get and set mixer
    public String getMixer() { return mixer; }
    public void setMixer(String mixer) { this.mixer = mixer; }

    // Get and set alcohol
    public String getAlcohol(){ return alcohol; }
    public void setAlcohol(String alcohol){ this.alcohol = alcohol; }

    // Get and set double
    public boolean getDoubleAlcohol() { return doubleAlcohol; }
    public void setDoubleAlcohol(boolean doubleAlcohol) { this.doubleAlcohol = doubleAlcohol; }

    // Get and set price
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    // Get and set state
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    // Get and set paid
    public boolean getPaid() { return paid; }
    public void setPaid(boolean paid) { this.paid = paid; }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mixer);
        parcel.writeString(alcohol);
        parcel.writeByte((byte) (doubleAlcohol ? 1 : 0));     //if doubleAlcohol == true, byte == 1
    }

    //Used to print the orders
    @Override
    public String toString(){
        return ("None".equals(this.alcohol) ? "" : this.alcohol )
                + ("None".equals(mixer) || "None".equals(alcohol) ? "" : " and ")
                + ("None".equals(this.mixer) ? "" : this.mixer )
                + ("None".equals(this.alcohol) ? "\n" : (doubleAlcohol ? "\nDouble":"\nSingle") )
                + "\nKey:\n " + this.order_key;
    }

}