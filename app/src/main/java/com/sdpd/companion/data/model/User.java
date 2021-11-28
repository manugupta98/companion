package com.sdpd.companion.data.model;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class User {
    private String uid;
    private String displayName;
    private String photoUri;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public User(String uid, String displayName, String photoUri) {
        this.uid = uid;
        this.displayName = displayName;
        this.photoUri = photoUri;
    }

    public User(FirebaseUser firebaseUser){
        this.uid = firebaseUser.getUid();
        this.displayName = firebaseUser.getDisplayName();
        this.photoUri = firebaseUser.getPhotoUrl().toString();
    }

    public String getUid() {
        return uid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("displayName", displayName);
        result.put("photoUri", (photoUri != null)? photoUri : false);
        return result;
    }

}
