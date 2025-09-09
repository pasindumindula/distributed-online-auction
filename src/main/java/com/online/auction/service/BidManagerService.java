package com.online.auction.service;

import jakarta.ejb.*;
import jakarta.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import com.online.auction.model.Bid;
import com.online.auction.exception.BidException;

@Stateless
public class BidManagerService {

    @EJB
    private AuctionRegistry auctionRegistry;

    @Resource(lookup = "jdbc/auctionDB")
    private DataSource dataSource;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Bid createBid(Bid bid) {
        // 1. FIRST, check against the in-memory Singleton for speed
        Double currentHighBid = auctionRegistry.getHighBid(bid.getAuctionId());
        if (currentHighBid != null && bid.getAmount() <= currentHighBid) {
            System.out.println("Bid rejected. " + bid.getAmount() + " is not higher than " + currentHighBid);
            return null; // Bid is not high enough
        }

        // 2. If it passes, then do the database operation
        String sql = "INSERT INTO bid (auction_id, bidder_name, amount, bid_time) VALUES (?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, bid.getAuctionId());
            ps.setString(2, bid.getBidderName());
            ps.setDouble(3, bid.getAmount());
            ps.setTimestamp(4, new Timestamp(bid.getBidTime().getTime()));

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new BidException("Creating bid failed, no rows affected.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    bid.setId(generatedKeys.getLong(1));
                } else {
                    throw new BidException("Creating bid failed, no ID obtained.");
                }
            }
        } catch (Exception e) {
            throw new BidException("Failed to create bid", e);
        }

        // 3. UPDATE THE SINGLETON with the new high bid
        auctionRegistry.updateHighBid(bid.getAuctionId(), bid.getAmount());
        System.out.println("New high bid for auction " + bid.getAuctionId() + " is " + bid.getAmount());

        return bid;
    }
}