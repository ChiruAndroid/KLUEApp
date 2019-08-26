package com.volive.klueapp.models;

import java.io.Serializable;

public class Category_Model implements Serializable {
    String cat_id, cname, cat_image;

    public Category_Model(){
        // Empty
    }

    public Category_Model(String cat_id, String cname, String cat_image) {
        this.cat_id = cat_id;
        this.cname = cname;
        this.cat_image = cat_image;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCat_image() {
        return cat_image;
    }

    public void setCat_image(String cat_image) {
        this.cat_image = cat_image;
    }
}
