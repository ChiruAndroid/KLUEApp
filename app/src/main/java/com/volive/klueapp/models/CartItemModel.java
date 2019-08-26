package com.volive.klueapp.models;

public class CartItemModel {

    String prod_id;
    String brand_name;
    String quantity;
    String pname;
    String prod_image;
    String regular_price_sar;
    String min_quantity;
    String price_sar;
    Boolean isChecked;
    String price_kwd, regular_price_kwd, price_aed, regular_price_aed, price_usd, regular_price_usd;
    String price;
    String currency_type;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCurrency_type() {
        return currency_type;
    }

    public void setCurrency_type(String currency_type) {
        this.currency_type = currency_type;
    }


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

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProd_id() {
        return prod_id;
    }

    public void setProd_id(String prod_id) {
        this.prod_id = prod_id;
    }

    public String getPrice_sar() {
        return price_sar;
    }

    public void setPrice_sar(String price_sar) {
        this.price_sar = price_sar;
    }


    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
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

    public String getRegular_price_sar() {
        return regular_price_sar;
    }

    public void setRegular_price_sar(String regular_price_sar) {
        this.regular_price_sar = regular_price_sar;
    }

    public String getMin_quantity() {
        return min_quantity;
    }

    public void setMin_quantity(String min_quantity) {
        this.min_quantity = min_quantity;
    }
}
