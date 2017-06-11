package models;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.Serializable;

import listeners.SaveAttachment;
import solutions.lhdev.app.app.R;

/**
 * Created by JuanIgnacio on 26/05/2017.
 */

public class Message implements Serializable{
    private int id;

    private String messageText;

    private int userId;

    private User user;

    private String attachmentName;

    private byte[] attachmentBynary;

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

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public byte[] getAttachmentBynary() {
        return attachmentBynary;
    }

    public void setAttachmentBynary(byte[] attachmentBynary) {
        this.attachmentBynary = attachmentBynary;
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
        l.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        if (!right)
        {
            l.setGravity(Gravity.RIGHT);
            lp.setMargins(0,0,20,20);
        }
        else
        {
            l.setGravity(Gravity.LEFT);
            lp.setMargins(20,0,0,20);
        }
        l.setLayoutParams(lp);

        TextView message = new TextView(l.getContext());
        message.setText(this.getMessageText());
        message.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        message.setFocusable(true);
        message.setFocusableInTouchMode(true);

        if (this.getAttachmentName() != null)
        {
            l.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, context.getResources().getDisplayMetrics())));
            ImageButton attachment = new ImageButton(l.getContext());
            /*
            android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:src="@mipmap/clip"
                android:scaleType="fitCenter"
                android:background="@android:color/transparent"
             */
            attachment.setLayoutParams(new ViewGroup.LayoutParams((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics()), (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics())));
            attachment.setBackgroundColor(Color.TRANSPARENT);
            attachment.setImageResource(R.mipmap.clip);
            attachment.setScaleType(ImageView.ScaleType.FIT_START);
            l.addView(attachment);

            try {
                attachment.setOnClickListener(new SaveAttachment(this));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        l.addView(message);

        return l;
    }

}
