package com.online.auction.jms;

import com.online.auction.websocket.AuctionWebSocket;
import jakarta.ejb.*;
import jakarta.jms.*;

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:app/jms/bidTopic"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic")
})
public class BidNotificationMDB implements MessageListener {

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof ObjectMessage) {
                ObjectMessage objectMessage = (ObjectMessage) message;
                BidMessage bidMsg = (BidMessage) objectMessage.getObject();

                String jsonMessage = String.format(
                        "{\"auctionId\": %d, \"itemName\": \"%s\", \"newPrice\": %.2f, \"bidder\": \"%s\"}",
                        bidMsg.getAuctionId(), bidMsg.getItemName(), bidMsg.getNewPrice(), bidMsg.getBidderName()
                );

                AuctionWebSocket.broadcast(jsonMessage);
                System.out.println("MDB: Broadcasted update: " + jsonMessage);
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}