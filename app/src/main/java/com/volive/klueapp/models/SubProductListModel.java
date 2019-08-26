package com.volive.klueapp.models;

import java.io.Serializable;

public class SubProductListModel implements Serializable {

    String prod_id;
    String pname;
    String prod_image;
    String price_sar;
    int is_wish_list;
    String price_kwd, regular_price_kwd, price_aed, regular_price_aed, price_usd, regular_price_usd;

    public String getPrice_kwd() {
        return price_kwd;
    }

    public void setPrice_kwd(String price_kwd) {
        this.price_kwd = price_kwd;
    }

    public String getRegular_price_kwd() {
        return regular_price_kwd;
    }

    public void setRegular_price_kwd(String regular_price_kwd) {
        this.regular_price_kwd = regular_price_kwd;
    }

    public String getPrice_aed() {
        return price_aed;
    }

    public void setPrice_aed(String price_aed) {
        this.price_aed = price_aed;
    }

    public String getRegular_price_aed() {
        return regular_price_aed;
    }

    public void setRegular_price_aed(String regular_price_aed) {
        this.regular_price_aed = regular_price_aed;
    }

    public String getPrice_usd() {
        return price_usd;
    }

    public void setPrice_usd(String price_usd) {
        this.price_usd = price_usd;
    }

    public String getRegular_price_usd() {
        return regular_price_usd;
    }

    public void setRegular_price_usd(String regular_price_usd) {
        this.regular_price_usd = regular_price_usd;
    }

    public int getIs_wish_list() {
        return is_wish_list;
    }

    public void setIs_wish_list(int is_wish_list) {
        this.is_wish_list = is_wish_list;
    }

    boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getPrice_sar() {
        return price_sar;
    }

    public void setPrice_sar(String price_sar) {
        this.price_sar = price_sar;
    }

    public String getRegular_price_sar() {
        return regular_price_sar;
    }

    public void setRegular_price_sar(String regular_price_sar) {
        this.regular_price_sar = regular_price_sar;
    }

    String regular_price_sar;
    String price_type,pdt_price_type;

    public String getPrice_type() {
        return price_type;
    }

    public void setPrice_type(String price_type) {
        this.price_type = price_type;
    }

    public String getPdt_price_type() {
        return pdt_price_type;
    }

    public void setPdt_price_type(String pdt_price_type) {
        this.pdt_price_type = pdt_price_type;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    String brand_name;

    public String getProd_id() {
        return prod_id;
    }

    public void setProd_id(String prod_id) {
        this.prod_id = prod_id;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getProd_image() {
        return prod_image;
    }

    public void setProd_image(String prod_image) {
        this.prod_image = prod_image;
    }

}
