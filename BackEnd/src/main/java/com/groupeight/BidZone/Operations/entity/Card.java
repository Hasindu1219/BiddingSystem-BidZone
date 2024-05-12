package com.groupeight.BidZone.Operations.entity;

public class Card {

    private String cardNumber;
    private String expiryDate;
    private String cvv;

    // Constructors, getters, and setters
    // Implement constructors, getters, and setters

    @Override
    public String toString() {
        return "CardDetails{" +
                "cardNumber='" + cardNumber + '\'' +
                ", expiryDate='" + expiryDate + '\'' +
                ", cvv='" + cvv + '\'' +
                '}';
    }
}
