package com.example.familyhealthhandbook.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class HealthRecordPost implements Serializable {
    @SerializedName("images")
    @Expose
    private List<String> images;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("sickness")
    @Expose
    private Sickness sickness;
    @SerializedName("createAt")
    @Expose
    private String createAt;

    public HealthRecordPost(List<String> images, String id, String location, Sickness sickness, String createAt) {
        this.images = images;
        this.id = id;
        this.location = location;
        this.sickness = sickness;
        this.createAt = createAt;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Sickness getSickness() {
        return sickness;
    }

    public void setSickness(Sickness sickness) {
        this.sickness = sickness;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }
}
