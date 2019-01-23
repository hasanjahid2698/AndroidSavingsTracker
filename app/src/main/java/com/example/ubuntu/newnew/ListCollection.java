package com.example.ubuntu.newnew;

import android.os.Parcel;
import android.os.Parcelable;

public class ListCollection implements Parcelable{

    int id;
    int option;
    String date;
    String category;
    Double amount;


    public ListCollection(int id, int option, String date, String category, Double amount) {
        this.id = id;
        this.option = option;
        this.date = date;
        this.category = category;
        this.amount = amount;
    }

    public ListCollection(){

    }

    public ListCollection(Parcel parcel){
        id = parcel.readInt();
        option = parcel.readInt();
        date = parcel.readString();
        category = parcel.readString();
        amount = parcel.readDouble();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(option);
        parcel.writeString(date);
        parcel.writeString(category);
        parcel.writeDouble(amount);
    }

    public static final Creator<ListCollection> CREATOR = new Creator<ListCollection>() {
        @Override
        public ListCollection createFromParcel(Parcel in) {
            return new ListCollection(in);
        }

        @Override
        public ListCollection[] newArray(int size) {
            return new ListCollection[size];
        }
    };
}
