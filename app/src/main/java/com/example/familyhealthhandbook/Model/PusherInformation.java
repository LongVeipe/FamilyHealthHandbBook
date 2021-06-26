package com.example.familyhealthhandbook.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PusherInformation implements Serializable {
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("members")
    @Expose
    private List<String> members;

    public PusherInformation(User user, List<String> members) {
        this.user = user;
        this.members = members;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }
}
