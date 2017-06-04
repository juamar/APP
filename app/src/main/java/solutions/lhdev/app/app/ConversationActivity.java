package solutions.lhdev.app.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import models.Conversation;
import models.Message;
import models.User;
import server.Server;

public class ConversationActivity extends AppCompatActivity {

    private TextView tVFriend;
    private LinearLayout messagesContainer;
    private int conversationId;
    private EditText eTMessage;
    private ImageButton IBSend;
    private ScrollView sVConversationActivity;
    private ImageButton iBAttach;
    static final int REQUEST_BROWSE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tVFriend = (TextView) findViewById(R.id.tVFriend);
        messagesContainer = (LinearLayout) findViewById(R.id.messagesContainer);
        eTMessage = (EditText) findViewById(R.id.eTMessage);
        IBSend = (ImageButton) findViewById(R.id.IBSend);
        sVConversationActivity = (ScrollView) findViewById(R.id.sVConversationActivity);
        sVConversationActivity.fullScroll(View.FOCUS_DOWN);
        iBAttach = (ImageButton) findViewById(R.id.iBAttachment);

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

        IBSend.setOnClickListener(new SendClick());
        iBAttach.setOnClickListener(new AttachFile());


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
            result.get(result.size() - 1).requestFocus();
        }
    }
    private class SendClick implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            if (eTMessage.getText().toString().trim().length() > 0) {
                new SendMessage().execute(new Message(1, eTMessage.getText().toString().trim() , Server.getInstance().getUser(), new Conversation(conversationId), false, conversationId, Server.getInstance().getUser().getId()));
            }
        }
    }

    private class SendMessage extends AsyncTask<Message, Void, Void> {

        Message message;

        @Override
        protected Void doInBackground(Message... messages) {
            try {
                message = messages[0];
                Server.getInstance().sendMessage(message);
            } catch (Exception e) {
                Log.e("ConversationActivity", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v)
        {
            View messageView = message.buildMessage(messagesContainer.getContext(),true);
            messagesContainer.addView(messageView);
            messageView.requestFocus();
            eTMessage.setText("");

        }
    }

    private class AttachFile implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            selectFile();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_BROWSE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                Toast.makeText(ConversationActivity.this, uri.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void selectFile()
    {
        Intent intent = new Intent();
        intent.setType("*/*");
        if (Build.VERSION.SDK_INT < 19) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent = Intent.createChooser(intent, getResources().getString(R.string.selectFile));
        } else {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            String[] mimetypes = { "audio/*", "video/*" };
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
        }
        startActivityForResult(intent, REQUEST_BROWSE);
    }

}
