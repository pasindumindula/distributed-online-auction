package com.online.auction.ejb;

import com.online.auction.entity.Auction;
import com.online.auction.entity.Bid;
import com.online.auction.entity.User;
import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSProducer;
import jakarta.jms.Topic;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import java.time.Instant;

@Stateless
public class BidManagerBean {

    @PersistenceContext(unitName = "AuctionPU")
    private EntityManager em;

    @Inject
    private AuctionRegistry registry;

    @Resource(lookup = "jms/auctionTopic")
    private Topic auctionTopic;

    @Resource(lookup = "jms/__defaultConnectionFactory")
    private ConnectionFactory connectionFactory;

    public Bid placeBid(Long auctionId, Long userId, double amount) {
        Auction auction = em.find(Auction.class, auctionId, LockModeType.OPTIMISTIC);
        if (auction == null || !auction.isActive()) {
            throw new IllegalStateException("Auction not active or not found");
        }

        if (amount <= auction.getCurrentPrice()) {
            throw new IllegalArgumentException("Bid must be higher than current price");
        }

        User user = em.find(User.class, userId);
        if (user == null) throw new IllegalArgumentException("User not found");

        Bid bid = new Bid();
        bid.setAuction(auction);
        bid.setBidder(user);
        bid.setAmount(amount);
        bid.setPlacedAt(Instant.now());
        em.persist(bid);

        auction.setCurrentPrice(amount);
        em.merge(auction);

        publishBidEvent(auction.getId(), user.getUsername(), amount);
        return bid;
    }

    private void publishBidEvent(Long auctionId, String username, double amount) {
        try (JMSContext ctx = connectionFactory.createContext()) {
            JMSProducer producer = ctx.createProducer();
            String json = String.format("{\"auctionId\":%d,\"user\":\"%s\",\"amount\":%s}", auctionId, username, amount);
            producer.send(auctionTopic, json);
        }
    }
}
