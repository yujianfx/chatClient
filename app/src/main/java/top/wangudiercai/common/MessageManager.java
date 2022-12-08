package top.wangudiercai.common;

import top.wangudiercai.domain.Message;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class MessageManager {
    private TreeSet<Message> MESSAGES = new TreeSet<>();
    private static MessageManager instance;

    //单例模式工厂方法
    public static MessageManager getInstance() {
        if (instance == null) {
            synchronized (MessageManager.class) {
                if (instance == null) {
                    instance = new MessageManager();
                }
            }
        }
        return instance;
    }


    public void addMessage(Message message) {
        MESSAGES.add(message);
    }

    public Integer getMessageCount() {
        return MESSAGES.size();
    }

    public LinkedList<Message> getMessages() {
        return MESSAGES.stream().collect(Collectors.toCollection(LinkedList::new));
    }
}
