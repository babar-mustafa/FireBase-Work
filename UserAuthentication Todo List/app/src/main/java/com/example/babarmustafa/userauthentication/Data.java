package com.example.babarmustafa.userauthentication;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by Babar Mustafa on 9/30/2016.
 */


public class Data {

    private String Uid;
    private String pushId;
    private String name;
    private String city;
    private boolean checkb;

    public Data() {
    }

    public Data(String uid, String pushId, String name, String city, boolean checkb) {
        Uid = uid;
        this.pushId = pushId;
        this.name = name;
        this.city = city;
        this.checkb = checkb;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isCheckb() {
        return checkb;
    }

    public void setCheckb(boolean checkb) {
        this.checkb = checkb;
    }
}
