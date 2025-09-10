package com.online.auction.rest;

import com.online.auction.entity.Auction;
import com.online.auction.entity.Bid;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.online.auction.service.AuctionManagerService;
import java.util.List;

@Path("/auction")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuctionResource {

    @EJB
    private AuctionManagerService auctionManager;

    @POST
    @Path("/bid")
    public Response placeBid(Bid bid) {
        try {
            boolean success = auctionManager.placeBid(bid);
            if (success) {
                return Response.ok().entity("{\"status\": \"success\", \"message\": \"Bid placed successfully\"}").build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"status\": \"error\", \"message\": \"Bid amount too low\"}").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"status\": \"error\", \"message\": \"Failed to place bid: " + e.getMessage() + "\"}").build();
        }
    }

    @GET
    @Path("/status")
    public Response getStatus() {
        return Response.ok().entity("{\"status\": \"Auction system is running\"}").build();
    }

    @GET
    @Path("/active")
    public Response getActiveAuctions() {
        try {
            // Get active auctions from your auctionManager service
            List<Auction> activeAuctions = auctionManager.getActiveAuctions();
            return Response.ok(activeAuctions).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Failed to retrieve auctions: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getAuction(@PathParam("id") Long id) {
        try {
            Auction auction = auctionManager.getAuction(id);
            if (auction != null) {
                return Response.ok(auction).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Auction not found\"}")
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Failed to retrieve auction: " + e.getMessage() + "\"}")
                    .build();
        }
    }
}