package com.example.familyhealthhandbook.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PusherResponse implements Serializable {
    @SerializedName("type")
    @Expose
    private int type;
    @SerializedName("information")
    @Expose
    private MyNotification information;

    public PusherResponse(int type, MyNotification information) {
        this.type = type;
        this.information = information;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public MyNotification getInformation() {
        return information;
    }

    public void setInformation(MyNotification information) {
        this.information = information;
    }
}
