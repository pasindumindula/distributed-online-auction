package com.online.auction.service;

import jakarta.ejb.*;
import jakarta.ejb.ConcurrencyManagementType;

import java.util.concurrent.ConcurrentHashMap;

@Singleton
@Startup
@LocalBean
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class AuctionRegistry {
    private ConcurrentHashMap<Long, Double> currentHighBids = new ConcurrentHashMap<>();

    public void updateHighBid(Long auctionId, Double newBid) {
        currentHighBids.put(auctionId, newBid);
    }

    public Double getHighBid(Long auctionId) {
        return currentHighBids.get(auctionId);
    }

    public void registerAuction(Long auctionId, Double startingPrice) {
        currentHighBids.putIfAbsent(auctionId, startingPrice);
    }

    public void unregisterAuction(Long auctionId) {
        currentHighBids.remove(auctionId);
    }
}