package models;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    public View buildMessage(Context context, boolean right)
    {
        LinearLayout l = new LinearLayout(context);
        l.setOrientation(LinearLayout.HORIZONTAL);
        l.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, context.getResources().getDisplayMetrics())));
        if (!right)
        {
            l.setGravity(Gravity.RIGHT);
        }
        else
        {
            l.setGravity(Gravity.LEFT);
        }

        TextView message = new TextView(l.getContext());
        message.setText(this.getMessageText());
        message.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        message.setFocusable(true);
        message.setFocusableInTouchMode(true);
        l.addView(message);

        return l;
    }

}
