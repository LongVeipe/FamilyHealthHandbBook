package com.example.familyhealthhandbook.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Group implements Serializable {
    @SerializedName("members")
    @Expose
    private List<String> members;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("master")
    @Expose
    private String master;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("inviteCode")
    @Expose
    private String inviteCode;
    @SerializedName("createAt")
    @Expose
    private String CreateAt;
    @SerializedName("updateAt")
    @Expose
    private String UpdateAt;

    public Group(List<String> members, String id, String master, String name, String description, String avatar, String inviteCode, String createAt, String updateAt) {
        this.members = members;
        this.id = id;
        this.master = master;
        this.name = name;
        this.description = description;
        this.avatar = avatar;
        this.inviteCode = inviteCode;
        CreateAt = createAt;
        UpdateAt = updateAt;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getCreateAt() {
        return CreateAt;
    }

    public void setCreateAt(String createAt) {
        CreateAt = createAt;
    }

    public String getUpdateAt() {
        return UpdateAt;
    }

    public void setUpdateAt(String updateAt) {
        UpdateAt = updateAt;
    }
}
