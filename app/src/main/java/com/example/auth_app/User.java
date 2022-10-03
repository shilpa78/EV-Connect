package com.example.auth_app;

import android.widget.TextView;

import org.w3c.dom.Text;

public class User {
    public String u_name, u_age, u_email,u_city;
    public String ev_type,ev_number;

    public User(String u_name, String u_age, String u_email,String u_city, String ev_type, String ev_number) {
        this.u_name = u_name;
        this.u_age = u_age;
        this.u_email = u_email;
        this.u_city = u_city;
        this.ev_type = ev_type;
        this.ev_number = ev_number;
    }
}