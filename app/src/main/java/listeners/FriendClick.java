package listeners;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import models.User;
import server.Server;
import solutions.lhdev.app.app.ConversationActivity;

/**
 * Created by JuanIgnacio on 10/06/2017.
 */
public class FriendClick implements View.OnClickListener {

    private User friend;

    public FriendClick(User friend) {
        this.friend = friend;
    }

    @Override
    public void onClick(View v) {
        new GetFriends().execute(v.getContext());
    }

    private class GetFriends extends AsyncTask<Context, Void, Integer> {

        private Context context;

        @Override
        protected Integer doInBackground(Context... contexts) {
            try {
                context = contexts[0];
                return Server.getInstance().addConversation(friend);
            } catch (Exception e) {
                Log.e("FriendClick", e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Integer conversation)
        {
            Intent intent = new Intent(context, ConversationActivity.class);
            intent.putExtra("conversationId", conversation);
            intent.putExtra("friend", friend);
            context.startActivity(intent);
        }

    }
}
