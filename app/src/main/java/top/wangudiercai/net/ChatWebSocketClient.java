package top.wangudiercai.net;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import top.wangudiercai.chatclient.MainActivity;
import top.wangudiercai.common.MessageManager;
import top.wangudiercai.common.ObjectMapperFactory;
import top.wangudiercai.domain.Message;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;

public class ChatWebSocketClient extends WebSocketClient {
    private MessageManager messageManager;
    private ObjectMapper objectMapper = ObjectMapperFactory.getInstance();

    public ChatWebSocketClient(MessageManager messageManager) {
        super(URI.create("ws://www.wangudiercai.top:10800/msgWebsocket"));
        this.messageManager = messageManager;
    }

    @Override
    public void send(String message) {
        super.send(message);
        Log.i("Message sent {}", message);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.i("WebSocket opened,handshakedata: {}", handshakedata.toString());
    }

    @Override
    public void onMessage(String message) {
        Message s = null;
        try {
            s = objectMapper.readValue(message, Message.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        messageManager.addMessage(s);
        android.os.Message msg = MainActivity.handler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putSerializable("newMessage", s);
        msg.setData(bundle);
        msg.sendToTarget();
        Log.i("WebSocket Message received {}", message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.i("WebSocket closed, reason: {}", reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }
}
