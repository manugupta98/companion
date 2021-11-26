package com.sdpd.companion.data.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;
import java.util.HashMap;

@IgnoreExtraProperties
public class User {

    public String username;
    public String email;

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

}
