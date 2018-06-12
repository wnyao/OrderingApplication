package com.example.kongwenyao.orderingapplication;

public class CartItem {

    private int tagNum;
    private String itemName;
    private int itemAmount;
    private double totalPrice;

    public CartItem(int tagNum, String itemName, int itemAmount, double totalPrice) {
        this.tagNum = tagNum;
        this.itemName = itemName;
        this.itemAmount = itemAmount;
        this.totalPrice = totalPrice;
    }

    public int getTag() {
        return tagNum;
    }

    public String getItemName() {
        return itemName;
    }

    public int getItemAmount() {
        return itemAmount;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
