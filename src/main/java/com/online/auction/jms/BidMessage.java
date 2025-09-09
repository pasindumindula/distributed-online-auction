package com.online.auction.jms;

import java.io.Serializable;

public class BidMessage implements Serializable {
    private Long auctionId;
    private String itemName;
    private Double newPrice;
    private String bidderName;

    // Constructor
    public BidMessage(Long auctionId, String itemName, Double newPrice, String bidderName) {
        this.auctionId = auctionId;
        this.itemName = itemName;
        this.newPrice = newPrice;
        this.bidderName = bidderName;
    }

    // Getters and Setters
    public Long getAuctionId() { return auctionId; }
    public void setAuctionId(Long auctionId) { this.auctionId = auctionId; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public Double getNewPrice() { return newPrice; }
    public void setNewPrice(Double newPrice) { this.newPrice = newPrice; }

    public String getBidderName() { return bidderName; }
    public void setBidderName(String bidderName) { this.bidderName = bidderName; }
}