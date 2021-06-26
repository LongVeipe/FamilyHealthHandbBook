package com.example.familyhealthhandbook.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Post implements Serializable {
    @SerializedName("deleted")
    @Expose
    private Boolean deleted;
    @SerializedName("_id")
    @Expose
    private String _id;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("idGroup")
    @Expose
    private String idGroup;
    @SerializedName("healthRecord")
    @Expose
    private HealthRecord healthRecord;
    @SerializedName("createAt")
    @Expose
    private String createAt;
    @SerializedName("updateAt")
    @Expose
    private String updateAt;

    public Post(Boolean deleted, String _id, User user, String idGroup, HealthRecord healthRecord, String createAt, String updateAt) {
        this.deleted = deleted;
        this._id = _id;
        this.user = user;
        this.idGroup = idGroup;
        this.healthRecord = healthRecord;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(String idGroup) {
        this.idGroup = idGroup;
    }

    public HealthRecord getHealthRecord() {
        return healthRecord;
    }

    public void setHealthRecord(HealthRecord healthRecord) {
        this.healthRecord = healthRecord;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }
}