package com.online.auction.jms;

import jakarta.ejb.MessageDriven;
import jakarta.ejb.ActivationConfigProperty;
import jakarta.jms.MessageListener;
import jakarta.jms.Message;
import jakarta.jms.JMSException;
import jakarta.jms.ObjectMessage;
import com.online.auction.websocket.WebSocketEndpoint;

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/bidTopic"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Topic")
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

                WebSocketEndpoint.broadcast(jsonMessage);
                System.out.println("MDB: Broadcasted update: " + jsonMessage);
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}