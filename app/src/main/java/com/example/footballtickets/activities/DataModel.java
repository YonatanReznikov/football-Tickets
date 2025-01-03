package com.example.footballtickets.activities;

public class DataModel {

    private String name;
    private String description;
    private int price;
    private int image1;
    private int image2;
    private int leagueImage;
    private String currencySymbol;
    private int id_;

    public DataModel(String name, String description, int price, int image1, int image2, int leagueImage, String currencySymbol, int id_) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.image1 = image1;
        this.image2 = image2;
        this.leagueImage = leagueImage;
        this.currencySymbol = currencySymbol;
        this.id_ = id_;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage1(int image1) {
        this.image1 = image1;
    }

    public void setImage2(int image2) {
        this.image2 = image2;
    }
    public String getCurrencySymbol() {
        return currencySymbol;
    }
    public void setLeagueImage(int leagueImage) {
        this.leagueImage = leagueImage;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getImage1() {
        return image1;
    }

    public int getImage2() {
        return image2;
    }

    public int getLeagueImage() {
        return leagueImage;
    }
    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public int getId() {
        return id_;
    }
}
