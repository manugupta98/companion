package com.sdpd.companion.data.model;

import androidx.annotation.Nullable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties
public class Group {

    private String id;
    private String imageUri;
    private String name;
    private String description;
    private String classCode;

    private String lastMessage;
    private String lastMessageSenderName;


    public Group(){}

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastMessageSenderName() {
        return lastMessageSenderName;
    }

    public void setLastMessageSenderName(String lastMessageSenderName) {
        this.lastMessageSenderName = lastMessageSenderName;
    }


    public Group(String id, String imageUri, String name, String description, String classCode, String lastMessage, String lastMessageSenderName) {
        this.id = id;
        this.imageUri = imageUri;
        this.name = name;
        this.description = description;
        this.classCode = classCode;

        this.lastMessage = lastMessage;
        this.lastMessageSenderName = lastMessageSenderName;

    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("imageUri", (imageUri != null)? imageUri : false);
        result.put("name", name);
        result.put("description", description);
        result.put("classCode", (classCode != null)? classCode : false);
        result.put("lastMessage", (lastMessage != null)? lastMessage : false);
        result.put("lastMessageSenderName", (lastMessageSenderName != null)? lastMessageSenderName : false);

        return result;
    }


    public String getImageUri() {
        return imageUri;
    }

    public String getClassCode() {
        return classCode;
    }
}
