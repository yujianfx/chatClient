package top.wangudiercai.common;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import top.wangudiercai.chatclient.R;
import top.wangudiercai.common.MessageManager;
import top.wangudiercai.domain.Message;
import top.wangudiercai.view.CircularImageView;

import java.util.List;

public class MessageListAdapter extends BaseAdapter {
    private List<Message> messages;

    public MessageListAdapter(List<Message> messages) {
        this.messages = messages;
    }

    public MessageListAdapter() {
    }

    public List<Message> getMessages() {
        return messages;
    }

    public MessageListAdapter setMessages(List<Message> messages) {
        this.messages = messages;
        return this;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return messages.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.message_item, null);
        }
        Message message = (Message) getItem(position);
        Log.i("message", message.toString() + "" + position);
        TextView name = convertView.findViewById(R.id.name);
        TextView messageText = convertView.findViewById(R.id.message_item_text);
        CircularImageView avatar = convertView.findViewById(R.id.avatar);
        name.setText(message.getName());
        messageText.setText(message.getMessage());
        DisplayImageOptions options = new DisplayImageOptions
                .Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnFail(R.drawable.img_2)
                .showImageOnLoading(R.drawable.img_3)
                .build();
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(parent.getContext()));
        imageLoader.displayImage(message.getAvatarUrl(), avatar, options);
        return convertView;

    }

}
