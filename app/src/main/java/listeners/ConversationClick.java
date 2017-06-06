package listeners;

import android.content.Intent;
import android.view.View;

import models.User;
import solutions.lhdev.app.app.ConversationActivity;

/**
 * Created by JuanIgnacio on 29/05/2017.
 */

public class ConversationClick implements View.OnClickListener
{
    private int conversationId;
    private User friend;

    public ConversationClick(int conversationId, User friend) {
        this.conversationId = conversationId;
        this.friend = friend;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), ConversationActivity.class);
        intent.putExtra("conversationId", conversationId);
        intent.putExtra("friend", friend);
        v.getContext().startActivity(intent);
    }
}
