package com.example.babarmustafa.userauthentication;

/**
 * Created by Babar Mustafa on 9/29/2016.
 */

public class User {
    String id;
    String name;
    String email;
    String pass;

    public User(String id, String name, String email, String pass) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.pass = pass;
    }

    public User(String s) {
    }

    public User() {

    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return pass;
    }
}
