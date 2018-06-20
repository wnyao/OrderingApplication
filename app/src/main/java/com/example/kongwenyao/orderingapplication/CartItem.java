package com.example.kongwenyao.orderingapplication;

public class CartItem extends MenuItem {

    private int tagNum;
    private int itemAmount;
    private double totalPrice;

    public CartItem (int tagNum, String itemName, int itemAmount, double totalPrice) {
        super();
        setItemName(itemName);
        this.tagNum = tagNum;
        this.itemAmount = itemAmount;
        this.totalPrice = totalPrice;
    }

    public int getTag() {
        return tagNum;
    }

    public int getItemAmount() {
        return itemAmount;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
