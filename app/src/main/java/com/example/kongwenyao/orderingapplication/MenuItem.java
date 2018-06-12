package com.example.kongwenyao.orderingapplication;

public class MenuItem {

    private String itemName;
    private String itemDescription;
    private double itemPrice;
    private int itemDrawableID;

    public MenuItem(String itemName, double itemPrice, String itemDescription, int itemDrawableID) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemDescription = itemDescription;
        this.itemDrawableID = itemDrawableID;
    }

    public MenuItem() {
        //Empty constructor
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public int getItemDrawableID() {
        return itemDrawableID;
    }
}
