package com.online.auction.ejb;

import com.online.auction.entity.Auction;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.Instant;

@Stateless
public class AuctionManagerBean {

    @PersistenceContext(unitName = "AuctionPU")
    private EntityManager em;

    @Inject
    private AuctionRegistry registry;

    public Auction createAuction(String title, String desc, double startPrice, Instant start, Instant end) {
        Auction auction = new Auction();
        auction.setTitle(title);
        auction.setDescription(desc);
        auction.setStartingPrice(startPrice);
        auction.setCurrentPrice(startPrice);
        auction.setStartTime(start);
        auction.setEndTime(end);
        auction.setActive(true);
        em.persist(auction);
        registry.register(auction.getId());
        return auction;
    }

    public void closeAuction(Long auctionId) {
        Auction auction = em.find(Auction.class, auctionId);
        if (auction != null) {
            auction.setActive(false);
            em.merge(auction);
            registry.unregister(auctionId);
        }
    }
}
