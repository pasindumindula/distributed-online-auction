package com.online.auction.websocket;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint("/ws/auctions")
public class AuctionWebSocket {

    private static final Set<Session> sessions =
            Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }

    @OnMessage
    public void onMessage(String msg, Session session) {
        // Not needed for now (clients only listen)
    }

    public static void broadcast(String message) {
        synchronized (sessions) {
            for (Session s : sessions) {
                try {
                    s.getBasicRemote().sendText(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
