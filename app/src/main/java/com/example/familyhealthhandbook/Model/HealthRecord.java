package com.example.familyhealthhandbook.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class HealthRecord implements Serializable {
    @SerializedName("images")
    @Expose
    private List<String> images;
    @SerializedName("_id")
    @Expose
    private String _id;
    @SerializedName("idUser")
    @Expose
    private String idUser;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("sickness")
    @Expose
    private Sickness sickness;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updateAt")
    @Expose
    private String updateAt;

    public HealthRecord(List<String> images, String id, String idUser, String location, Sickness sickness, String createAt, String updateAt) {
        this.images = images;
        this._id = id;
        this.idUser = idUser;
        this.location = location;
        this.sickness = sickness;
        this.createdAt = createAt;
        this.updateAt = updateAt;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
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
        return createdAt;
    }

    public void setCreateAt(String createAt) {
        this.createdAt = createAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }
}
