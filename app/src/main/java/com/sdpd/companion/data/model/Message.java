package com.sdpd.companion.data.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Message {
    private String id;
    private String senderId;
    private String senderName;
    private String message;
    private long timestamp;

    public Message() {
    }

    public Message(String id, String senderId, String senderName, String message, long timestamp) {
        this.id = id;
        this.senderId = senderId;
        this.senderName = senderName;
        this.message = message;
        this.timestamp = timestamp;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("senderId", senderId);
        result.put("senderName", senderName);
        result.put("message", message);
        result.put("timestamp", ServerValue.TIMESTAMP);
        return result;
    }

    public String getId() {
        return id;
    }


    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
