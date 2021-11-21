package com.sdpd.companion.data.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Report {

    public String email;
    public String GroupId;
    public String Description;

    public Report(String email, String GroupId, String Description) {
        this.email = email;
        this.GroupId = GroupId;
        this.Description = Description;
    }
}
