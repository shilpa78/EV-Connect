package com.example.auth_app.ui.shops;

public class ShopListItem {
    public int shop_num;
    public String shop_name;
    public double lati;
    public double longi;
    public String contact_number;
    public String strt_time;
    public String stop_time;
    public double ratings;
    public String address;
    public double distance;
    public String status;
    public String url;
    public int image;

    public ShopListItem(int shop_num, String shop_name, double lati, double longi, String strt_time, String stop_time, double ratings, String address, double distance, String status, String url, String contact_number, int image) {
        this.shop_num = shop_num;
        this.shop_name = shop_name;
        this.lati = lati;
        this.longi = longi;
        this.contact_number = contact_number;
        this.strt_time = strt_time;
        this.stop_time = stop_time;
        this.ratings = ratings;
        this.address = address;
        this.distance = distance;
        this.status = status;
        this.image =image;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getShop_num() {
        return shop_num;
    }

    public void setShop_num(int shop_num) {
        this.shop_num = shop_num;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public double getLati() {
        return lati;
    }

    public void setLati(double lati) {
        this.lati = lati;
    }

    public double getLongi() {
        return longi;
    }

    public void setLongi(double longi) {
        this.longi = longi;
    }

    public String getStrt_time() {
        return strt_time;
    }

    public void setStrt_time(String strt_time) {
        this.strt_time = strt_time;
    }

    public String getStop_time() {
        return stop_time;
    }

    public void setStop_time(String stop_time) {
        this.stop_time = stop_time;
    }

    public double getRatings() {
        return ratings;
    }

    public void setRatings(double ratings) {
        this.ratings = ratings;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}

