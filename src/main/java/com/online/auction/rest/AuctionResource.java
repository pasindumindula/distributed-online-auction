package com.online.auction.rest;

import com.online.auction.ejb.AuctionManagerBean;
import com.online.auction.ejb.BidManagerBean;
import com.online.auction.entity.Auction;
import com.online.auction.entity.Bid;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.util.List;

@Path("/auctions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuctionResource {

    @PersistenceContext(unitName = "AuctionPU")
    private EntityManager em;

    @Inject
    private AuctionManagerBean auctionManager;

    @Inject
    private BidManagerBean bidManager;

    @GET
    public List<Auction> list() {
        TypedQuery<Auction> q = em.createQuery("SELECT a FROM Auction a WHERE a.active = TRUE", Auction.class);
        return q.getResultList();
    }

    @POST
    @Path("/{id}/bid")
    public Response placeBid(@PathParam("id") Long id, BidRequest req) {
        try {
            Bid bid = bidManager.placeBid(id, req.userId, req.amount);
            return Response.ok(bid).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.serverError().entity(ex.getMessage()).build();
        }
    }

    public static class BidRequest {
        public Long userId;
        public double amount;
    }
}
