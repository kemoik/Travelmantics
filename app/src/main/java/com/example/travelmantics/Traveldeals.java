package com.example.travelmantics;

import java.io.Serializable;

public class Traveldeals implements Serializable
{
    private String id;
    private String tittle;
    private String description;
    private String price;
    private String ImageUrl;
    private String ImageName;

    public Traveldeals(){

    }


    public Traveldeals( String tittle, String description, String price, String imageUrl,String ImageName) {
        this.setId(id);
        this.setTittle(tittle);
        this.setDescription(description);
        this.setPrice(price);
        this.setImageUrl(imageUrl);
        this.setImageName(ImageName);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }
}
