package top.wangudiercai.domain;

import java.time.LocalDateTime;


public class Message implements java.io.Serializable, Comparable<Message> {
    private Long id;
    private String name;
    private String message;
    private LocalDateTime time;
    private String avatarUrl;
    private static final long serialVersionUID = 1L;

    @Override
    public int compareTo(Message o) {
        if (this.time.isAfter(o.time)) {
            return -1;
        }
        if (this.time.isBefore(o.time)) {
            return 1;
        }
        return 0;
    }

    public Message() {
    }

    public Message(String name, String message, LocalDateTime time, String avatarUrl) {
        this.name = name;
        this.message = message;
        this.time = time;
        this.avatarUrl = avatarUrl;
    }

    public Message(Long id, String name, String message, LocalDateTime time, String avatarUrl) {
        this.id = id;
        this.name = name;
        this.message = message;
        this.time = time;
        this.avatarUrl = avatarUrl;
    }

    public Long getId() {
        return id;
    }

    public Message setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Message)) {
            return false;
        }

        Message message1 = (Message) o;

        if (getName() != null ? !getName().equals(message1.getName()) : message1.getName() != null) {
            return false;
        }
        if (getMessage() != null ? !getMessage().equals(message1.getMessage()) : message1.getMessage() != null) {
            return false;
        }
        if (getTime() != null ? !getTime().equals(message1.getTime()) : message1.getTime() != null) {
            return false;
        }
        return getAvatarUrl() != null ? getAvatarUrl().equals(message1.getAvatarUrl()) : message1.getAvatarUrl() == null;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getMessage() != null ? getMessage().hashCode() : 0);
        result = 31 * result + (getTime() != null ? getTime().hashCode() : 0);
        result = 31 * result + (getAvatarUrl() != null ? getAvatarUrl().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", message='" + message + '\'' +
                ", time=" + time +
                ", avatarUrl='" + avatarUrl + '\'' +
                '}';
    }
}
