package solutions.lhdev.app.app;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import Models.User;
import Server.Server;

public class ConversationActivity extends AppCompatActivity {

    private TextView tVFriend;
    private LinearLayout messagesContainer;
    private int conversationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tVFriend = (TextView) findViewById(R.id.tVFriend);
        messagesContainer = (LinearLayout) findViewById(R.id.messagesContainer);

        Intent intent = getIntent();

        User friend = (User) intent.getSerializableExtra("friend");
        SpannableString content = new SpannableString(friend.getName() + " " + friend.getLastName());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        tVFriend.setText(content);

        conversationId = intent.getIntExtra("conversationId", 0);

        if (conversationId == 0)
        {
            Toast.makeText(this, getResources().getString(R.string.errorRetrievingConversation), Toast.LENGTH_SHORT).show();
        }
        else
        {
            new GetConversations().execute(messagesContainer.getContext());
        }


    }
    private class GetConversations extends AsyncTask<Context, Void, ArrayList<View>> {
        @Override
        protected ArrayList<View> doInBackground(Context... contexts) {
            try {
                return Server.getInstance().getMessages(contexts[0], conversationId);
            } catch (Exception e) {
                Log.e("ConversationActivity", e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<View> result)
        {
            for (View message : result)
            {
                messagesContainer.addView(message);
            }
        }
    }

}
