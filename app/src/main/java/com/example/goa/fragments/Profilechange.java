package com.example.goa.fragments;

import android.provider.ContactsContract;

public class Profilechange {
    private String id;
    private String Name;
    private String emailId;
    public Profilechange(){

    }
    public Profilechange(String id ,String Name,String emailid) {

        this.id=id;
        this.Name=Name;
        this.emailId=emailId;

    }
    public String getid(){
        return id;
    }
    public String getName(){
        return Name;
    }public String emailId(){
        return emailId;
    }
}

