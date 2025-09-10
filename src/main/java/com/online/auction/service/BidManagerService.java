package com.online.auction.service;

import jakarta.ejb.Stateless;
import jakarta.ejb.EJB;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import com.online.auction.entity.Bid;
import com.online.auction.exception.BidException;

import javax.sql.DataSource;

@Stateless
public class BidManagerService {

    @EJB
    private AuctionRegistry auctionRegistry;

    @Resource(lookup = "jdbc/auctionDB")
    private javax.sql.DataSource dataSource;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Bid createBid(Bid bid) {
        // 1. Check against in-memory Singleton for speed
        Double currentHighBid = auctionRegistry.getHighBid(bid.getAuctionId());
        if (currentHighBid != null && bid.getAmount() <= currentHighBid) {
            System.out.println("Bid rejected. " + bid.getAmount() + " is not higher than " + currentHighBid);
            return null;
        }

        // 2. Database operation
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

        // 3. Update Singleton with new high bid
        auctionRegistry.updateHighBid(bid.getAuctionId(), bid.getAmount());
        System.out.println("New high bid for auction " + bid.getAuctionId() + " is " + bid.getAmount());

        return bid;
    }

    public Double getHighestBidFromDB(Long auctionId) {
        String sql = "SELECT MAX(amount) as max_bid FROM bid WHERE auction_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, auctionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("max_bid");
                }
            }
        } catch (Exception e) {
            throw new BidException("Failed to get highest bid from DB", e);
        }
        return null;
    }
}