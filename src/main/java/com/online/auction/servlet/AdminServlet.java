package com.online.auction.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ejb.EJB;
import java.io.IOException;
import com.online.auction.service.AuctionRegistry;

@WebServlet("/admin/initAuction")
public class AdminServlet extends HttpServlet {

    @EJB
    private AuctionRegistry auctionRegistry;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Long auctionId = Long.parseLong(request.getParameter("auctionId"));
            Double startingPrice = Double.parseDouble(request.getParameter("startingPrice"));

            auctionRegistry.registerAuction(auctionId, startingPrice);
            response.getWriter().println("Auction " + auctionId + " registered with starting price " + startingPrice);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Invalid auction ID or starting price");
        }
    }
}