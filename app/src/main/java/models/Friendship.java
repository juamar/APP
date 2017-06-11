package models;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import listeners.ConversationClick;
import listeners.FriendClick;
import solutions.lhdev.app.app.R;

/**
 * Created by JuanIgnacio on 10/06/2017.
 */

public class Friendship {
    public int Id;

    public int UserRequesterId;

    public int UserId;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getUserRequesterId() {
        return UserRequesterId;
    }

    public void setUserRequesterId(int userRequesterId) {
        UserRequesterId = userRequesterId;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public View buildConversation(User friend, Context context)
    {
        LinearLayout l = new LinearLayout(context);

        l.setOrientation(LinearLayout.HORIZONTAL);
        l.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 75, context.getResources().getDisplayMetrics())));
        l.setBackgroundResource(R.drawable.border);
        float horizontalMargin = 18;
        l.setPadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, horizontalMargin, context.getResources().getDisplayMetrics()),0,(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, horizontalMargin, context.getResources().getDisplayMetrics()),0);
        l.setWeightSum(1f);

        LinearLayout l1 = new LinearLayout(l.getContext());
        l1.setOrientation(LinearLayout.VERTICAL);
        l1.setLayoutParams(new LinearLayout.LayoutParams( (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, context.getResources().getDisplayMetrics()), ViewGroup.LayoutParams.MATCH_PARENT,0.6f));
        l.addView(l1);

        TextView userName = new TextView(l1.getContext());
        userName.setText(friend.getName() + " " + friend.getLastName());
        userName.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        userName.setGravity(Gravity.CENTER_VERTICAL);
        l1.addView(userName);

        LinearLayout l4 = new LinearLayout(l.getContext());
        l4.setOrientation(LinearLayout.VERTICAL);
        l4.setLayoutParams(new LinearLayout.LayoutParams( (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, context.getResources().getDisplayMetrics()), ViewGroup.LayoutParams.MATCH_PARENT,0.1f));
        l.addView(l4);

        LinearLayout l5 = new LinearLayout(l4.getContext());
        l5.setOrientation(LinearLayout.HORIZONTAL);
        l5.setLayoutParams(new LinearLayout.LayoutParams( (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, context.getResources().getDisplayMetrics()), ViewGroup.LayoutParams.MATCH_PARENT,0.3f));
        l.addView(l5);

        ImageButton go = new ImageButton(l5.getContext());
        go.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 0.5f));
        go.setBackgroundColor(Color.TRANSPARENT);
        go.setImageResource(R.mipmap.go);

        go.setOnClickListener(new FriendClick(friend));

        l5.addView(go);

        return l;
    }
}
