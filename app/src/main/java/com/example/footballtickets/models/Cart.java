package com.example.footballtickets.models;

public class Cart {
    private String cartId;
    private String matchName;

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    private String currencySymbol;
    private int price;
    private int ticketCount;
    private int selectedQuantity;
    private long timestamp;

    public Cart() {
    }

    // Constructor
    public Cart(String cartId, String matchName, String currencySymbol, int price, int ticketCount, long timestamp) {
        this.cartId = cartId;
        this.matchName = matchName;
        this.price = price;
        this.currencySymbol=currencySymbol;
        this.ticketCount = ticketCount;
        this.timestamp = timestamp;
        this.selectedQuantity = 1;
    }

    // Getters and Setters
    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getMatchName() {
        return matchName;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTicketCount() {
        return ticketCount;
    }

    public void setTicketCount(int ticketCount) {
        this.ticketCount = ticketCount;
    }

    public int getSelectedQuantity() {
        return selectedQuantity;
    }

    public void setSelectedQuantity(int selectedQuantity) {
        this.selectedQuantity = selectedQuantity;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
