package com.example.babarmustafa.todofirebase;

/**
 * Created by imran on 9/26/2016.
 */

public class Data {
    String name;
    String city;
    String id;
    boolean checkb;

    public Data(String id, String name, String city,  boolean checkb) {
        this.name = name;
        this.city = city;
        this.id = id;
        this.checkb = checkb;
    }

    public Data() {
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getId() {
        return id;
    }

    public boolean isCheckb() {
        return checkb;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCheckb(boolean checkb) {
        this.checkb = checkb;
    }
}
