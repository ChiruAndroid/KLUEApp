package com.volive.klueapp.models;

import java.io.Serializable;

public class OrderDetailModel implements Serializable{
    String prod_id,product_qty,price,brand_name,prod_name,prod_image;

    String price_aed,price_kwd,price_usd;

    public String getPrice_aed() {
        return price_aed;
    }

    public void setPrice_aed(String price_aed) {
        this.price_aed = price_aed;
    }

    public String getPrice_kwd() {
        return price_kwd;
    }

    public void setPrice_kwd(String price_kwd) {
        this.price_kwd = price_kwd;
    }

    public String getPrice_usd() {
        return price_usd;
    }

    public void setPrice_usd(String price_usd) {
        this.price_usd = price_usd;
    }

    public String getProd_id() {
        return prod_id;
    }

    public void setProd_id(String prod_id) {
        this.prod_id = prod_id;
    }

    public String getProduct_qty() {
        return product_qty;
    }

    public void setProduct_qty(String product_qty) {
        this.product_qty = product_qty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    public String getProd_image() {
        return prod_image;
    }

    public void setProd_image(String prod_image) {
        this.prod_image = prod_image;
    }
}
