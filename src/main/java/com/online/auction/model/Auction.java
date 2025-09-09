package com.online.auction.model;

import java.io.Serializable;
import java.util.Date;

public class Auction implements Serializable {
    private Long id;
    private String itemName;
    private String description;
    private Double startingPrice;
    private Double currentBid;
    private Date startTime;
    private Date endTime;
    private String status; // "ACTIVE", "CLOSED", "PENDING"

    // Constructors
    public Auction() {
    }

    public Auction(String itemName, String description, Double startingPrice, Date endTime) {
        this.itemName = itemName;
        this.description = description;
        this.startingPrice = startingPrice;
        this.currentBid = startingPrice;
        this.startTime = new Date();
        this.endTime = endTime;
        this.status = "ACTIVE";
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(Double startingPrice) {
        this.startingPrice = startingPrice;
    }

    public Double getCurrentBid() {
        return currentBid;
    }

    public void setCurrentBid(Double currentBid) {
        this.currentBid = currentBid;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Auction{" +
                "id=" + id +
                ", itemName='" + itemName + '\'' +
                ", description='" + description + '\'' +
                ", startingPrice=" + startingPrice +
                ", currentBid=" + currentBid +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", status='" + status + '\'' +
                '}';
    }
}