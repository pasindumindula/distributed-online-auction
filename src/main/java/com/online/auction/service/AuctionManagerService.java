package com.online.auction.service;

import jakarta.ejb.*;
import jakarta.annotation.Resource;
import jakarta.jms.*;
import com.online.auction.model.Bid;
import com.online.auction.jms.BidMessage;
import com.online.auction.exception.BidException;

@Stateless
public class AuctionManagerService {

    @EJB
    private BidManagerService bidManager;

    @Resource
    private JMSContext jmsContext;

    @Resource(lookup = "java:app/jms/bidTopic")
    private Topic bidTopic;

    public boolean placeBid(Bid bid) {
        try {
            // 1. Delegate bid validation and persistence to BidManagerService
            Bid createdBid = bidManager.createBid(bid);
            if (createdBid == null) {
                return false; // Bid was not valid
            }

            // 2. Create and send a JMS Message with bid details
            BidMessage bidMsg = new BidMessage(
                    createdBid.getAuctionId(),
                    "Awesome Item", // TODO: Fetch actual item name from DB
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
}