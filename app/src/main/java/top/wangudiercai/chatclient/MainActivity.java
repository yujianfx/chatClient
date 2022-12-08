package top.wangudiercai.chatclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.*;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import top.wangudiercai.common.MessageListAdapter;
import top.wangudiercai.common.ObjectMapperFactory;
import top.wangudiercai.domain.Identity;
import top.wangudiercai.domain.Message;
import top.wangudiercai.service.WebSocketService;
import top.wangudiercai.view.ChatListView;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private WebSocketService.WebSockBind binder;
    private static ChatListView listView;
    private static List<Message> messages;
    private static MessageListAdapter adapter;
    private static ObjectMapper objectMapper = ObjectMapperFactory.getInstance();
    private Identity identity = new Identity();
    public static Handler handler = new Handler((msg) -> {
        Log.i("MainActivity", "new message received");
        Message message = (Message) msg.getData().get("newMessage");
        Log.i("newMessage{}", message.toString());
        adapter.getMessages().add(message);
        setListViewHeightBasedOnChildren(listView);
        adapter.notifyDataSetChanged();
        return false;
    });

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent identityData = getIntent();
        identity.setName(identityData.getStringExtra("name"));
        identity.setAvatarUrl(identityData.getStringExtra("avatarUrl"));
        listView = findViewById(R.id.listView);
        messages = new LinkedList<>();
        messages.add(new Message(999999999L, "system", "welcome", LocalDateTime.now(), "https://avatars0.githubusercontent.com/u/17098981?s=460&v=4"));
        adapter = new MessageListAdapter(messages);
        Intent intent = new Intent(this, WebSocketService.class);
        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                binder = (WebSocketService.WebSockBind) service;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                binder = null;

            }
        }, Context.BIND_AUTO_CREATE);
        startService(intent);
        EditText editText = findViewById(R.id.editText);
        Button button = findViewById(R.id.button);
        button.setOnClickListener((v) -> {
            String text = editText.getText().toString();
            Message message = new Message(System.currentTimeMillis(), identity.getName(), text, LocalDateTime.now(), identity.getAvatarUrl());
            try {
                binder.sendMessage(objectMapper.writeValueAsString(message));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                editText.setText("");
            }

        });
        listView.setAdapter(adapter);
    }
}
