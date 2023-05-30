package com.example.basicman.model;

public class Cart {
    int idP;
    String nameP;
    long priceP;
    String imgP;
    int amount;

    public Cart() {
    }

    public int getIdP() {
        return idP;
    }

    public void setIdP(int idP) {
        this.idP = idP;
    }

    public String getNameP() {
        return nameP;
    }

    public void setNameP(String nameP) {
        this.nameP = nameP;
    }

    public long getPriceP() {
        return priceP;
    }

    public void setPriceP(long priceP) {
        this.priceP = priceP;
    }

    public String getImgP() {
        return imgP;
    }

    public void setImgP(String imgP) {
        this.imgP = imgP;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
