package com.online.auction.service;

import com.online.auction.entity.Auction;
import jakarta.ejb.Stateless;
import jakarta.ejb.EJB;
import jakarta.annotation.Resource;
import jakarta.jms.JMSContext;
import jakarta.jms.Topic;
import jakarta.jms.ObjectMessage;
import com.online.auction.entity.Bid;
import com.online.auction.jms.BidMessage;
import com.online.auction.exception.BidException;

import java.util.ArrayList;
import java.util.List;

@Stateless
public class AuctionManagerService {

    @EJB
    private BidManagerService bidManager;

    @Resource
    private JMSContext jmsContext;

    @Resource(lookup = "jms/bidTopic")
    private Topic bidTopic;

    public boolean placeBid(Bid bid) {
        try {
            // 1. Delegate bid validation and persistence
            Bid createdBid = bidManager.createBid(bid);
            if (createdBid == null) {
                return false;
            }

            // 2. Create and send JMS Message
            BidMessage bidMsg = new BidMessage(
                    createdBid.getAuctionId(),
                    "Auction Item", // TODO: Fetch actual item name
                    createdBid.getAmount(),
                    createdBid.getBidderName()
            );

            ObjectMessage message = jmsContext.createObjectMessage(bidMsg);
            jmsContext.createProducer().send(bidTopic, message);
            System.out.println("JMS Message sent for Auction ID: " + bidMsg.getAuctionId());

            return true;

        } catch (BidException e) {
            System.err.println("Bid processing failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Add these methods to your AuctionManagerService class
    public List<Auction> getActiveAuctions() {
        // Implement logic to get active auctions from your database
        // This is a placeholder - you'll need to implement the actual logic
        return new ArrayList<>(); // Return empty list for now
    }

    public Auction getAuction(Long id) {
        // Implement logic to get a specific auction by ID from your database
        // This is a placeholder - you'll need to implement the actual logic
        return null; // Return null for now
    }
}