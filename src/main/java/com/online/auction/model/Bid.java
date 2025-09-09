package com.online.auction.model;

import java.io.Serializable;
import java.util.Date;

public class Bid implements Serializable {
    private Long id;
    private Long auctionId;
    private String bidderName;
    private Double amount;
    private Date bidTime;

    // Default constructor (required for JavaBeans)
    public Bid() {
    }

    // Parameterized constructor
    public Bid(Long auctionId, String bidderName, Double amount) {
        this.auctionId = auctionId;
        this.bidderName = bidderName;
        this.amount = amount;
        this.bidTime = new Date(); // Set current time automatically
    }

    // Parameterized constructor with all fields
    public Bid(Long id, Long auctionId, String bidderName, Double amount, Date bidTime) {
        this.id = id;
        this.auctionId = auctionId;
        this.bidderName = bidderName;
        this.amount = amount;
        this.bidTime = bidTime;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(Long auctionId) {
        this.auctionId = auctionId;
    }

    public String getBidderName() {
        return bidderName;
    }

    public void setBidderName(String bidderName) {
        this.bidderName = bidderName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getBidTime() {
        return bidTime;
    }

    public void setBidTime(Date bidTime) {
        this.bidTime = bidTime;
    }

    @Override
    public String toString() {
        return "Bid{" +
                "id=" + id +
                ", auctionId=" + auctionId +
                ", bidderName='" + bidderName + '\'' +
                ", amount=" + amount +
                ", bidTime=" + bidTime +
                '}';
    }
}