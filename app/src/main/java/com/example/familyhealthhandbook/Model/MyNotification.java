package com.example.familyhealthhandbook.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MyNotification implements Serializable {
    @SerializedName("deleted")
    @Expose
    private Boolean deleted;
    @SerializedName("_id")
    @Expose
    private String _id;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("group")
    @Expose
    private Group group;
    @SerializedName("action")
    @Expose
    private int action;
    @SerializedName("createAt")
    @Expose
    private String createAt;
    @SerializedName("updateAt")
    @Expose
    private String updateAt;
    @SerializedName("members")
    @Expose
    private List<String> members;

    public MyNotification(Boolean deleted, String _id, User user, Group group, int action, String createAt, String updateAt, List<String> members) {
        this.deleted = deleted;
        this._id = _id;
        this.user = user;
        this.group = group;
        this.action = action;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.members = members;
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

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
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

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }
}
