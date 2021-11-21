package com.sdpd.companion.ui.group;

import java.io.Serializable;

public class Group implements Serializable {
    public String groupName;
    public String groupDes;
    public String groupCode;
    public String groupTopic;
    public String groupKey;

    public Group(){

    }
    public Group(String groupName, String groupDes, String groupCode, String groupTopic){
        this.groupName = groupName;
        this.groupDes = groupDes;
        this.groupCode = groupCode;
        this. groupTopic = groupTopic;
        this.groupKey = "";
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return this.groupName.toString();
    }
}