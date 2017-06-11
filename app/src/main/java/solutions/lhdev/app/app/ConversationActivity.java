package solutions.lhdev.app.app;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import models.Conversation;
import models.Message;
import models.User;
import server.Server;
import android.Manifest;

public class ConversationActivity extends AppCompatActivity {

    private final int MY_PERMISSIONS_REQUEST = 1;
    private TextView tVFriend;
    private LinearLayout messagesContainer;
    private int conversationId;
    private EditText eTMessage;
    private ImageButton IBSend;
    private ScrollView sVConversationActivity;
    private ImageButton iBAttach;
    private static final int REQUEST_BROWSE = 1;
    private String attachmentName;
    private byte[] attachmentBynary;


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
        protected void onPostExecute(ArrayList<View> result) {
            for (View message : result) {
                messagesContainer.addView(message);
            }
            if (result.size() > 0) {
                result.get(result.size() - 1).requestFocus();
            }
        }
    }

    private class SendClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (eTMessage.getText().toString().trim().length() > 0) {
                Message message = new Message(1, eTMessage.getText().toString().trim(), Server.getInstance().getUser(), new Conversation(conversationId), false, conversationId, Server.getInstance().getUser().getId());
                if (attachmentName != null) {
                    message.setAttachmentName(attachmentName);
                    message.setAttachmentBynary(attachmentBynary);
                }
                new SendMessage().execute(message);
                eTMessage.setText("");
                attachmentName = null;
                attachmentBynary = new byte[1024];
            }
        }
    }

    private class SendMessage extends AsyncTask<Message, Void, Integer> {

        Message message;

        @Override
        protected Integer doInBackground(Message... messages) {
            try {
                message = messages[0];
                return Server.getInstance().sendMessage(message);
            } catch (Exception e) {
                Log.e("ConversationActivity", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result.equals(0))
            {
                View messageView = message.buildMessage(messagesContainer.getContext(), true);
                messagesContainer.addView(messageView);
                messageView.requestFocus();
            }
            if (result.equals(1))
            {
                Toast.makeText(ConversationActivity.this, getString(R.string.resend_please),Toast.LENGTH_LONG).show();
            }
            if (result.equals(2))
            {
                Toast.makeText(ConversationActivity.this, getString(R.string.send_message_error),Toast.LENGTH_LONG).show();
            }
        }
    }

    private class AttachFile implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            selectFile();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_BROWSE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                File file = new File(uri.getPath());
                InputStream iStream = null;
                try {
                    iStream = getContentResolver().openInputStream(uri);
                    attachmentBynary = getBytes(iStream);
                    attachmentName = getFileName(uri);

                    Toast.makeText(ConversationActivity.this, attachmentName + ": " + getResources().getString(R.string.fileAttached), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                } catch (OutOfMemoryError e) {
                    Toast.makeText(ConversationActivity.this, getResources().getString(R.string.fileToBig), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1000000;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public void selectFile() {
        Intent intent = new Intent();
        intent.setType("*/*");
        //if (Build.VERSION.SDK_INT < 19) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent = Intent.createChooser(intent, getResources().getString(R.string.selectFile));
       /** } else {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            String[] mimetypes = {"audio/*", "video/*"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
        }**/
        startActivityForResult(intent, REQUEST_BROWSE);
    }

    //register your activity onResume()
    @Override
    public void onResume() {
        super.onResume();
        this.registerReceiver(mMessageReceiver, new IntentFilter("unique_name"));
        this.registerReceiver(saveAttachReceiver, new IntentFilter("save"));
    }

    //Must unregister onPause()
    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(mMessageReceiver);
        this.unregisterReceiver(saveAttachReceiver);
    }


    //This is the handler that will manager to process the broadcast intent
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            messagesContainer.removeAllViews();
            new GetConversations().execute(messagesContainer.getContext());
        }
    };

    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();

        User friend = (User) intent.getSerializableExtra("friend");
        SpannableString content = new SpannableString(friend.getName() + " " + friend.getLastName());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        tVFriend.setText(content);

        conversationId = intent.getIntExtra("conversationId", 0);

        if (conversationId == 0) {
            Toast.makeText(this, getResources().getString(R.string.errorRetrievingConversation), Toast.LENGTH_SHORT).show();
        } else {
            new GetConversations().execute(messagesContainer.getContext());
        }

        IBSend.setOnClickListener(new SendClick());
        iBAttach.setOnClickListener(new AttachFile());
    }


    //This is the handler that will manager to process the broadcast intent
    private BroadcastReceiver saveAttachReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            ActivityCompat.requestPermissions(ConversationActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //This code is usefull for Android prior to 6.0
                    File file=new File(Environment.getExternalStorageDirectory(), Server.getInstance().getMessageAttachment().getAttachmentName());

                    if (file.exists()) {
                        file.delete();
                    }

                    try {
                        FileOutputStream fos=new FileOutputStream(file.getPath());
                        System.out.println(file.getPath());
                        fos.write(Server.getInstance().getMessageAttachment().getAttachmentBynary());
                        fos.close();
                        Toast.makeText(this, getString(R.string.fileLocation) + file.getPath(), Toast.LENGTH_LONG).show();
                    }
                    catch (java.io.IOException e) {
                        Log.e("SavingFile", "Exception in photoCallback", e);
                    }
                }
                else
                {
                    Toast.makeText(this, getString(R.string.write_pemission_denied), Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

}
