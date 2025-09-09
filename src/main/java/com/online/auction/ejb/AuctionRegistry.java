package com.online.auction.ejb;

import jakarta.ejb.Lock;
import jakarta.ejb.LockType;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
@Startup
public class AuctionRegistry {

    private final Map<Long, Boolean> activeAuctions = new ConcurrentHashMap<>();

    @Lock(LockType.WRITE)
    public void register(Long auctionId) {
        activeAuctions.put(auctionId, true);
    }

    @Lock(LockType.WRITE)
    public void unregister(Long auctionId) {
        activeAuctions.remove(auctionId);
    }

    @Lock(LockType.READ)
    public boolean isActive(Long auctionId) {
        return activeAuctions.containsKey(auctionId);
    }
}