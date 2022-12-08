package top.wangudiercai.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import top.wangudiercai.common.MessageManager;
import top.wangudiercai.domain.Message;
import top.wangudiercai.net.ChatWebSocketClient;

import java.util.List;

public class WebSocketService extends Service {
    private WebSockBind mBinder;
    private ChatWebSocketClient chatWebSocketClient;
    private MessageManager messageManager;

    public class WebSockBind extends Binder {
        public WebSocketService getService() {
            return WebSocketService.this;
        }

        public List<Message> getMessageList() {
            return messageManager.getMessages();
        }

        public void sendMessage(String message) {
            chatWebSocketClient.send(message);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        messageManager = MessageManager.getInstance();
        chatWebSocketClient = new ChatWebSocketClient(messageManager);
        mBinder = new WebSockBind();
        Log.i("WebSocketService", "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        chatWebSocketClient.connect();
        new Thread(() -> {
            while (true) {
                if (chatWebSocketClient.isClosed()) {
                    chatWebSocketClient.reconnect();
                } else {
                    chatWebSocketClient.send("");
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("WebSocketService", "onDestroy");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


}
