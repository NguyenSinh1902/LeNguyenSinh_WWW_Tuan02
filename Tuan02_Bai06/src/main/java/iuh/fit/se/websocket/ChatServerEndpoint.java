package iuh.fit.se.websocket;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;


@ServerEndpoint("/chat/{username}")
public class ChatServerEndpoint {

    private static Set<Session> clients = new CopyOnWriteArraySet<>();
    private static Gson gson = new Gson();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException {
        session.getUserProperties().put("username", username);
        clients.add(session);

        broadcast(new Message("System", username + " joined the chat!"));
    }

    @OnMessage
    public void onMessage(String jsonMessage, Session session) throws IOException {
        String username = (String) session.getUserProperties().get("username");
        Message msg = new Message(username, jsonMessage);
        broadcast(msg);
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        String username = (String) session.getUserProperties().get("username");
        clients.remove(session);
        broadcast(new Message("System", username + " left the chat."));
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    private void broadcast(Message message) throws IOException {
        String json = gson.toJson(message);
        for (Session client : clients) {
            if (client.isOpen()) {
                client.getBasicRemote().sendText(json);
            }
        }
    }
}
