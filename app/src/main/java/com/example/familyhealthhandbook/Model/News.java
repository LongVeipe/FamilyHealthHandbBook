package com.example.familyhealthhandbook.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class News implements Serializable {
    @SerializedName("aid")
    @Expose
    private String aid;
    @SerializedName("categoryname")
    @Expose
    private String categoryname;
    @SerializedName("catealias")
    @Expose
    private String catealias;
    @SerializedName("urlcate")
    @Expose
    private String urlcate;
    @SerializedName("atype")
    @Expose
    private String atype;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("publishtime")
    @Expose
    private String publishtime;
    @SerializedName("publishdate")
    @Expose
    private String publishdate;
    @SerializedName("lead")
    @Expose
    private String lead;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("tags")
    @Expose
    private String tags;

    public News(String aid, String categoryname, String catealias, String urlcate, String atype, String title, String image, String publishtime, String publishdate, String lead, String link, String tags) {
        this.aid = aid;
        this.categoryname = categoryname;
        this.catealias = catealias;
        this.urlcate = urlcate;
        this.atype = atype;
        this.title = title;
        this.image = image;
        this.publishtime = publishtime;
        this.publishdate = publishdate;
        this.lead = lead;
        this.link = link;
        this.tags = tags;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getCatealias() {
        return catealias;
    }

    public void setCatealias(String catealias) {
        this.catealias = catealias;
    }

    public String getUrlcate() {
        return urlcate;
    }

    public void setUrlcate(String urlcate) {
        this.urlcate = urlcate;
    }

    public String getAtype() {
        return atype;
    }

    public void setAtype(String atype) {
        this.atype = atype;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPublishtime() {
        return publishtime;
    }

    public void setPublishtime(String publishtime) {
        this.publishtime = publishtime;
    }

    public String getPublishdate() {
        return publishdate;
    }

    public void setPublishdate(String publishdate) {
        this.publishdate = publishdate;
    }

    public String getLead() {
        return lead;
    }

    public void setLead(String lead) {
        this.lead = lead;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
