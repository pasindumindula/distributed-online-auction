package com.online.auction.service;

import jakarta.ejb.Stateless;
import jakarta.ejb.EJB;
import jakarta.annotation.Resource;
import jakarta.jms.JMSContext;
import jakarta.jms.Topic;
import jakarta.jms.ObjectMessage;
import com.online.auction.entity.Bid;
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
}