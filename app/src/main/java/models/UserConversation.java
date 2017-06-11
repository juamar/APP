package models;

/**
 * Created by JuanIgnacio on 26/05/2017.
 */

public class UserConversation {
    private int id;

    private int userId;

    private User user;

    private int conversationId;

    private Conversation conversation;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public UserConversation(int conversationId, int userId) {
        this.conversationId = conversationId;
        this.userId = userId;
    }

    public UserConversation() {
    }
}
