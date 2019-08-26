package com.volive.klueapp.models;

import java.io.Serializable;

public class Home_banner_model implements Serializable {

    String banner_id,banner_image,banner_link,status;

    public String getBanner_id() {
        return banner_id;
    }

    public void setBanner_id(String banner_id) {
        this.banner_id = banner_id;
    }

    public String getBanner_image() {
        return banner_image;
    }

    public void setBanner_image(String banner_image) {
        this.banner_image = banner_image;
    }

    public String getBanner_link() {
        return banner_link;
    }

    public void setBanner_link(String banner_link) {
        this.banner_link = banner_link;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
