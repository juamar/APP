package models;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import listeners.ConversationClick;
import solutions.lhdev.app.app.R;

/**
 * Created by JuanIgnacio on 26/05/2017.
 */

public class Conversation {
    private int id;

    private ArrayList<Message> messages;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public Conversation(int id) {
        this.id = id;
    }

    public Conversation() {

    }

    /**
     * This method builds a conversation View for the InicioActivity.
     * @param friend The Friend we are talking to.
     * @param lastMessage The Last Message in the conversation.
     * @param messagesNotReaded Messages Marked as not readed in the conversation.
     * @param context The context for the View.
     * @return A View ready to be added to the InicioActivity.
     */
    public View buildConversation(User friend, Message lastMessage, int messagesNotReaded, Context context)
    {
        LinearLayout l = new LinearLayout(context);

        l.setOrientation(LinearLayout.HORIZONTAL);
        l.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 75, context.getResources().getDisplayMetrics())));
        l.setBackgroundResource(R.drawable.border);
        float horizontalMargin = 18;
        l.setPadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, horizontalMargin, context.getResources().getDisplayMetrics()),0,(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, horizontalMargin, context.getResources().getDisplayMetrics()),0);
        l.setWeightSum(1f);

        LinearLayout l1 = new LinearLayout(l.getContext());
        l1.setOrientation(LinearLayout.VERTICAL);
        l1.setLayoutParams(new LinearLayout.LayoutParams( (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, context.getResources().getDisplayMetrics()),LayoutParams.MATCH_PARENT,0.6f));
        l.addView(l1);

        LinearLayout l2 = new LinearLayout(l1.getContext());
        l2.setOrientation(LinearLayout.VERTICAL);
        l2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, context.getResources().getDisplayMetrics())));
        l2.setGravity(Gravity.CENTER_VERTICAL);
        l1.addView(l2);

        TextView userName = new TextView(l2.getContext());
        userName.setText(friend.getName() + " " + friend.getLastName());
        userName.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        l2.addView(userName);

        LinearLayout l3 = new LinearLayout(l1.getContext());
        l3.setOrientation(LinearLayout.VERTICAL);
        l3.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        l3.setGravity(Gravity.CENTER_VERTICAL);
        l1.addView(l3);

        TextView message = new TextView(l3.getContext());
        message.setText("- " + lastMessage.getMessageText());
        message.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        message.setEllipsize(TextUtils.TruncateAt.END);
        message.setMaxLines(1);
        l3.addView(message);

        LinearLayout l4 = new LinearLayout(l.getContext());
        l4.setOrientation(LinearLayout.VERTICAL);
        l4.setLayoutParams(new LinearLayout.LayoutParams( (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, context.getResources().getDisplayMetrics()),LayoutParams.MATCH_PARENT,0.1f));
        l.addView(l4);

        LinearLayout l5 = new LinearLayout(l4.getContext());
        l5.setOrientation(LinearLayout.HORIZONTAL);
        l5.setLayoutParams(new LinearLayout.LayoutParams( (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, context.getResources().getDisplayMetrics()),LayoutParams.MATCH_PARENT,0.3f));
        l.addView(l5);

        LinearLayout l6 = new LinearLayout(l5.getContext());
        l6.setOrientation(LinearLayout.VERTICAL);
        l6.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
        l6.setGravity(Gravity.CENTER_VERTICAL);
        l5.addView(l6);

        TextView notification = new TextView(l6.getContext());
        notification.setText(String.valueOf(messagesNotReaded));
        notification.setLayoutParams(new LayoutParams((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, context.getResources().getDisplayMetrics()), (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, context.getResources().getDisplayMetrics())));
        notification.setBackgroundResource(R.drawable.rounded_corner);
        notification.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        l6.addView(notification);

        ImageButton go = new ImageButton(l5.getContext());
        go.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 0.5f));
        go.setBackgroundColor(Color.TRANSPARENT);
        go.setImageResource(R.mipmap.go);

        go.setOnClickListener(new ConversationClick(this.getId(), friend));

        l5.addView(go);

        return l;
    }
}
