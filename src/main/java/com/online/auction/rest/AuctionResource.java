package com.online.auction.rest;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.online.auction.service.AuctionManagerService;
import com.online.auction.entity.Bid;

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
}