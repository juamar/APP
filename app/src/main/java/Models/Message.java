package Models;

/**
 * Created by JuanIgnacio on 26/05/2017.
 */

public class Message {
    private int id;

    private String messageText;

    private int userId;

    private User user;

    private String attachment;

    private int conversationId;

    private Conversation conversation;

    private boolean isReaded;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
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

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
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

    public boolean getIsReaded() {
        return isReaded;
    }

    public boolean isReaded() {
        return isReaded;
    }

    public void setReaded(boolean readed) {
        isReaded = readed;
    }

    public Message(int id, String messageText, User user, Conversation conversation, boolean isReaded, int conversationId, int userId) {
        this.id = id;
        this.messageText = messageText;
        this.user = user;
        this.conversation = conversation;
        this.isReaded = isReaded;
        this.conversationId = conversationId;
        this.userId = userId;
    }

    public Message() {
    }
}
